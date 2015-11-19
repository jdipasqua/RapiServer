package controladores;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import utiles.Constantes;
import utiles.GoogleGeoCodeHelper;
import entidades.Calificacion;
import entidades.PuntoGeografico;
import entidades.Turno;
import entidades.Viaje;
import entidades.estados.Estados.EstadoChofer;
import entidades.estados.Estados.EstadoViaje;
import entidades.usuarios.Chofer;
import entidades.usuarios.Pasajero;

public class ControladorViajes extends BaseControlador
{
	public ControladorViajes()
	{
		super();
	}

	public ControladorViajes(EntityManager em)
	{
		super(em);
	}

	/**
	 * Solicita un viaje para un pasajero, unicamente con direccion origen.
	 * 
	 * @param idPasajero
	 *            el id del pasajero solicitante.
	 * @param direccion
	 *            la direccion donde sera recogido.
	 * @return el viaje si se pudo instanciar, null caso contrario.
	 */
	public Viaje solicitar(long idPasajero, String direccion)
	{
		return solicitar(idPasajero, null,
				new GoogleGeoCodeHelper().getPuntoGeografico(direccion));
	}

	/**
	 * Solicita un viaje para un pasajero, con origen y destino.
	 * 
	 * @param idPasajero
	 *            la id del pasajero solicitante.
	 * @param puntos
	 *            los puntos de inicio y fin.
	 * @return el viaje si se pudo instanciar, null caso contrario.
	 */
	public Viaje solicitar(long idPasajero, String comentario, PuntoGeografico... puntos)
	{
		Pasajero p = (Pasajero) new ControladorGenerico().buscarPorID(
				Pasajero.class, idPasajero);

		if (p == null)
			return null;

		Viaje viaje = new Viaje(p, comentario);
		viaje.setPuntos(new ArrayList<PuntoGeografico>());

		for (int i = 0; i < puntos.length; i++)
			viaje.getPuntos().add(puntos[i]);
			
		viaje.setEstado(EstadoViaje.SIN_CHOFER);

		if (super.create(viaje))
			return viaje;
		else
			return null;
	}

	/**
	 * Actualiza el estado de un viaje.
	 * 
	 * @param idViaje
	 *            el id del viaje.
	 * @param nuevo
	 *            el nuevo estado.
	 * @return true si la operacion se pudo realizar.
	 */
	public boolean actualizarEstado(long idViaje, EstadoViaje nuevo)
	{
		ControladorGenerico con = new ControladorGenerico();

		Viaje viaje = (Viaje) con.buscarPorID(Viaje.class, idViaje);

		if (viaje == null)
			return false;

		viaje.setEstado(nuevo);
		return edit(viaje);
	}

	/**
	 * Inicia un viaje asignado.
	 * 
	 * @param idViaje
	 *            la id del viaje a iniciar.
	 * @return el viaje iniciado, si existia y estaba asignado.
	 */
	public Viaje iniciarViaje(long idViaje)
	{
		ControladorGenerico con = new ControladorGenerico();

		Viaje viaje = (Viaje) con.buscarPorID(Viaje.class, idViaje);

		if (viaje == null)
			return viaje;

		viaje.setEstado(EstadoViaje.INICIADO);
		viaje.setInicio(Calendar.getInstance());

		if (edit(viaje))
			return viaje;

		return null;
	}

	/**
	 * Finaliza un viaje iniciado.
	 * 
	 * @param id
	 *            la id del viaje.
	 * @return el viaje finalizado, si existia y estaba iniciado.
	 */
	public Viaje finalizarViaje(long id)
	{
		ControladorGenerico con = new ControladorGenerico();

		Viaje viaje = (Viaje) con.buscarPorID(Viaje.class, id);

		if (viaje == null || !viaje.getEstado().equals(EstadoViaje.INICIADO))
			return null;

		viaje.setEstado(EstadoViaje.FINALIZADO);
		viaje.setFin(Calendar.getInstance());
		
		Chofer chofer = viaje.getTurno().getChofer();
		chofer.setEstado(EstadoChofer.DISPONIBLE);
		edit(chofer);

		if (con.edit(viaje))
			return viaje;

		return null;
	}

