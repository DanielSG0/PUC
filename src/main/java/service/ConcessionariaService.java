package service;

import java.util.Scanner;
//import java.time.LocalDate;
import java.io.File;
//import java.time.LocalDateTime;
import java.util.List;
import dao.ConcessionariaDAO;
import model.Concessionaria;
import spark.Request;
import spark.Response;


public class ConcessionariaService {

	private ConcessionariaDAO concessionariaDAO = new ConcessionariaDAO();
	private String form;
	private final int FORM_INSERT = 1;
	private final int FORM_DETAIL = 2;
	private final int FORM_UPDATE = 3;
	private final int FORM_ORDERBY_ID = 1;
	private final int FORM_ORDERBY_ANO = 2;
	private final int FORM_ORDERBY_MARCA = 3;
	
	
	public ConcessionariaService() {
		makeForm();
	}

	
	public void makeForm() {
		makeForm(FORM_INSERT, new Concessionaria(), FORM_ORDERBY_MARCA);
	}

	
	public void makeForm(int orderBy) {
		makeForm(FORM_INSERT, new Concessionaria(), orderBy);
	}

	
	public void makeForm(int tipo, Concessionaria veiculo, int orderBy) {
		String nomeArquivo = "form.html";
		form = "";
		try{
			Scanner entrada = new Scanner(new File(nomeArquivo));
		    while(entrada.hasNext()){
		    	form += (entrada.nextLine() + "\n");
		    }
		    entrada.close();
		}  catch (Exception e) { System.out.println(e.getMessage()); }
		
		String umConcessionaria = "";
		if(tipo != FORM_INSERT) {
			umConcessionaria += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umConcessionaria += "\t\t<tr>";
			umConcessionaria += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/concessionaria/list/1\">Novo Veiculo</a></b></font></td>";
			umConcessionaria += "\t\t</tr>";
			umConcessionaria += "\t</table>";
			umConcessionaria += "\t<br>";			
		}
		
		if(tipo == FORM_INSERT || tipo == FORM_UPDATE) {
			String action = "/concessionaria/";
			String name, descricao, buttonLabel;
			if (tipo == FORM_INSERT){
				action += "insert";
				name = "Inserir Veiculo";
				descricao = "";
				buttonLabel = "Inserir";
			} else {
				action += "update/" + veiculo.getId();
				name = "Atualizar Veiculo (ID " + veiculo.getId() + ")";
				descricao = veiculo.getMarca();
				buttonLabel = "Atualizar";
			}
			umConcessionaria += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
			umConcessionaria += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umConcessionaria += "\t\t<tr>";
			umConcessionaria += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + name + "</b></font></td>";
			umConcessionaria += "\t\t</tr>";
			umConcessionaria += "\t\t<tr>";
			umConcessionaria += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umConcessionaria += "\t\t</tr>";
			umConcessionaria += "\t\t<tr>";
			umConcessionaria += "\t\t\t<td>&nbsp;Marca: <input class=\"input--register\" type=\"text\" name=\"marca\" value=\""+ descricao +"\"></td>";
			umConcessionaria += "\t\t\t<td>Ano: <input class=\"input--register\" type=\"text\" name=\"ano\" value=\""+ veiculo.getAno() +"\"></td>";
			umConcessionaria += "\t\t\t<td>Cor: <input class=\"input--register\" type=\"text\" name=\"cor\" value=\""+ veiculo.getCor() +"\"></td>";
			umConcessionaria += "\t\t</tr>";
			umConcessionaria += "\t\t<tr>";
			//umConcessionaria += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\""+ veiculo.getDataFabricacao().toString() + "\"></td>";
			//umConcessionaria += "\t\t\t<td>Data de validade: <input class=\"input--register\" type=\"text\" name=\"dataValidade\" value=\""+ veiculo.getDataValidade().toString() + "\"></td>";
			umConcessionaria += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\""+ buttonLabel +"\" class=\"input--main__style input--button\"></td>";
			umConcessionaria += "\t\t</tr>";
			umConcessionaria += "\t</table>";
			umConcessionaria += "\t</form>";		
		} else if (tipo == FORM_DETAIL){
			umConcessionaria += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
			umConcessionaria += "\t\t<tr>";
			umConcessionaria += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Concessionaria (ID " + veiculo.getId() + ")</b></font></td>";
			umConcessionaria += "\t\t</tr>";
			umConcessionaria += "\t\t<tr>";
			umConcessionaria += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
			umConcessionaria += "\t\t</tr>";
			umConcessionaria += "\t\t<tr>";
			umConcessionaria += "\t\t\t<td>&nbsp;Marca: "+ veiculo.getMarca() +"</td>";
			umConcessionaria += "\t\t\t<td>Ano: "+ veiculo.getAno() +"</td>";
			umConcessionaria += "\t\t\t<td>Cor: "+ veiculo.getCor() +"</td>";
			umConcessionaria += "\t\t</tr>";
			umConcessionaria += "\t\t<tr>";
			//umConcessionaria += "\t\t\t<td>&nbsp;Data de fabricação: "+ veiculo.getDataFabricacao().toString() + "</td>";
			//umConcessionaria += "\t\t\t<td>Data de validade: "+ veiculo.getDataValidade().toString() + "</td>";
			umConcessionaria += "\t\t\t<td>&nbsp;</td>";
			umConcessionaria += "\t\t</tr>";
			umConcessionaria += "\t</table>";		
		} else {
			System.out.println("ERRO! Tipo não identificado " + tipo);
		}
		form = form.replaceFirst("<UM-PRODUTO>", umConcessionaria);
		
		String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
		list += "\n<tr><td colspan=\"7\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Veiculos</b></font></td></tr>\n" +
				"\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
    			"\n<tr>\n" + 
        		"\t<td><a href=\"/concessionaria/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
        		"\t<td><a href=\"/concessionaria/list/" + FORM_ORDERBY_MARCA + "\"><b>Marca</b></a></td>\n" +
        		"\t<td><a href=\"/concessionaria/list/" + FORM_ORDERBY_ANO + "\"><b>Ano</b></a></td>\n" +
        		"\t<td><b>Cor</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
        		"\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
        		"</tr>\n";
		
		List<Concessionaria> veiculos;
		if (orderBy == FORM_ORDERBY_ID) {                 	veiculos = concessionariaDAO.getOrderByID();
		} else if (orderBy == FORM_ORDERBY_MARCA) {		veiculos = concessionariaDAO.getOrderByMarca();
		} else if (orderBy == FORM_ORDERBY_ANO) {			veiculos = concessionariaDAO.getOrderByAno();
		} else {											veiculos = concessionariaDAO.get();
		}

		int i = 0;
		String bgcolor = "";
		for (Concessionaria p : veiculos) {
			bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
			list += "\n<tr bgcolor=\""+ bgcolor +"\">\n" + 
            		  "\t<td>" + p.getId() + "</td>\n" +
            		  "\t<td>" + p.getMarca() + "</td>\n" +
            		  "\t<td>" + p.getAno() + "</td>\n" +
            		  "\t<td>" + p.getCor() + "</td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/concessionaria/" + p.getId() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"/concessionaria/update/" + p.getId() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteConcessionaria('" + p.getId() + "', '" + p.getMarca() + "', '" + p.getAno() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
            		  "</tr>\n";
		}
		list += "</table>";		
		form = form.replaceFirst("<LISTAR-PRODUTO>", list);				
	}
	
	
	public Object insert(Request request, Response response) {
		String marca = request.queryParams("marca");
		int ano = Integer.parseInt(request.queryParams("ano"));
		String cor = request.queryParams("cor");
		
		String resp = "";
		
		Concessionaria veiculo = new Concessionaria(-1, marca, ano, cor);
		
		if(concessionariaDAO.inserirVeiculo(veiculo) == true) {
            resp = "Concessionaria (" + marca + ") inserido!";
            response.status(201); // 201 Created
		} else {
			resp = "Concessionaria (" + marca + ") não inserido!";
			response.status(404); // 404 Not found
		}
			
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Concessionaria veiculo = (Concessionaria) concessionariaDAO.getVeiculoId(id);
		
		if (veiculo != null) {
			response.status(200); // success
			makeForm(FORM_DETAIL, veiculo, FORM_ORDERBY_MARCA);
        } else {
            response.status(404); // 404 Not found
            String resp = "Veiculo " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}

	
	public Object getToUpdate(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));		
		Concessionaria veiculo = (Concessionaria) concessionariaDAO.getVeiculoId(id);
		
		if (veiculo != null) {
			response.status(200); // success
			makeForm(FORM_UPDATE, veiculo, FORM_ORDERBY_MARCA);
        } else {
            response.status(404); // 404 Not found
            String resp = "Veiculo " + id + " não encontrado.";
    		makeForm();
    		form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");     
        }

		return form;
	}
	
	
	public Object getAll(Request request, Response response) {
		int orderBy = Integer.parseInt(request.params(":orderby"));
		makeForm(orderBy);
	    response.header("Content-Type", "text/html");
	    response.header("Content-Encoding", "UTF-8");
		return form;
	}			
	
	public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
		Concessionaria veiculo = concessionariaDAO.getVeiculoId(id);
        String resp = "";       

        if (veiculo != null) {
        	veiculo.setMarca(request.queryParams("marca"));
        	veiculo.setAno(Integer.parseInt(request.queryParams("ano")));
        	veiculo.setCor(request.queryParams("cor"));
        	concessionariaDAO.atualizarVeiculo(veiculo);
        	response.status(200); // success
            resp = "Veiculo (ID " + veiculo.getId() + ") atualizado!";
        } else {
            response.status(404); // 404 Not found
            resp = "Veiculo (ID \" + veiculo.getId() + \") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}

	
	public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Concessionaria veiculo = concessionariaDAO.getVeiculoId(id);
        String resp = "";       

        if (veiculo != null) {
            concessionariaDAO.excluirVeiculo(id);
            response.status(200); // success
            resp = "Veiculo (" + id + ") excluído!";
        } else {
            response.status(404); // 404 Not found
            resp = "Veiculo (" + id + ") não encontrado!";
        }
		makeForm();
		return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\""+ resp +"\">");
	}
}