/**
 * 
 * Esse arquivo ele serve para ensinar uma biblioteca chamada Knex 
 * (que é um gerenciador de banco de dados para Node.js) a encontrar o seu 
 * banco de dados, dependendo de onde o código está rodando.
 * 
 */

//Carrega as variaveis do .env
require("dotenv").config();

//Esse codigo divide a configuração em dois mundos

module.exports = {

    //Meu computador
    /**
     * Define como conectar quando eu estiver 
     * acessando pelo meu PC
     * 
     */
  development: {
    client: "pg",
    connection: {
      host: process.env.DB_HOST || "localhost",
      port: process.env.DB_PORT || 5432,
      database: process.env.DB_NAME || "produtos_db",
      user: process.env.DB_USER || "postgres",
      password: process.env.DB_PASSWORD || "postgres",
    },
    migrations: { directory: "./database/migrations" },
    seeds: { directory: "./database/seeds" },
  },

  //Railway
  /**
   * 
   * Define como conectar quando o site esta "no ar"
   * 
   */
  production: {
    client: "pg",
    connection: process.env.DATABASE_URL,

    migrations: { directory: "./database/migrations" },
    seeds: { directory: "./database/seeds" },
  }
};