/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import entidades.usuarios.Chofer;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table (name = "TURNOS")
@NamedQueries
({
	@NamedQuery (name = "turnosPorChofer", query = "select t from Turno t where t.chofer.id = ?1"),
	@NamedQuery (name = "turnoAbiertoPorChofer", query = "select t from Turno t where t.chofer.id = ?1 and t.fin = null"),
	@NamedQuery (name = "turnosAbiertos", query = "select t from Turno t where t.fin = null"),
	@NamedQuery (name = "choferesTurnoPorEstado", query = "select t.chofer from Turno t where t.fin = null and t.chofer.estado = ?1 and t.movil != null")
})
public class Turno implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column (name = "ID_TURNO")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    
    @Temporal (TemporalType.TIMESTAMP)
    @Column (name = "INICIO")
    private Calendar inicio;
    
    @Temporal (TemporalType.TIMESTAMP)
    @Column (name = "FIN")
    private Calendar fin;
    
    @ManyToOne
    @JoinColumn (name = "MOVIL")
    private Movil movil;
    
    @ManyToOne (cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn (name = "CHOFER")
    private Chofer chofer;

    public Turno ()
    {
        
    }
    
    public Turno(Calendar inicio, Chofer chofer)
    {
    	this(inicio, null, chofer);
    }
    
    public Turno(Calendar inicio, Movil movil, Chofer chofer)
    {
        this.inicio = inicio;
        this.movil = movil;
        this.chofer = chofer;
    }    

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Calendar getInicio()
    {
        return inicio;
    }

    public void setInicio(Calendar inicio)
    {
        this.inicio = inicio;
    }

    public Calendar getFin()
    {
        return fin;
    }

    public void setFin(Calendar fin)
    {
        this.fin = fin;
    }

    public Movil getMovil()
    {
        return movil;
    }

    public void setMovil(Movil movil)
    {
        this.movil = movil;
    }

    public Chofer getChofer()
    {
        return chofer;
    }

    public void setChofer(Chofer chofer)
    {
        this.chofer = chofer;
    }
}
