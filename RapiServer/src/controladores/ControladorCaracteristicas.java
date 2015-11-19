package controladores;

import javax.persistence.Query;
import entidades.Caracteristica;

public class ControladorCaracteristicas extends BaseControlador
{
	public boolean agregar (Caracteristica caracteristica)
	{
		if (caracteristicaPorNombre(caracteristica.getNombre()) != null)
			return false;		
		
		return create(caracteristica);
	}
	
	public Caracteristica caracteristicaPorNombre (String nombre)
	{
		Query query = em.createNamedQuery("caracteristicaPorNombre");
		
		query.setParameter(1, nombre);
		
		try
		{
			return (Caracteristica) query.getResultList().get(0);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
