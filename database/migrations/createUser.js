exports.up = function(knex){

    //Criando tabela
    return knex.schema.createTable("usuario", (table) => {

        table.increments("id").primary()
        table.string("nome", 200).notNullable();
        table.string("email", 100).notNullable();
        table.string("login", 100).notNullable();
        table.string("senha", 100).notNullable();
        table.string("roles", 200).notNullable().defaultTo('USER');

    }); 

};

exports.down = function(knex){
    return knex.schema.dropTable("usuario");
};

