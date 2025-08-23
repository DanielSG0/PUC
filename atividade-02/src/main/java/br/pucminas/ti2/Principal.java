package br.pucminas.ti2;
import java.util.*;

public class Principal {
	public static Scanner sc = new Scanner(System.in);
	
	public static void menu() {
		System.out.println("========= Menu de Opções =========");
		
		System.out.println("1 - Listar veiculos disponíveis");
		System.out.println("2 - Inserir um novo veículo");
		System.out.println("3 - Excluir um veiculo");
		System.out.println("4 - Atualizar veiculo");
		System.out.println("5 - Buscar veiculo por ID");
		System.out.println("0 - Sair");
		
		System.out.println("==================================");
	}
	
	public static void listarVeiculos(DAO dao) {
		Concessionaria[] veiculos = dao.getVeiculos();
		if(veiculos != null && veiculos.length > 0) {
			//Listar todos os veiculos
			System.out.println("====== Veículos disponíveis ======");
			for (int i = 0; i < veiculos.length; i++) {
				System.out.println(veiculos[i].toString());
			}
		}else {
			System.out.println("Nenhum veículo cadastrado.");
		}
	}
	
	/*public static boolean procurarVeiculo(DAO dao, int codigoVeiculoDeletado) {
		Concessionaria[] veiculos = dao.getVeiculos();
		boolean encontrado = false;
		for (Concessionaria veiculoAchado : veiculos) {
			if(veiculoAchado.getId() == codigoVeiculoDeletado) {
				dao.excluirVeiculo(codigoVeiculoDeletado);
				return true;
			}
		}
		
		return false;
	}*/
	
	public static void alterarTodosOsDadosVeiculo(DAO dao, Concessionaria veiculo) {
		System.out.println("Digite a nova marca: ");
		veiculo.setMarca(sc.nextLine());
		
		System.out.println("Digite o novo ano: ");
		veiculo.setAno(sc.nextInt());
		sc.nextLine();
		
		System.out.println("Digite a nova cor: ");
		veiculo.setCor(sc.nextLine());
		
		dao.atualizarVeiculo(veiculo);
	}
	
	public static void buscarVeiculo(DAO dao, int id_veiculo) {
		Concessionaria veiculo = dao.getVeiculoId(id_veiculo);
		
		if(veiculo != null) {
			System.out.println(veiculo.toString());
		}else {
			System.out.println("Veículo não encontrado.");
		}
	}
	
	public static void alterarDadoVeiculo(DAO dao, Concessionaria veiculo) {
		int subMenu = 0;
		do {
			System.out.println("O que deseja alterar?");
			
			System.out.println("1 - Marca");
			System.out.println("2 - Ano");
			System.out.println("3 - Cor");
			System.out.println("4 - Todos os dados");
			System.out.println("0 - Salvar e Sair");
			subMenu = sc.nextInt();
			sc.nextLine();
			
			switch (subMenu) {
			case 1:
				System.out.println("Digite a nova marca: ");
				veiculo.setMarca(sc.nextLine());
				break;
			case 2:
				System.out.println("Digite o novo ano: ");
				veiculo.setAno(sc.nextInt());
				sc.nextLine();
				break;
			case 3:
				System.out.println("Digite a nova cor: ");
				veiculo.setCor(sc.nextLine());
				break;
			case 4:
				alterarTodosOsDadosVeiculo(dao, veiculo);
				break;
			case 0:
				if(dao.atualizarVeiculo(veiculo)) {
					System.out.println("Veiculo atualizado com sucesso!");
				}
				break;
			default:
				System.out.println("Opção inválida.");
				break;
			}
		} while (subMenu != 0);
	}
	
	public static void deletarVeiculo(DAO dao, int codigoVeiculoDeletado) {
		//boolean encontrado = procurarVeiculo(dao, codigoVeiculoDeletado);
		boolean status = dao.excluirVeiculo(codigoVeiculoDeletado);
		if(status != false) {
			System.out.println("Veículo deletado com sucesso!");
		}else {
			System.out.println("Código de veículo não encontrado na nossa base de dados.");
		}
	}
	
	public static boolean inserirVeiculo(DAO dao, String marca, int ano, String cor) {
		
		boolean inserido = false;
		
		Concessionaria veiculo = new Concessionaria(marca, ano, cor);
		if(dao.inserirVeiculo(veiculo) == true) {
			inserido = true;
		}else {
			inserido = false;
		}
		
		return inserido;
	}
	
	public static void main (String args[]) {
		DAO dao = new DAO();
		
		dao.conectar();
		int idAtualizar = 0;
		int idVeiculoProcurado = 0;
		int op = 0;
		
		do {
			menu();
			op = sc.nextInt();
			sc.nextLine();
			
			switch (op) {
			case 1:
				listarVeiculos(dao);
				break;
			case 2:
				System.out.println("Digite a marca: ");
				String marca = sc.nextLine();
				
				System.out.println("Digite o ano: ");
				int ano = sc.nextInt();
				sc.nextLine();
				
				System.out.println("Digite a cor: ");
				String cor = sc.nextLine();
				
				if(inserirVeiculo(dao, marca, ano, cor) == true) {
					System.out.println("Veiculo inserido com sucesso!");
				}else {
					System.out.println("Falha ao tentar adicionar um veículo.");
				}
				
				break;
				
			case 3: 
				listarVeiculos(dao);
				System.out.println("Digite o codigo do veiculo que deseja deletar: ");
				int veiculoDeletado = sc.nextInt();
				sc.nextLine();
				deletarVeiculo(dao, veiculoDeletado);
				break;
				
			case 4:
				listarVeiculos(dao);
				System.out.println("Digite o código do veiculo que deseja atualizar: ");
				idAtualizar = sc.nextInt();
				sc.nextLine();
				
				Concessionaria veiculo = dao.getVeiculoId(idAtualizar);
				if(veiculo !=  null) {
					alterarDadoVeiculo(dao, veiculo);
				}else {
					System.out.println("Veiculo nao encontrado.");
				}
				
				break;
			case 5:
				System.out.println("Digite o ID do veiculo que deseja buscar");
				idVeiculoProcurado = sc.nextInt();
				sc.nextLine();
				
				buscarVeiculo(dao, idVeiculoProcurado);
				break;
			case 0:
				System.out.println("Bye bye..");
				break;
			default:
				System.out.println("Valor inválido! Digite uma das opções acima.");
				break;
			}
		} while (op != 0);
	}
}
