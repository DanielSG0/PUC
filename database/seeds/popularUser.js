exports.seed = async function(knex) {
  // 1. Limpa a tabela antes de inserir (pra não duplicar se rodar de novo)
  await knex('usuario').del();

  // 2. Insere os dados do professor
  await knex('usuario').insert([
    {
      nome: 'user',
      login: 'user',
      senha: '$2a$08$tprzZIs1OTKVMaVzZWrKfe8rX3toatWD6lsvp4u9AR54mrbSSLX7e',
      email: 'user@abc.com.br',
      roles: 'USER'
    },
    {
      nome: 'admin',
      login: 'admin',
      senha: '$2a$08$tprzZIs1OTKVMaVzZWrKfe8rX3toatWD6lsvp4u9AR54mrbSSLX7e',
      email: 'admini@abc.com.br',
      roles: 'USER;ADMIN'
    }
  ]);
};