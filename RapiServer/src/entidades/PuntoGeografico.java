/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name = "PUNTOS_GEOGRAFICOS")
public class PuntoGeografico implements Serializable, Cloneable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column (name = "ID_PUNTO_GEOGRAFICO")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column (name = "LATITUD")
    private double latitud;
    
    @Column (name = "LONGITUD")
    private double longitud;
    
    @Column (name = "DIRECCION")
    private String direccion;

    public PuntoGeografico ()
    {
        
    }
    
    public PuntoGeografico(double latitud, double longitud)
    {
    	this(latitud, longitud, null);
    }
    
    public PuntoGeografico(double latitud, double longitud, String direccion)
    {
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
    	PuntoGeografico punto = new PuntoGeografico();
    	
    	punto.setDireccion(direccion);
    	punto.setLatitud(latitud);
    	punto.setLongitud(longitud);
    	
    	return punto;
    }
    
    @Override
    public boolean equals(Object obj)
    {
    	if (!(obj instanceof PuntoGeografico))
    		return false;
    	
    	PuntoGeografico punto = (PuntoGeografico) obj;
    	
    	if (punto.getId() == id)
    		return true;
    	
    	return punto.getLatitud() == latitud && punto.getLongitud() == longitud;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public double getLatitud()
    {
        return latitud;
    }

    public void setLatitud(double latitud)
    {
        this.latitud = latitud;
    }

    public double getLongitud()
    {
        return longitud;
    }

    public void setLongitud(double longitud)
    {
        this.longitud = longitud;
    }

	public String getDireccion()
	{
		return direccion;
	}

	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}
}
