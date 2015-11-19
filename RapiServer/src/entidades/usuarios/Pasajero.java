/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.usuarios;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import entidades.Favorito;
import entidades.Movil;

@Entity
@Table (name = "PASAJEROS")
@NamedQueries
({
    @NamedQuery (name = "PasajeroPorClave", query = "select p from Pasajero p where p.id = ?1 and p.clave = ?2"),
    @NamedQuery (name = "PasajeroPorEmail", query = "select p from Pasajero p where p.email = ?1"),
    @NamedQuery (name = "PasajeroPorEmailClave", query = "select p from Pasajero p where p.email = ?1 and p.clave = ?2")
})
public class Pasajero extends Usuario
{
    private static final long serialVersionUID = 1L;

    @OneToMany (cascade = CascadeType.ALL)
    private List<Favorito> favoritos;
    
    public Pasajero()
    {
        super();
    }

    public Pasajero(String nombre, String apellido, String telefono, String email, String clave, byte[] imagen, String gcm)
    {
        super(nombre, apellido, telefono, email, clave, imagen, gcm);
        this.favoritos = new ArrayList<Favorito>();
    }
    
    public Chofer transformar (Movil movil)
    {
    	Chofer chofer = new Chofer(nombre, apellido, telefono, email, imagen, clave, gcm);
    	
    	List<Movil> moviles = new ArrayList<>();
    	moviles.add(movil);
    	
    	chofer.setMoviles(moviles);
    	
    	return chofer;
    }

	public List<Favorito> getFavoritos()
	{
		return favoritos;
	}

	public void setFavoritos(List<Favorito> favoritos)
	{
		this.favoritos = favoritos;
	}
	
	public boolean agregarFavorito(Favorito favorito)
	{
		return this.favoritos.add(favorito);
	}
    
    
}
