/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rest;

import admin.Admin;
import admin.MiServlet;
import controladores.ControladorChoferes;
import controladores.ControladorGenerico;
import controladores.ControladorViajes;
import entidades.Movil;
import entidades.PuntoGeografico;
import entidades.estados.Estados.EstadoChofer;
import entidades.usuarios.Chofer;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import utiles.GoogleGeoCodeHelper;

/**
 *
 * @author fcarou
 */
@Path ("chofer")
public class RestChofer
{
    @SuppressWarnings("unchecked")
	@GET
    @Path ("todos")
    @Produces (MediaType.APPLICATION_JSON)
    public List<Chofer> getChoferes ()
    {
        return new ControladorGenerico().buscarTodos(Chofer.class);
    }
    
    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    public int registrar (Chofer chofer)
    {
    	return new ControladorChoferes().registrarChofer(chofer);
    }
    
    @POST
    @Path("registrarFromPasajero")
    @Consumes(MediaType.APPLICATION_JSON)
    public int registrar (@QueryParam("idPasajero") long idPasajero, Movil movil)
    {
    	return new ControladorChoferes().pasajeroAChofer(idPasajero, movil);
    }
    
    @GET
    @Path ("calle")
    public String getDireccion (@QueryParam("latitud") double latitud, @QueryParam ("longitud") double longitud)
    {
        String calle = new GoogleGeoCodeHelper().getDireccion(new PuntoGeografico(latitud, longitud));
        
        System.out.println("La calle es: " + calle);
        
        return calle;
    }
    
    @GET
    @Path ("punto")
    @Produces (MediaType.APPLICATION_JSON)
    public PuntoGeografico getPunto (@QueryParam("direccion") String direccion)
    {
    	return new GoogleGeoCodeHelper().getPuntoGeografico(direccion);
    }
    
    @GET
    @Path ("distancia")
    public int getDistancia ()
    {
    	return new GoogleGeoCodeHelper().getDistancia(new PuntoGeografico(-42.7848922, -65.0173401), new PuntoGeografico(-42.7773112, -65.0262742));
    }
        
    @GET
    @Path("instanciar")
    public String instanciar ()
    {
    	Admin.getAdmin();
    	return "Hola!";
    }
    
    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    @Path ("actualizar_punto")
    public boolean actualizarPunto (@QueryParam("id") long id, PuntoGeografico punto)
    {
    	ControladorGenerico con = new ControladorGenerico();
    	
    	Chofer chofer = (Chofer) con.buscarPorID(Chofer.class, id);
    	
    	if (chofer == null)
    		return false;
    	
    	if (punto.getDireccion() == null)
    		punto.setDireccion(new GoogleGeoCodeHelper().getDireccion(punto));
    	
    	chofer.setUbicacion(punto);
    	
    	return con.edit(chofer);
    }
    
    /**
     * Actualiza el codigo GCM de un chofer.
     * @param id la id del chofer.
     * @param codigo el nuevo codigo.
     * @return true si se pudo completar la operacion.
     */
    @POST
    @Path ("cargarCodigo")
    public boolean cargarCodigo (@QueryParam ("id") long id, @HeaderParam ("codigo") String codigo)
    {
        return new ControladorChoferes().cargarCodigoGCM(id, codigo);
    }
    
    /**
     * Cambia el estado del chofer a ocupado.
     * @param id la id del chofer.
     * @return true si se pudo completar la operacion.
     */
    @GET
    @Path ("ocupado")
    public boolean choferOcupado (@QueryParam ("id") long id)
    {
    	return new ControladorChoferes().actualizarEstado(id, EstadoChofer.OCUPADO);
    }
    
    /**
     * Cambia el estado del chofer a disponible.
     * @param id la id del chofer.
     * @return true si se pudo completar la operacion.
     */
    @GET
    @Path ("disponible")
    public boolean choferDisponible (@QueryParam ("id") long id)
    {
    	return new ControladorChoferes().actualizarEstado(id, EstadoChofer.DISPONIBLE);
    }
    
    /**
     * Cancela el viaje activo asociado al chofer.
     * @param idViaje la id del viaje. 
     * @return true si se pudo realizar la operacion.
     */
    @GET
    @Path ("cancelar")
    public boolean cancelarViaje (@QueryParam ("id") long idViaje)
    {
    	return Admin.getAdmin().cancelarViajeChofer(idViaje);
    }
    
    /**
     * Finaliza el viaje activo asociado al chofer.
     * @param idViaje la id del viaje.
     * @return true si se pudo realizar la operacion.
     */
    @GET
    @Path ("finalizar")
    public boolean finalizarViaje (@QueryParam ("id") long idViaje,@QueryParam ("calificacion") int calificacion)
    {
    	Boolean calificar = new ControladorViajes().calificar(idViaje, calificacion,"nada");
    	Boolean finalizar = Admin.getAdmin().finalizarViajeChofer(idViaje);
    	if(calificar && finalizar)
    		return true;
    	else 
    		return false;
    }
    
    @POST
    @Path("iniciarSesion")
    @Consumes (MediaType.APPLICATION_JSON)
    public long iniciarSesion (Chofer chofer)
    {
    	return new ControladorChoferes().iniciarSesion(chofer.getEmail(), chofer.getClave(), chofer.getGcm());
    }
    
    @GET
    @Path("cerrarSesion")
    public boolean cerrarSesion (@QueryParam("id") long idChofer)
    {
    	return new ControladorChoferes().cerrarSesion(idChofer);
    }
    
    @POST
	@Path("checkDatos")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Chofer checkDatos (Chofer chofer)
	{
		return new ControladorChoferes().checkDatos(chofer.getEmail(), chofer.getClave());
	}
}
