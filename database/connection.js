const knex = require('knex')
const knexConfig = require('../knexfile') //pega as configurações da raiz

// Essa linha decide onde estamos. 
// Se estiver no Railway, o NODE_ENV será 'production'. 
// Se estiver no seu PC, será 'undefined', então ele usa 'development'.

const environment = process.env.NODE_ENV || 'development'

// Pega a configuração certa do knexfile (a parte do Postgres local ou do Railway)
const config = knexConfig[environment]

// Cria a conexão final
const db = knex(config)

//Exporta a conexao pronta "db" pra ser usada nas rotas
module.exports = db
