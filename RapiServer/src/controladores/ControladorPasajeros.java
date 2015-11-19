/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import utiles.Constantes;
import utiles.GoogleGeoCodeHelper;
import entidades.Favorito;
import entidades.PuntoGeografico;
import entidades.usuarios.Chofer;
import entidades.usuarios.Pasajero;

/**
 * 
 * @author fcarou
 */
public class ControladorPasajeros extends BaseControlador
{
	public ControladorPasajeros()
	{
		super();
	}

	public ControladorPasajeros(EntityManager em)
	{
		super(em);
	}

	public long iniciarSesion(String email, String clave, String gcm)
	{
		Pasajero pasajero = buscarPasajero(email, clave);

		if (pasajero == null)
			return -1;

		pasajero.setGcm(gcm);

		if (edit(pasajero))
			return pasajero.getId();

		return -1;
	}

	public boolean cerrarSesion(long id)
	{
		Pasajero pasajero = (Pasajero) new ControladorGenerico().buscarPorID(
				Pasajero.class, id);

		if (pasajero == null)
			return false;

		pasajero.setGcm(null);

		return edit(pasajero);
	}
	
	public Pasajero checkDatos (String email, String clave)
	{
		Pasajero pasajero = buscarPasajero(email, clave);

		if (pasajero == null)
			return null;

		return pasajero;
	}

	public Pasajero buscarPasajero(String email, String clave)
	{
		Query query = em.createNamedQuery("PasajeroPorEmailClave");

		query.setParameter(1, email);
		query.setParameter(2, clave);

		try
		{
			return (Pasajero) query.getResultList().get(0);
		} catch (Exception e)
		{
			return null;
		}
	}

	public Pasajero buscarPasajero(long id, String clave)
	{
		Query query = em.createNamedQuery("PasajeroPorClave");
		query.setParameter(1, id);
		query.setParameter(2, clave);

		try
		{
			return (Pasajero) query.getResultList().get(0);
		} catch (Exception ex)
		{
			return null;
		}
	}

	/**
	 * Carga el codigo GCM a un pasajero.
	 * 
	 * @param idChofer
	 *            la id del pasajero a asignar el codigo.
	 * @param codigoGCM
	 *            el codigo GCM del pasajero.
	 * @return true si el pasajero existe y se pudo completar la operacion.
	 */
	public boolean cargarCodigoGCM(long idPasajero, String codigoGCM)
	{
		Pasajero pasajero = (Pasajero) new ControladorGenerico().buscarPorID(
				Pasajero.class, idPasajero);

		if (pasajero == null)
			return false;

		pasajero.setGcm(codigoGCM);

		return edit(pasajero);
	}

	/**
	 * Busca un pasajero por email.
	 * 
	 * @param email
	 *            el email del pasajero a buscar.
	 * @return el pasajero con ese email, si lo hay.
	 */
	public Pasajero buscarPorEmail(String email)
	{
		if (email == null || email.equals(""))
			return null;

		Query query = em.createNamedQuery("PasajeroPorEmail");
		query.setParameter(1, email);

		try
		{
			return (Pasajero) query.getResultList().get(0);
		} catch (IndexOutOfBoundsException ex)
		{
			return null;
		}
	}

	/**
	 * Registra un pasajero en la DB.
	 * 
	 * @param pasajero
	 *            el pasajero a registrar.
	 * @return el codigo resultado.
	 */
	public int registrarPasajero(Pasajero pasajero)
	{
		if (pasajero == null)
			return Constantes.Registro.FALTAN_DATOS;

		if (pasajero.getEmail() == null || pasajero.getEmail().equals(""))
			return Constantes.Registro.FALTAN_DATOS;

		// Verifico que el email no haya sido registrado.
		if (buscarPorEmail(pasajero.getEmail()) != null)
			return Constantes.Registro.EMAIL_EXISTENTE;

		if (create(pasajero))
			return Constantes.Registro.OK;

		return Constantes.Registro.FALLO_PERSISTENCIA;
	}

	/**
	 * Registra un pasajero en el sistema a partir de los datos de un chofer
	 * existente. Si ya existe otro pasajero con el mismo email, la operacion se
	 * aborta.
	 * @param email el email del chofer.
	 * @param clave la clave del chofer.
	 * @return el codigo de registro.
	 */
	public int choferAPasajero(String email, String clave)
	{
		Chofer chofer = new ControladorChoferes().checkDatos(email, clave);

		return choferAPasajero(chofer);
	}

	/**
	 * Registra un pasajero en el sistema a partir de los datos de un chofer
	 * existente. Si ya existe otro pasajero con el mismo email, la operacion se
	 * aborta.
	 * 
	 * @param chofer
	 *            el chofer del cual sacar los datos.
	 * @return el pasajero, si la operacion se pudo completar.
	 */
	public int choferAPasajero(Chofer chofer)
	{
		if (chofer == null)
			return Constantes.Registro.NULL;

		// Verifico que no haya un pasajero con el mismo email que el chofer.
		if (buscarPorEmail(chofer.getEmail()) != null)
			return Constantes.Registro.EMAIL_EXISTENTE;

		// Obtengo el pasajero a partir de los datos del chofer.
		Pasajero pasajero = chofer.transformar();

		if (create(pasajero))
			return Constantes.Registro.OK;
		else
			return Constantes.Registro.FALLO_PERSISTENCIA;
	}

	/**
	 * trae las dirrecciones favoritas del pasjero
	 * 
	 * @param idPasajero
	 * @return null si no encuentra pasajero
	 */
	public List<Favorito> direccinesFavorritas(long idPasajero)
	{
		Pasajero pasajero = (Pasajero) new ControladorGenerico().buscarPorID(
				Pasajero.class, idPasajero);
		if (pasajero != null)
			return pasajero.getFavoritos();
		return null;
	}

	/**
	 * agregar un punto favorito
	 * 
	 * @param idPasajero
	 * @param latitud
	 * @param longitud
	 * @param detalle
	 * @return
	 */
	public boolean agregarFavorito(long idPasajero, Favorito favorito)
	{
		PuntoGeografico punto = favorito.getPunto();
		Pasajero pasajero = (Pasajero) new ControladorGenerico().buscarPorID(
				Pasajero.class, idPasajero);
		if (punto.getDireccion() == null || punto.getDireccion().equals(""))
		{
			punto.setDireccion(new GoogleGeoCodeHelper().getDireccion(punto));
		} else
		{
			favorito.setPunto(new GoogleGeoCodeHelper()
					.getPuntoGeografico(punto.getDireccion()));
		}

		if (pasajero != null)
		{
			pasajero.agregarFavorito(favorito);
			return edit(pasajero);
		}
		return false;
	}
}
