//Importamos a biblioteca do express
const express = require ('express')
const bcrypt = require ('bcryptjs')
const jwt = require ('jsonwebtoken')

/**
 * 
Aqui está a mágica. O apiRouter funciona como um "mini-servidor". Ele não consegue ouvir uma porta (não tem listen), mas ele consegue agrupar rotas (caminhos).

Analogia: O app (lá no server.js) é a tomada principal da parede. O apiRouter é uma régua (filtro de linha) que você liga na tomada para ter mais entradas.
 */
let apiRouter = express.Router() /* '*' */

const endpoint = '/'

//bd fake
const lista_produtos = {
    produtos: [
        { id: 1, descricao: "Produto 1", valor: 5.00, marca: "marca " },
        { id: 2, descricao: "Produto 2", valor: 5.00, marca: "marca " },
        { id: 3, descricao: "Produto 3", valor: 5.00, marca: "marca " },
    ]
}

const knex = require('knex')({
    client: 'pg',
    debug: true,
    connection: {
        connectionString : process.env.DATABASE_URL,
        ssl: { rejectUnauthorized: false },
    }
});

/**
.get: Define que essa rota só aceita requisições do tipo GET (o tipo padrão quando você digita um link no navegador).

endpoint + 'produtos': O computador junta as duas strings. / + produtos vira o caminho final: /produtos.

function (req, res): É a função que roda quando alguém entra nessa rota.

req (Request/Requisição): Traz informações de quem está pedindo (dados do usuário, navegador, etc).

res (Response/Resposta): É a ferramenta que você usa para enviar a resposta de volta.
 */


apiRouter.get (endpoint + 'produtos', (req, res) => {
//Sucesso - status: 200!
//esse .json pega o objeto js da lista_produtos converte para JSON e manda de volta pra quem pediu
    knex.select("*").from('produto')
    .then( produto => res.status(200).json(produtos) )
    .catch(err => {
        res.status(500).json({
            message: 'Erro ao recuperar produtos - ' + err.message
        })
    })

// res.status(200).json (lista_produtos)
})

apiRouter.get(endpoint + 'produtos/:id', (req, res) =>{
    knex.select("*").from('produto').where("id", req.params.id).first()
    .then(produto => {
        if (produto != undefined) {
            res.status(200).json(produto)
        }else{
            //Se der erro é bom retornar um objeto json também 
            //Pra quem estiver mexendo no front end conseguir identificar o erro
            res.status(404).json({message: "Produto não encontrado para este ID"})
        }
    })
    .catch(err => {
        res.status(500).json({
            message: 'Erro ao buscar produto: ' + err.message
        })
    })
})

apiRouter.post(endpoint + 'produtos', (req, res) =>{
    let novo_produto = {
        descricao: req.body.descricao,
        valor: req.body.valor,
        marca: req.body.marca
    }

    /*
    O PGSQL ele nao devolve os dados inseridos, so diz que inseriu
    pra conseguir devolver o produto criado com o id pro frontend eu 
    adiciono o comando na corrente do knex .returning('*') -> retorne
    todas as colunas do que acabou de ser criado
    
    obs: passar o nome da tabela a ser inserida dentro dos parametros do knex
    */

    //Esse returning retorna um array, mesmo que com uma posicao, ou seja
    //coloco [0] pra retornar apenas o objeto solto que fica melhor pro front end
    knex('produto').insert(novo_produto).returning('*')
    .then(dados => res.status(201).json(dados[0]))
    .catch(err =>{
        res.status(500).json({
            message: "Erro ao inserir o objeto: " + err.message
        })
    })
})

apiRouter.put(endpoint + 'produtos/:id', (req, res) => {

    let produto_atualizado = {
        descricao: req.body.descricao,
        marca: req.body.marca,
        valor: req.body.valor
    }

    knex('produto').update(produto_atualizado).where("id", req.params.id)
    .then(resp => {
        if(resp > 0){
            res.status(200).json({
                message: "Objeto atualizado com sucesso!"
            })
        }else{
            res.status(404).json({
                message: "Objeto não encontrado."
            })
        }
    })
    .catch(err => {
        res.status(500).json({
            message: "Erro ao atualizar produto: " + err.message
        })
    })
})

apiRouter.delete(endpoint + 'produtos/:id', (req, res) => {

    knex('produto').where("id", req.params.id).delete()
    .then(resp => {
        if(resp > 0){
            res.status(200).json({
                message: "Produto deletado com sucesso!"
            })
        }else{
            res.status(404).json({
                message: "Produto não encontrado."
            })
        }
    })
    .catch(err => {
        res.status(500).json({
            message: "Erro ao remover produto: " + err.message
        })
    })

})

apiRouter.post (endpoint + 'seguranca/register', (req, res) =>{
    knex ('usuario')
        .insert({
            nome: req.body.nome,
            login: req.body.login,
            senha: bcrypt.hashSync(req.body.senha, 8),
            email: req.body.email

            //Esse ['id'] diz pra salvar e retornar apenas o id Gerado
        }, ['id'])
        .then((result) =>{
            let usuario = result[0] //Pega o primeiro item que é o objeto {id: 1}
            res.status(200).json({"id": usuario.id})
            return
        })
        .catch(err => {
            res.status(500).json({
                message: "Erro ao registrar o usuario - " + err.message
            })
        })
})

apiRouter.post(endpoint + 'seguranca/login', (req, res) => {
    //Procura no bd se tem alguem com aquele login inserido pelo usuario
    knex.select('*').from('usuario').where({login: req.body.login})
    .then(usuarios => {
        if(usuarios.length){
            let usuario = usuarios[0]
            //Pega a senha que o usuario digitou, criptografa e compara ela na hora com a do banco. Se baterem retorna true
            let checkSenha = bcrypt.compareSync (req.body.senha, usuario.senha)
            if (checkSenha){
                var tokenJWT = jwt.sign({ id: usuario.id},
                    process.env.SECRET_KEY, {
                    expiresIn: 3600
                })
                res.status(200).json({
                    id: usuario.id,
                    login: usuario.login,
                    nome: usuario.nome,
                    roles: usuario.roles,
                    token: tokenJWT
                })
                return
            }
        }
        res.status(200).json({message: 'Login ou senha incorretos'})
    })
    .catch(err =>{
        res.status(500).json({
            message: 'Erro ao verificar login - ' + err.message
        })
    })
})

/**
O que faz: O Node.js trata cada arquivo como uma ilha isolada. O que acontece em apiRouter.js fica em apiRouter.js.

Para que serve: Essa linha diz: "Quando o server.js fizer um require deste arquivo, entregue para ele a variável apiRouter que configuramos abaixo". Sem isso, seu server.js receberia um objeto vazio e o site não funcionaria.
 */

module.exports = apiRouter;