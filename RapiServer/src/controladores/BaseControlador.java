/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;

/**
 *
 * @author Felipe
 */
public class BaseControlador
{
    protected final EntityManager em;
    private EntityTransaction transaccion;
    
    public BaseControlador ()
    {
        this.em = conexion.Conexion.getConexion().getEntityManager();
        transaccion = em.getTransaction();
    }
    
    public BaseControlador (EntityManager em)
    {
        this.em = em;
        transaccion = em.getTransaction();
    }
    
    public void begin ()
    {        
        if (transaccion == null)
            transaccion = em.getTransaction();
        
        if (transaccion.isActive())
            return;
        
        transaccion.begin();
    }
    
    public void commit () throws RollbackException
    {
        transaccion.commit();
    }
    
    public void rollback ()
    {
        transaccion.rollback();
    }
    
    public boolean isActive ()
    {
        return transaccion.isActive();
    }
    
    public EntityManager getEntityManager ()
    {
        return this.em;
    }
    
    public boolean create (Object o)
    {
        try
        {
            begin();
            em.persist(o);
            commit();
            
            return true;
        }
        catch (RollbackException ex)
        {
            return false;
        }
    }
    
    public boolean edit (Object o)
    {
        try
        {
            begin();
            em.merge(o);
            commit();
            
            return true;
        }
        catch (RollbackException ex)
        {
            return false;
        }
    }
    
    public boolean destroy (Object o)
    {
        try
        {
            begin();
            em.remove(o);
            commit();
            
            return true;
        }
        catch (RollbackException ex)
        {
            return false;
        }
    }
}