	/**
	 * Cancela un viaje. Para que un viaje sea cancelable, el estado del mismo
	 * debe ser sin chofer o asignado.
	 * 
	 * @param id
	 *            la id del viaje.
	 * @return true si se pudo cancelar, false si no.
	 */
	public boolean cancelarViaje(long id)
	{
		ControladorGenerico con = new ControladorGenerico();

		Viaje viaje = (Viaje) con.buscarPorID(Viaje.class, id);

		if (viaje == null || viaje.getEstado().equals(EstadoViaje.CANCELADO)
				|| viaje.getEstado().equals(EstadoViaje.FINALIZADO)
				|| viaje.getEstado().equals(EstadoViaje.INICIADO))
			return false;

		if (viaje.getEstado().equals(EstadoViaje.ASIGNADO))
		{
			Chofer chofer = viaje.getTurno().getChofer();
			chofer.setEstado(EstadoChofer.DISPONIBLE);
			
			edit (chofer);
		}
		
		viaje.setEstado(EstadoViaje.CANCELADO);

		return con.edit(viaje);
	}
	
	public boolean forzarFinalizacion (long idChofer)
	{
		Viaje viaje = getViajeActivoChofer(idChofer);
		
		if (viaje == null)
			return false;
		
		switch (viaje.getEstado())
		{
		case ASIGNADO:
			return cancelarViaje(viaje.getId());
		case INICIADO:
			return finalizarViaje(viaje.getId()) != null;
		default:
			break;
		}
		
		return false;
	}

	/**
	 * Acepta un viaje. Un viaje solo es aceptable si su estado es sin chofer.
	 * 
	 * @param idChofer
	 *            la id del chofer que acepta.
	 * @param idViaje
	 *            la id del viaje aceptado.
	 * @return el viaje aceptado, si la operacion se pudo completar.
	 */
	public Viaje aceptarViaje(long idChofer, long idViaje)
	{
		ControladorGenerico con = new ControladorGenerico();

		Viaje viaje = (Viaje) con.buscarPorID(Viaje.class, idViaje);
		Turno turno = new ControladorTurnos().getTurnoAbierto(idChofer);

		if (viaje == null || viaje.getEstado() != EstadoViaje.SIN_CHOFER
				|| turno == null)
			return null;

		viaje.setEstado(EstadoViaje.ASIGNADO);
		viaje.setTurno(turno);
		turno.getChofer().setEstado(EstadoChofer.OCUPADO);

		if (con.edit(turno.getChofer()))
			con.edit(viaje);

		return viaje;
	}

