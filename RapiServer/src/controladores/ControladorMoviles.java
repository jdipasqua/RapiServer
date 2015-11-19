package controladores;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import entidades.Movil;
import entidades.usuarios.Chofer;

public class ControladorMoviles extends BaseControlador
{
	public ControladorMoviles ()
	{
		super();
	}
	
	public ControladorMoviles (EntityManager em)
	{
		super(em);
	}
	
	/**
     * Obtiene los moviles de un chofer.
     * @param idChofer la id de un chofer.
     * @return la lista de moviles, si el chofer es valido y los tiene.
     */
	public List<Movil> getMovilesChofer (long idChofer)
	{
		Chofer chofer = (Chofer) new ControladorGenerico().buscarPorID(Chofer.class, idChofer);
		
		if (chofer == null)
			return null;
		
		return chofer.getMoviles();
	}
	
	/**
	 * Obtiene el movil con una patente dada.
	 * @param patente la patente del movil.
	 * @return el movil, si lo hay.
	 */
	public Movil buscarPorPatente (String patente)
	{
		Query query = em.createNamedQuery("movilPorPatente");
		query.setParameter(1, patente);
		
		try
		{
			return (Movil) query.getResultList().get(0);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Obtiene el movil con una patente dada sobre un chofer.
	 * @param chofer el chofer en el cual buscar el movil.
	 * @param patente la patente del movil.
	 * @return el movil, si existe y es del chofer dado.
	 */
	public Movil buscarPorPatente (Chofer chofer, String patente)
	{
		if (chofer.getMoviles() == null)
			return null;
		
		for (Movil movil : chofer.getMoviles())
			if (movil.getPatente().equals(patente))
				return movil;
		
		return null;
	}
	
	public Movil buscarPorIDMovil (Chofer chofer, long idMovil)
	{
		if (chofer == null)
			return null;
		
		List<Movil> moviles = chofer.getMoviles();
		
		if (moviles == null || moviles.isEmpty())
			return null;
		
		for (Movil movil : moviles)
			if (movil.getId() == idMovil)
				return movil;
		
		return null;
	}
	
	/**
	 * Agrega un movil a un chofer. No puede haber dos moviles de igual patente.
	 * @param idChofer la id del chofer a agregar el movil.
	 * @param movil el movil a agregar.
	 * @return true si la operacion se pudo completar.
	 */
	public boolean agregarMovil (long idChofer, Movil movil)
	{
		Chofer chofer = (Chofer) new ControladorGenerico().buscarPorID(Chofer.class, idChofer);
		
		if (chofer == null || movil == null)
			return false;
		
		// Verifico la existencia de otro movil con la misma patente.
		if (buscarPorPatente(movil.getPatente()) != null)
			return false;
		
		// Si el chofer no tiene una lista de moviles, la instancio.
		if (chofer.getMoviles() == null)
			chofer.setMoviles(new ArrayList<Movil>());
		
		chofer.getMoviles().add(movil);
		
		return edit(chofer);
	}
	
	/**
	 * Elimina un movil de un chofer.
	 * @param idChofer la id del chofer dueño del movil.
	 * @param patente la patente del movil.
	 * @return true si la operacion se pudo completar.
	 */
	public boolean eliminarMovil (long idChofer, String patente)
	{
		Chofer chofer = (Chofer) new ControladorGenerico().buscarPorID(Chofer.class, idChofer);
		
		if (chofer == null || patente == null || patente.equals(""))
			return false;
		
		// Obtengo el movil del chofer.
		Movil movil = buscarPorPatente(chofer, patente);
		
		// En caso que el movil no exista, o exista pero no es del chofer dado, retorno false.
		if (movil == null)
			return false;
		
		chofer.getMoviles().remove(movil);
		
		return edit(chofer);
	}
}
