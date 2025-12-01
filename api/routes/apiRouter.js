//Importamos a biblioteca do express
const express = require ('express')

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

/**
.get: Define que essa rota só aceita requisições do tipo GET (o tipo padrão quando você digita um link no navegador).

endpoint + 'produtos': O computador junta as duas strings. / + produtos vira o caminho final: /produtos.

function (req, res): É a função que roda quando alguém entra nessa rota.

req (Request/Requisição): Traz informações de quem está pedindo (dados do usuário, navegador, etc).

res (Response/Resposta): É a ferramenta que você usa para enviar a resposta de volta.
 */


apiRouter.get (endpoint + 'produtos', function (req, res) {
//Sucesso - status: 200!
//esse .json pega o objeto js da lista_produtos converte para JSON e manda de volta pra quem pediu
res.status(200).json (lista_produtos)
})


/**
O que faz: O Node.js trata cada arquivo como uma ilha isolada. O que acontece em apiRouter.js fica em apiRouter.js.

Para que serve: Essa linha diz: "Quando o server.js fizer um require deste arquivo, entregue para ele a variável apiRouter que configuramos abaixo". Sem isso, seu server.js receberia um objeto vazio e o site não funcionaria.
 */

module.exports = apiRouter;