package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controladores.ControladorCaracteristicas;
import controladores.ControladorGenerico;
import entidades.Caracteristica;

@Path("caracteristicas")
public class RestCaracteristicas
{
	@SuppressWarnings("unchecked")
	@GET
	@Path("todas")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Caracteristica> getCaracteristicas ()
	{
		return new ControladorGenerico().buscarTodos(Caracteristica.class);
	}
	
	@POST
	@Path("nueva")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean agregarCaracteristica (Caracteristica caracteristica)
	{
		return new ControladorCaracteristicas().agregar(caracteristica);
	}
}
