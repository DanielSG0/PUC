exports.up = function (knex) {

    //Faz a criação da tabela
  return knex.schema.createTable("produto", (table) => {
    //Cria coluna ID autoincrement como chave primaria
    table.increments("id").primary();

    //cria coluna descricao como not null
    table.string("descricao").notNullable();
    table.decimal("valor", 10, 2).notNullable();
    table.string("marca").notNullable();

    // Cria magicamente duas colunas: "created_at" e "updated_at"
    // Para saber quando o produto foi criado e quando foi alterado.    
    table.timestamps(true, true);
  });
};

exports.down = function (knex) {
    //Se der ruim apaga a tabela produto
  return knex.schema.dropTable("produto");
};