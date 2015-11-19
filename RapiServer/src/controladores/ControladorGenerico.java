/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Felipe
 */
public class ControladorGenerico extends BaseControlador
{
    public ControladorGenerico ()
    {
        super ();
    }
    
    public ControladorGenerico(EntityManager em)
    {
        super(em);
    }
    
    /**
     * Busca todas las entidades pertenecientes a determinada clase.
     * @param clazz la clase de las entidades.
     * @return la lista de entidades, si las hay.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List buscarTodos (Class clazz)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(clazz));
        Query q = em.createQuery(cq);

        return q.getResultList();
    }
    
    /**
     * Ejecuta una consulta sobre la DB.
     * @param nombreQuery el nombre de la consulta declarada en el Entity.
     * @param params los parametros para la consulta.
     * @param limite el limite de la lista, si es mayor a 0.
     * @return la lista resultante de la consulta, si se pudo realizar.
     */
    @SuppressWarnings("rawtypes")
	public List buscarPorQuery (String nombreQuery, Object[] params, int limite)
    {       
        try
        {
            Query query = em.createNamedQuery(nombreQuery);

            if (limite > 0)
                query.setMaxResults(limite);
            
            for (int i = 0; i < params.length; i++)
                query.setParameter(i + 1, params[i]);

            return query.getResultList();
        }
        catch (Exception e)
        {
            System.out.println("Controlador generico: Excepcion en buscarPorQuery. " + e.getClass().getName());
            return null;
        }
    }
    
    /**
     * Busca un objeto entidad por clave primaria.
     * @param clazz la clase de la entidad.
     * @param id la id de la entidad.
     * @return la entidad, si existe.
     */
    public Object buscarPorID (Class<?> clazz, Object id)
    {
        return em.find(clazz, id);
    }
}
