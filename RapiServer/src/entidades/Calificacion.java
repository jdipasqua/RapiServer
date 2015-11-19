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
@Table (name = "CALIFICACIONES")
public class Calificacion implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column (name = "ID_CALIFICACION")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column (name = "CALIFICACION")
    private int calificacion;
    
    @Column (name = "DETALLE")
    private String detalle;

    public Calificacion ()
    {
        
    }
    
    public Calificacion(int calificacion, String detalle)
    {
        this.calificacion = calificacion;
        this.detalle = detalle;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public int getCalificacion()
    {
        return calificacion;
    }

    public void setCalificacion(int calificacion)
    {
        this.calificacion = calificacion;
    }

    public String getDetalle()
    {
        return detalle;
    }

    public void setDetalle(String detalle)
    {
        this.detalle = detalle;
    }
}
