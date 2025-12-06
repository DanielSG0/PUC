//Pega o botão do HTML
const botao = document.getElementById('btnEnviar');
const btnCarregar = document.getElementById('btnCarregar');
const lista = document.getElementById('listaProdutos');

//Fica vigiando quando alguem clicar nele
botao.addEventListener('click', function() {
    
    //Pega os valores que a pessoa digitou
    const desc = document.getElementById('descricao').value;
    const val = document.getElementById('valor').value;
    const mar = document.getElementById('marca').value;

    const token = localStorage.getItem('token');

    if(!token){
        alert("Você precisa estar logado para cadastrar");
        window.location.href = 'login.html';
        return;
    }

    //Monta o objeto pra mandar
    const dadosParaEnviar = {
        descricao: desc,
        valor: val,
        marca: mar
    };

    //Essa linha inicia um pedido (HTTP Request)
    //Ela vai bater na porta localhost:3000/api/produtos
    fetch('/api/produtos', {
        //Cabeçalho da requisição

        //tipo de ação
        method: 'POST',

        //é pra avisar que no cabeçalho contem dados pra consumir
        headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + token},

        //converte numa string pra enviar
        body: JSON.stringify(dadosParaEnviar)
    })
    .then(resposta => {
        if (!resposta.ok) throw new Error ("Erro na requisição: " + resposta.status);
        return resposta.json();
    })
    .then(json => {
        alert('Deu bom! Produto cadastrado com ID: ' + json.id);

        document.getElementById('descricao').value = '';
        document.getElementById('valor').value = '';
        document.getElementById('marca').value = '';

        console.log(json);
    })
    .catch(erro => {
        alert('Deu ruim! Provalmente você não é admin ou seu token expirou.');
        console.error(erro);
    });
});


btnCarregar.addEventListener('click', () =>{

    const token = localStorage.getItem('token');

    if(!token){
        alert("Você não esta logado.");
        window.location.href = 'login.html';
        return;
    }

    fetch('/api/produtos', {
        method: 'GET',
        headers: {'Authorization': 'Bearer ' + token}
    })
    .then(resp => resp.json())
    .then(produtos => {
        lista.innerHTML = '';

        produtos.forEach(prod => {
            const item = document.createElement('li');
            item.innerText = `${prod.descricao} - R$ ${prod.valor}`;
            lista.appendChild(item);
        });
    })
    .catch(err => console.error(err))
});