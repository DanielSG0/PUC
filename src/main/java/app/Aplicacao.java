package app;

import static spark.Spark.*;
import service.ConcessionariaService;


public class Aplicacao {
	
	private static ConcessionariaService concessionariaService = new ConcessionariaService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/concessionaria/insert", (request, response) -> concessionariaService.insert(request, response));

        get("/concessionaria/:id", (request, response) -> concessionariaService.get(request, response));
        
        get("/concessionaria/list/:orderby", (request, response) -> concessionariaService.getAll(request, response));

        get("/concessionaria/update/:id", (request, response) -> concessionariaService.getToUpdate(request, response));
        
        post("/concessionaria/update/:id", (request, response) -> concessionariaService.update(request, response));
           
        get("/concessionaria/delete/:id", (request, response) -> concessionariaService.delete(request, response));     
    }
}