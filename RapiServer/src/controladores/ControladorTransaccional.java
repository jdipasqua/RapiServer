/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controladores;

/**
 *
 * @author fcarou
 */
public class ControladorTransaccional extends BaseControlador
{
    @Override
    public boolean create(Object o)
    {
        em.persist(o);
        return true;
    }

    @Override
    public boolean edit(Object o)
    {
        em.merge(o);
        return true;
    }
}
