package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import controladores.ControladorMoviles;
import entidades.Movil;

import javax.ws.rs.Path;

@Path("movil")
public class RestMoviles
{
	@GET
	@Path("movilesChofer")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Movil> getMovilesChofer (@QueryParam ("idChofer") long idChofer)
	{
		return new ControladorMoviles().getMovilesChofer(idChofer);
	}
	
	@POST
	@Path("registrarMovil")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean registrarMovil (@QueryParam("idChofer") long idChofer, Movil movil)
	{
		return new ControladorMoviles().agregarMovil(idChofer, movil);
	}
}
