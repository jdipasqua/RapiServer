package controladores;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import entidades.Movil;
import entidades.Turno;
import entidades.estados.Estados.EstadoChofer;
import entidades.usuarios.Chofer;

public class ControladorTurnos extends BaseControlador
{
	public ControladorTurnos ()
	{
		
	}
	
	/**
	 * Encuentra la lista de turnos asociada a un chofer.
	 * @param id el id del chofer.
	 * @return la lista de turnos, si existe.
	 */
	@SuppressWarnings("unchecked")
	public List<Turno> turnoPorChofer (long id)
	{
		Query query = em.createNamedQuery("turnoPorChofer");
		query.setParameter(1, id);
		
		return query.getResultList();
	}
	
	/**
	 * Encuentra el turno abierto, es decir, el ultimo, de un chofer.
	 * @param idChofer la id del chofer.
	 * @return el turno, si existe.
	 */
	public Turno getTurnoAbierto (long idChofer)
	{
		Query query = em.createNamedQuery("turnoAbiertoPorChofer");
		query.setParameter(1, idChofer);
		
		try
		{
			return (Turno) query.getResultList().get(0);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Inicia un turno para un chofer, tomando la hora de inicio del sistema.
	 * @param chofer el chofer sobre el cual iniciar el turno, solo debe tener su id y id del movil, dado que sera recuperado de la DB.
	 * @return el turno iniciado, si se pudo completar la operacion.
	 */
	public Turno iniciarTurno (Chofer chofer)
	{
		try
		{
			return iniciarTurno(chofer.getId(), chofer.getMoviles().get(0).getId());
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Inicia un turno para un chofer, tomando la hora de inicio del sistema.
	 * @param email el email del chofer.
	 * @param clave la clave.
	 * @return el turno si la operacion se pudo completar. Null caso contrario.
	 */
	public Turno iniciarTurno (long idChofer, long idMovil)
	{
		Chofer chofer = (Chofer) new ControladorGenerico().buscarPorID(Chofer.class, idChofer);
		
		if (chofer == null)
			return null;
		
		// Verifico que el chofer no tenga un turno abierto.
		Turno turno = getTurnoAbierto(idChofer);
		
		if (turno != null)
			return null;
		
		Movil movil = new ControladorMoviles().buscarPorIDMovil(chofer, idMovil);
		
		if (movil == null)
			return null;
		
		turno = new Turno(Calendar.getInstance(), movil, chofer);
		
		chofer.setEstado(EstadoChofer.DISPONIBLE);
		
		if (edit(chofer))
			if (create(turno))
				return turno;
		
		return null;
	}
	
	/**
	 * Cierra el ultimo turno de un chofer, tomando la hora de cierre del sistema.
	 * @param idChofer la id del chofer.
	 * @return true si se pudo cerrar.
	 */
	public boolean cerrarTurno (long idChofer)
	{
		return cerrarTurno(idChofer, Calendar.getInstance());
	}
	
	/**
	 * Cierra el ultimo turno de un chofer.
	 * @param idChofer la id del chofer.
	 * @param hora la hora de cierre.
	 * @return true si se pudo cerrar.
	 */
	public boolean cerrarTurno (long idChofer, Calendar hora)
	{
		// Verifico que el chofer tenga un turno activo.
		Turno turno = getTurnoAbierto(idChofer);
		
		if (turno == null)
			return false;
		
		new ControladorViajes().forzarFinalizacion(idChofer);
		
		turno.setFin(hora);
		
		return super.edit(turno);
	}
	
	/**
	 * Asigna un movil a un turno. No se puede asignar un movil a un turno si este ya lo tiene.
	 * @param idChofer la id del chofer del turno activo.
	 * @param patente la patente del movil, debe pertenecer al chofer.
	 * @return true si se pudo completar la operacion.
	 */
	public boolean asignarMovil (long idChofer, String patente)
	{
		Turno turno = getTurnoAbierto(idChofer);
		
		if (turno == null || turno.getMovil() != null)
			return false;
		
		Chofer chofer = turno.getChofer();
		
		Movil movil = new ControladorMoviles().buscarPorPatente(chofer, patente);
		
		if (movil == null)
			return false;
		
		turno.setMovil(movil);
		
		return edit(turno);
	}
	
	/**
	 * Obtiene los choferes que tienen turnos abiertos y un estado dado.
	 * @param estado el estado que deben tener los choferes.
	 * @return los choferes, si los hay.
	 */
	@SuppressWarnings("unchecked")
	public List<Chofer> getChoferesTurnoAbierto (EstadoChofer estado)
	{
		Query query = em.createNamedQuery("choferesTurnoPorEstado");
		query.setParameter(1, estado);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Turno> getTurnosAbiertos ()
	{
		Query query = em.createNamedQuery("turnosAbiertos");
		
		return query.getResultList();
	}
}
