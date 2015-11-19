package rest;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import admin.Admin;
import admin.MiServlet;
import controladores.ControladorViajes;
import entidades.Viaje;

@Path ("viaje")
public class RestViaje
{
	@GET
    @Path("iniciar")
    public boolean iniciarViaje (@QueryParam("id") long id)
    {
    	return Admin.getAdmin().iniciarViaje(id);
    }
	
	@GET
	@Path("finalizarPasajero")
	public boolean finalizarViajePasajero (@QueryParam("id") long id)
	{
		return Admin.getAdmin().finalizarViajePasajero(id);
	}
	
	@GET
	@Path("finalizarChofer")
	public boolean finalizarViajeChofer (@QueryParam("id") long id)
	{
		return Admin.getAdmin().finalizarViajeChofer(id);
	}
	
	@GET
	@Path("cancelar")
	public boolean cancelar (@QueryParam("id_usuario") long idUsuario, @QueryParam("id_viaje") long idViaje)
	{
		// Avisar al otro usuario que el viaje fue cancelado.
		
		return true;
	}
	
	@GET
	@Path("aceptar")
	@Produces (MediaType.APPLICATION_JSON)
	public Viaje aceptarViaje (@QueryParam("id_chofer") long idChofer, @QueryParam("id_viaje") long idViaje)
	{
		return Admin.getAdmin().aceptarViaje(idChofer, idViaje);
	}
	
	@GET
	@Path("rechazar")
	public void rechazarViaje (@QueryParam("idChofer") long idChofer, @QueryParam("idViaje") long idViaje)
	{
		MiServlet.getServlet().rechazar(idChofer, idViaje);
	}
	
	@GET
	@Path ("viajeChofer")
	@Produces (MediaType.APPLICATION_JSON)
	public Viaje getViajeChofer (@QueryParam("id") long idChofer)
	{
		return new ControladorViajes().getViajeActivoChofer(idChofer);
	}
	
	@GET
	@Path ("viajePasajero")
	@Produces (MediaType.APPLICATION_JSON)
	public Viaje getViajePasajero (@QueryParam("id") long idPasajero)
	{
		return new ControladorViajes().getViajeActivoPasajero(idPasajero);
	}
	
	@GET
	@Path ("tiempoEstimado")
	public String getTiempoEstimado (@QueryParam ("id") long idViaje)
	{
		return new ControladorViajes().getTiempoEstimado(idViaje);
	}
	
	@GET
	@Path ("calificar")
	@Produces (MediaType.APPLICATION_JSON)
	public Boolean calificar (@QueryParam ("id") long idViaje,@QueryParam ("calificacion") int calificacion,@QueryParam ("detalle")String detalle)
	{
		return new ControladorViajes().calificar(idViaje, calificacion, detalle);
	}	
}