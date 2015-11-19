/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entidades.Caracteristica;
import entidades.Movil;
import entidades.Turno;
import entidades.Viaje;
import entidades.estados.Estados.EstadoChofer;
import entidades.usuarios.Chofer;
import entidades.usuarios.Pasajero;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import utiles.Constantes;

/**
 *
 * @author fcarou
 */
public class ControladorChoferes extends BaseControlador
{
    public ControladorChoferes ()
    {
        super();
    }
    
    public ControladorChoferes (EntityManager em)
    {
        super(em);
    }
    
    public Chofer buscarChofer (long id, String clave)
    {
        Query query = em.createNamedQuery("choferPorClave");
        
        query.setParameter(1, id);
        query.setParameter(2, clave);
        
        try
        {
            return (Chofer) query.getResultList().get(0);
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Carga el codigo GCM a un chofer.
     * @param idChofer la id del chofer a asignar el codigo.
     * @param codigoGCM el codigo GCM del chofer.
     * @return true si el chofer existe y se pudo completar la operacion.
     */
    public boolean cargarCodigoGCM (long idChofer, String codigoGCM)
    {
    	Chofer chofer = (Chofer) new ControladorGenerico().buscarPorID(Chofer.class, idChofer);
    	
    	if (chofer == null)
    		return false;
    	
    	chofer.setGcm(codigoGCM);
    	
    	return edit(chofer);
    }
    
    /**
     * Busca un chofer por email.
     * @param email el email del chofer a buscar.
     * @return el chofer con ese email, si lo hay.
     */
    public Chofer buscarPorEmail (String email)
    {
    	if (email == null || email.equals(""))
    		return null;
    	
    	Query query = em.createNamedQuery("choferPorEmail");
    	query.setParameter(1, email);
    	
    	try
    	{
    		return (Chofer) query.getResultList().get(0);
    	}
    	catch (IndexOutOfBoundsException ex)
    	{
    		return null;
    	}
    }
    
    /**
     * Actualiza el estado de un chofer.
     * @param idChofer la id del chofer a actualizar estado.
     * @param estado el nuevo estado.
     * @return true si se pudo realizar.
     */
    public boolean actualizarEstado (long idChofer, EstadoChofer estado)
    {
    	Turno turno = new ControladorTurnos().getTurnoAbierto(idChofer);
		
		if (turno == null)
			return false;
		
		Viaje viaje = new ControladorViajes().getViajeActivoChofer(idChofer);
		
		if (viaje != null)
			return false;
		
		Chofer chofer = turno.getChofer();
		
		chofer.setEstado(estado);
		
		return edit(chofer);
    }
    
    /**
     * Obtiene una lista de choferes que estan de turno y utilizando
     * un movil con las caracteristicas indicadas.
     * @param caracteristicas las caracteristicas que deben tener los moviles.
     * @return la lista de choferes, si los hay.
     */
    public List<Chofer> choferesMovilCaracteristica (List<Caracteristica> caracteristicas)
    {
    	List<Turno> turnos = new ControladorTurnos().getTurnosAbiertos();
    	
    	removerOcupados(turnos);
    	removerTurnosMovil(turnos, caracteristicas);
    	
    	return turnosAChofer(turnos);
    }
    
    private void removerOcupados (List<Turno> turnos)
    {
    	Turno turno;
    	Iterator<Turno> iterator = turnos.iterator();
    	
    	while (iterator.hasNext())
    	{
    		turno = iterator.next();
    		
    		if (!turno.getChofer().getEstado().equals(EstadoChofer.DISPONIBLE))
    			iterator.remove();
    	}
    }
    
    public void removerTurnosMovil (List<Turno> turnos, List<Caracteristica> caracteristicas)
    {
    	Turno turno;
    	Iterator<Turno> iterator = turnos.iterator();
    	
    	while (iterator.hasNext())
    	{
    		turno = iterator.next();
    		
    		if (!turno.getMovil().validarCaracteristicas(caracteristicas))
    			iterator.remove();
    	}
    }
    
    /**
     * Registra un chofer en la DB.
     * @param chofer el chofer a registrar.
     * @return codigo resultado.
     */
    public int registrarChofer (Chofer chofer)
    {
    	if (chofer == null)
    		return Constantes.Registro.NULL;
    	
    	if (chofer.getEmail() == null || chofer.getEmail().equals(""))
    		return Constantes.Registro.FALTAN_DATOS;
    	
    	// Verifico que el email no haya sido registrado.
    	if (buscarPorEmail(chofer.getEmail()) != null)
    		return Constantes.Registro.EMAIL_EXISTENTE;
    	
    	if (create(chofer))
    		return Constantes.Registro.OK;
    	else
    		return Constantes.Registro.FALLO_PERSISTENCIA;
    }
    
    /**
     * Busca un chofer por email y clave.
     * @param email el email del chofer.
     * @param clave la clave, debe estar encriptada.
     * @return el chofer, si lo hay.
     */
    public Chofer choferPorEmailClave (String email, String clave)
    {
    	Query query = em.createNamedQuery("choferPorEmailClave");
    	query.setParameter(1, email);
    	query.setParameter(2, clave);
    	
    	try
    	{
    		return (Chofer) query.getResultList().get(0);
    	}
    	catch (Exception ex)
    	{
    		return null;
    	}
    }
  
    public List<Movil> getMovilesChofer (long idChofer)
    {
    	Chofer chofer = (Chofer) new ControladorGenerico().buscarPorID(Chofer.class, idChofer);
    	
    	if (chofer == null)
    		return null;
    	
    	return chofer.getMoviles();
    }
    
    /**
     * Registra un chofer en el sistema a partir de los datos de un pasaero ya existente. Si ya existe un chofer con el mismo email que el pasajero origen,
     * la operacion se aborta.
     * @param idPasajero la id del pasajero del cual sacar los datos.
     * @param moviles la lista de moviles a asociar al nuevo chofer.
     * @return el chofer, si la operacion se pudo completar.
     */
    public int pasajeroAChofer (long idPasajero, Movil movil)
    {
    	Pasajero pasajero = (Pasajero) new ControladorGenerico().buscarPorID(Pasajero.class, idPasajero);
    	
    	return pasajeroAChofer(pasajero, movil);
    }
    
    /**
     * Registra un chofer en el sistema a partir de los datos de un pasajero ya existente. Si ya existe un chofer con el mismo email que el pasajero origen,
     * la operacion se aborta.
     * @param pasajero el pasajero del cual sacar los datos.
     * @param moviles la lista de moviles a asociar al nuevo chofer.
     * @return el chofer, si la operacion se pudo completar.
     */
    public int pasajeroAChofer (Pasajero pasajero, Movil movil)
    {
    	if (pasajero == null)
    		return Constantes.Registro.NULL;
    	
    	// Verifico que no haya un chofer con el mismo email que el pasajero.
    	if (buscarPorEmail(pasajero.getEmail()) != null)
    		return Constantes.Registro.EMAIL_EXISTENTE;
    	
    	// Obtengo el chofer a partir de los datos del pasajero.
    	Chofer chofer = pasajero.transformar(movil);
    	
    	if (create(chofer))
    		return Constantes.Registro.OK;
    	else
    		return Constantes.Registro.FALLO_PERSISTENCIA;
    }
    
    public long iniciarSesion (String email, String clave, String gcm)
    {
    	Chofer chofer = choferPorEmailClave(email, clave);
    	
    	if (chofer == null)
    		return -1;
    	
    	chofer.setGcm(gcm);
    	
    	if (edit(chofer))
    		return chofer.getId();
    	
    	return -1;
    }
    
    public boolean cerrarSesion (long idChofer)
    {
    	Chofer chofer = (Chofer) new ControladorGenerico().buscarPorID(Chofer.class, idChofer);
    	
    	if (chofer == null)
    		return false;
    	
    	new ControladorTurnos().cerrarTurno(idChofer);
    	
    	chofer.setGcm(null);
    	
    	return edit(chofer);
    }
    
    public Chofer checkDatos (String email, String clave)
    {
    	Chofer chofer = choferPorEmailClave(email, clave);
    	
    	if (chofer == null)
    		return null;
    	
    	return chofer;
    }
    
    public List<Chofer> turnosAChofer (List<Turno> turnos)
    {
    	List<Chofer> choferes = new ArrayList<>();
    	
    	for (Turno turno : turnos)
    		choferes.add(turno.getChofer());
    	
    	return choferes;
    }
}
