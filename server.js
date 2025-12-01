/**
 * Importa a biblioteca dotenv e executa o metodo config
 * Faz o que? Lê um arquivo chamado .env na raiz do meu projeto 
 * (onde guardo senhas e chaves secretas) e carrega essas variáveis para dentro 
 * do Node.js. Isso permite que eu use process.env.SUA_SENHA mais tarde, mantendo meus segredos fora do código.
 * 
 */
require('dotenv').config()

//Importa o framework principal que facilita a criação do servidor.
const express = require('express')

//Importa a biblioteca de Cross-Origin Resource Sharing. Ela define quem pode acessar sua API (ex: permitir que seu front-end em localhost:5173 converse com esse back-end em localhost:3000).
const cors = require('cors')

//É uma biblioteca nativa do Node (não precisa instalar). Ela serve para manipular caminhos de pastas de forma que funcione tanto no Windows (\) quanto no Linux/Mac (/) sem dar erro.
const path = require('path')

//O que faz: Cria uma instância da aplicação Express. A variável app agora é o seu servidor. Tudo o que você configurar (rotas, regras) será "pendurado" nessa variável app.
//Pelo que entendi, o servidor é criado aqui.
const app = express()

const apiRouter = require('./api/routes/apiRouter')
app.use ('/api', apiRouter)

//Essas linhas com app.use() são chamadas de Middlewares. Pense neles como FILTROS: toda requisição que chegar no servidor passa por aqui antes de chegar nas suas rotas.

//O que faz: Libera o acesso. Sem isso, se você tentar chamar esse back-end a partir de um front-end (React, Vue, etc.), o navegador vai bloquear a conexão por segurança.
app.use(cors())

//FUNDAMENTAL!!!
//Isso ensina o servidor a ler JSON. Quando alguém envia dados no corpo da requisição (req.body) em formato JSON, essa linha traduz isso para um objeto JavaScript que você consegue ler no código.
app.use(express.json()) /* '*' */

//Permite que o servidor entenda dados enviados através de formulários HTML padrão (aqueles que não enviam JSON, mas sim application/x-www-form-urlencoded). O extended: true permite objetos aninhados complexos.
app.use(express.urlencoded({ extended: true }))
/**
 * 
O que faz: Cria uma rota "falsa" para servir arquivos reais (imagens, CSS, HTML).

A lógica: Se o usuário acessar http://localhost:3000/app/foto.jpg, o servidor vai procurar esse arquivo fisicamente na pasta /public/foto.jpg dentro do seu projeto.

__dirname: É uma variável mágica que significa "o caminho da pasta onde este arquivo atual está salvo". Garante que o servidor ache a pasta public não importa de onde você rode o comando.
 */
app.use('/app', express.static(path.join(__dirname, '/public')))

//Define a porta. Ele tenta pegar a porta definida no arquivo .env (útil quando você coloca o site no ar, pois o servidor na nuvem escolhe a porta). Se não houver nenhuma definida, usa a 3000 como padrão.
let port = process.env.PORT || 3000

//"Liga" o servidor. Ele fica ouvindo (escutando) requisições na porta definida. O terminal vai ficar "preso" rodando esse processo.
app.listen(port)
