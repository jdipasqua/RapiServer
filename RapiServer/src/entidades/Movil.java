/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table (name = "MOVILES")
@NamedQueries
({
	@NamedQuery (name = "movilPorPatente", query = "select m from Movil m where m.patente = ?1")
})
public class Movil implements Serializable
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column (name = "ID_MOVIL")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column (name = "PATENTE")
    private String patente;
    
    @Column (name = "MARCA")
    private String marca;
    
    @Column (name = "MODELO")
    private String modelo;
    
    @ManyToMany
    private List<Caracteristica> caracteristicas;
    
    public Movil ()
    {
        
    }

    public Movil(String patente, String marca, String modelo, List<Caracteristica> caracteristicas)
    {
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.caracteristicas = caracteristicas;
    }
    
    public boolean validarCaracteristicas (List<Caracteristica> caracteristicas)
    {
    	if (this.caracteristicas == null || this.caracteristicas.isEmpty())
    		return true;
    	
    	for (Caracteristica caracteristica : caracteristicas)
    		if (!existe(caracteristica))
    			return false;
    	
    	return true;
    }
    
    private boolean existe (Caracteristica existe)
    {
    	for (Caracteristica caracteristica : caracteristicas)
    		if (existe.getId() == caracteristica.getId())
    			return true;
    	
    	return false;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getPatente()
    {
        return patente;
    }

    public void setPatente(String patente)
    {
        this.patente = patente;
    }

    public String getMarca()
    {
        return marca;
    }

    public void setMarca(String marca)
    {
        this.marca = marca;
    }

    public String getModelo()
    {
        return modelo;
    }

    public void setModelo(String modelo)
    {
        this.modelo = modelo;
    }

    public List<Caracteristica> getCaracteristicas()
    {
        return caracteristicas;
    }

    public void setCaracteristicas(List<Caracteristica> caracteristicas)
    {
        this.caracteristicas = caracteristicas;
    }
}
