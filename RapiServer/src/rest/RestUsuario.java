/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rest;

import controladores.ControladorGenerico;
import entidades.usuarios.Usuario;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 *
 * @author fcarou
 */
@Path ("usuario")
public class RestUsuario
{
    @POST
    @Path ("cargarCodigo")
    public boolean cargarCodigo (@QueryParam ("id") long id, @HeaderParam ("codigo") String codigo)
    {
        ControladorGenerico con = new ControladorGenerico();
        
        Usuario usuario = (Usuario) con.buscarPorID(Usuario.class, id);
        
        usuario.setGcm(codigo);
        return con.edit(usuario);
    }
}
