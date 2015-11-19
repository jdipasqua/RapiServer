/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import admin.Admin;
import controladores.ControladorGenerico;
import controladores.ControladorPasajeros;
import controladores.ControladorViajes;
import entidades.Favorito;
import entidades.Viaje;
import entidades.nopersistence.SolicitudPasajero;
import entidades.usuarios.Chofer;
import entidades.usuarios.Pasajero;

/**
 * 
 * @author fcarou
 */
@Path("pasajero")
public class RestPasajero
{
	@SuppressWarnings("unchecked")
	@GET
	@Path("todos")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Pasajero> getPasajeros()
	{
		return new ControladorGenerico().buscarTodos(Pasajero.class);
	}

	@POST
	@Path("registrar")
	@Consumes(MediaType.APPLICATION_JSON)
	public int registrar(Pasajero pasajero)
	{
		return new ControladorPasajeros().registrarPasajero(pasajero);
	}
	
	@POST
	@Path("registrarFromChofer")
	@Consumes(MediaType.APPLICATION_JSON)
	public int registrar(Chofer chofer)
	{
		return new ControladorPasajeros().choferAPasajero(chofer.getEmail(), chofer.getClave());
	}

	@POST
	@Path("solicitar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Viaje solicitar(SolicitudPasajero solicitud)
	{
		Admin admin = Admin.getAdmin();
		
		return admin.solicitar(solicitud);
	}

	@POST
	@Path("cargarCodigo")
	public boolean cargarCodigo(@QueryParam("id") long id,
			@HeaderParam("codigo") String codigo)
	{
		ControladorGenerico con = new ControladorGenerico();

		Pasajero usuario = (Pasajero) con.buscarPorID(Pasajero.class, id);

		usuario.setGcm(codigo);
		return con.edit(usuario);
	}

	@GET
	@Path("cancelar")
	public boolean cancelarViaje(@QueryParam("id") long id)
	{
		return Admin.getAdmin().cancelarViajePasajero(id);
	}

	@GET
	@Path("finalizar")
	public boolean finalizarViaje(@QueryParam("id") long idViaje)
	{
		return Admin.getAdmin().finalizarViajePasajero(idViaje);
	}

	@GET
	@Path("calificaciones")
	@Produces(MediaType.APPLICATION_JSON)
	public int[] calificaciones(@QueryParam("id") long id)
	{
		return new ControladorViajes().calificaciones(id);
	}

	@POST
	@Path("iniciarSesion")
	@Consumes(MediaType.APPLICATION_JSON)
	public long iniciarSesion(Pasajero pasajero, @HeaderParam("gcm") String gcm)
	{
		String email = pasajero.getEmail();
		String clave = pasajero.getClave();

		if (email == null || clave == null || gcm == null)
			return -1;

		return new ControladorPasajeros().iniciarSesion(email, clave, gcm);
	}

    @GET
    @Path("cerrarSesion")
    public boolean cerrarSesion (@QueryParam("id") long id)
    {
    	return new ControladorPasajeros().cerrarSesion(id);
    }
    
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("favoritos")
	public List<Favorito> favoritos(@QueryParam("id") long idPasajero)
	{
		return new ControladorPasajeros().direccinesFavorritas(idPasajero);
	}

	@POST
	@Path("agregarFavorito")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean agregarFavorito(@QueryParam("id") long idPasajero,Favorito favorito)
	{
		return new ControladorPasajeros().agregarFavorito(idPasajero,favorito);
	}
	
	@POST
	@Path("checkDatos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Pasajero checkDatos (Pasajero pasajero)
	{
		return new ControladorPasajeros().checkDatos(pasajero.getEmail(), pasajero.getClave());
	}
}