	/**
	 * Obtiene la lista de viajes asociados a un chofer.
	 * 
	 * @param idChofer
	 *            la id del chofer.
	 * @return la lista de los viajes, si los hay.
	 */
	@SuppressWarnings("unchecked")
	public List<Viaje> getViajePorChofer(long idChofer)
	{
		Query query = em.createNamedQuery("viajePorChofer");
		query.setParameter(1, idChofer);

		try
		{
			return query.getResultList();
		} catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Obtiene el viaje activo asociado a un chofer. Un viaje activo es un viaje
	 * que tenga asignado a dicho chofer y cuyo estado sea asignado o iniciado.
	 * 
	 * @param idChofer
	 *            la id del chofer asignado al viaje.
	 * @return el viaje activo, si lo hay. Null caso contrario.
	 */
	public Viaje getViajeActivoChofer(long idChofer)
	{
		return getViajeActivo("viajeActivoChofer", idChofer);
	}

	/**
	 * Obtiene el viaje activo asociado a un pasajero. Un viaje activo es un
	 * viaje perteneciente a dicho pasajero y cuyo estado no sea cancelado o
	 * finalizado.
	 * 
	 * @param idPasajero
	 *            la id del chofer asignado al viaje.
	 * @return el viaje activo, si lo hay. Null caso contrario.
	 */
	public Viaje getViajeActivoPasajero(long idPasajero)
	{
		return getViajeActivo("viajeActivoPasajero", idPasajero);
	}

	/**
	 * Obtiene el viaje activo a partir de un query dado.
	 * 
	 * @param nombreQuery
	 *            el query a utilizar para encontrar el viaje activo.
	 * @param id
	 *            el id del usuario.
	 * @return el viaje activo, si lo hay. Null caso contrario.
	 */
	private Viaje getViajeActivo(String nombreQuery, long id)
	{
		Query query = em.createNamedQuery(nombreQuery);
		query.setParameter(1, id);
		query.setParameter(2, EstadoViaje.CANCELADO);

		try
		{
			return (Viaje) query.getResultList().get(0);
		} catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Obtiene el tiempo estimado de llegada al proximo destino. En caso que el
	 * estado del viaje sea asignado, se calcula en base a la distancia entre el
	 * chofer y el punto de encuentro. En caso que sea iniciado, el tiempo sera
	 * en base al movil con respecto del destino final, si lo hay.
	 * 
	 * @param idViaje
	 *            el id del viaje a calcular el tiempo estimado.
	 * @return el tiempo estimado, si el viaje es valido y se encuentra en
	 *         alguno de los estados previamente dichos.
	 */
	public String getTiempoEstimado(long idViaje)
	{
		PuntoGeografico origen, destino;
		Viaje viaje = (Viaje) new ControladorGenerico().buscarPorID(
				Viaje.class, idViaje);

		if (viaje == null)
			return null;

		if (viaje.isActive()
				&& !viaje.getEstado().equals(EstadoViaje.SIN_CHOFER))
		{
			origen = viaje.getTurno().getChofer().getUbicacion();

			switch (viaje.getEstado())
			{
			case ASIGNADO:
				destino = getOrigenViaje(viaje);
			case INICIADO:
				destino = getDestinoViaje(viaje);
			default:
				destino = origen;
			}

			return new GoogleGeoCodeHelper().getTiempoEstimado(origen, destino);
		} else
			return null;
	}

	/**
	 * Obtiene el punto de origen de un viaje.
	 * 
	 * @param idViaje
	 *            la id del viaje.
	 * @return el punto de origen, si el viaje es valido y lo tiene.
	 */
	public PuntoGeografico getOrigenViaje(long idViaje)
	{
		Viaje viaje = (Viaje) new ControladorGenerico().buscarPorID(
				Viaje.class, idViaje);

		return getOrigenViaje(viaje);
	}

	/**
	 * Obtiene el punto de origen de un viaje.
	 * 
	 * @param viaje
	 *            el viaje del cual obtener el punto.
	 * @return el origen, si lo tiene.
	 */
	public PuntoGeografico getOrigenViaje(Viaje viaje)
	{
		if (viaje == null || !viaje.isActive())
			return null;

		List<PuntoGeografico> puntos = viaje.getPuntos();

		if (puntos == null || puntos.isEmpty())
			return null;

		return puntos.get(0);
	}

	/**
	 * Obtiene el destino de un viaje.
	 * 
	 * @param idViaje
	 *            la id del viaje.
	 * @return el punto de destino, si el viaje es valido y lo tiene.
	 */
	public PuntoGeografico getDestinoViaje(long idViaje)
	{
		Viaje viaje = (Viaje) new ControladorGenerico().buscarPorID(
				Viaje.class, idViaje);

		return getDestinoViaje(viaje);
	}

	/**
	 * Obtiene el destino de un viaje.
	 * 
	 * @param viaje
	 *            el viaje del cual obtener el destino.
	 * @return el destino, si lo tiene.
	 */
	public PuntoGeografico getDestinoViaje(Viaje viaje)
	{
		if (viaje == null || !viaje.isActive())
			return null;

		List<PuntoGeografico> puntos = viaje.getPuntos();

		if (puntos == null || puntos.isEmpty() || puntos.size() == 1)
			return null;

		return puntos.get(puntos.size() - 1);
	}

	/**
	 * Califica al pasajero
	 * 
	 * @param idViaje
	 * @param calificacionValor
	 * @param detalle
	 * @return
	 */
	public Boolean calificar(long idViaje, int calificacionValor, String detalle)
	{
		ControladorGenerico con = new ControladorGenerico();
		Viaje viaje = (Viaje) con.buscarPorID(Viaje.class, idViaje);

		if ((calificacionValor == -1 || calificacionValor == 0 || calificacionValor == 1)
				&& viaje != null)
		{
			Calificacion calificacion = new Calificacion(calificacionValor,
					detalle);
			viaje.setCalificacion(calificacion);
			return edit(viaje);
		}
		return false;
	}

	/**
	 * calcula calificaciones
	 * @param id
	 * @return vector de calificaciones
	 */
	public int[] calificaciones(long id)
	{
		int[] calificaciones = new int[3];
		ControladorGenerico con = new ControladorGenerico();
		Object[] parametros = new Object[1];
		parametros[0] = id;
		List<Viaje> viajes = con.buscarPorQuery("viajesPorPasajeros", parametros, 0);
		 
		for (int i = 0; i < calificaciones.length; i++)
		{
			calificaciones[i] = 0;
		}

		Calificacion calificacion;

		if (viajes != null)
		{
			for (int i = 0; i < viajes.size(); i++)
			{
				calificacion = viajes.get(i).getCalificacion();
				if (calificacion != null)
				{
					switch (calificacion.getCalificacion())
					{
					case Constantes.Calificaciones.NEGATIVA:
						calificaciones[0]++;
						break;
					case Constantes.Calificaciones.NEUTRAL:
						calificaciones[1]++;
						break;
					case Constantes.Calificaciones.POSITIVA:
						calificaciones[2]++;
						break;
					default:
						;
					}
				}
			}
		}
		return calificaciones;
	}
}
