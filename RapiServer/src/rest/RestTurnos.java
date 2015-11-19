package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import controladores.ControladorChoferes;
import controladores.ControladorTurnos;
import entidades.Turno;
import entidades.estados.Estados.EstadoChofer;
import entidades.usuarios.Chofer;

@Path ("turnos")
public class RestTurnos
{
	@POST
	@Produces (MediaType.APPLICATION_JSON)
	@Consumes (MediaType.APPLICATION_JSON)
    @Path ("iniciarTurno")
    public Turno iniciarTurno (Chofer chofer)
    {
    	return new ControladorTurnos().iniciarTurno(chofer);
    }
    
    @GET
    @Path ("cerrarTurno")
    public boolean cerrarTurno (@QueryParam ("id") long idChofer)
    {
    	return new ControladorTurnos().cerrarTurno(idChofer);
    }
    
    @GET
    @Produces (MediaType.APPLICATION_JSON)
    @Path ("turnoChofer")
    public Turno turnoChofer (@QueryParam ("id") long id)
    {
    	return new ControladorTurnos().getTurnoAbierto(id);
    }
    
    @GET
    @Path("asignarMovil")
    public boolean asignarMovil (@QueryParam("id") long idChofer, @QueryParam("patente") String patente, @QueryParam("disponible") String disponible)
    {
    	boolean turno = new ControladorTurnos().asignarMovil(idChofer, patente);
    	
    	if (turno && Boolean.valueOf(disponible))
    		return new ControladorChoferes().actualizarEstado(idChofer, EstadoChofer.DISPONIBLE);
    	
    	return turno;
    }
}
