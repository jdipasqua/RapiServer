/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package conexion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author fcarou
 */
public class Conexion
{
    private EntityManagerFactory emf;
    private EntityManager em;
    
    private static Conexion conexion;
    
    private Conexion ()
    {
        emf = Persistence.createEntityManagerFactory("RapiServer");
        em = emf.createEntityManager();
    }
    
    public static Conexion getConexion ()
    {
        if (conexion == null)
            conexion = new Conexion();
        
        return conexion;
    }
    
    public EntityManager getEntityManager ()
    {
        return em;
    }
}
