/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import entidades.estados.Estados.EstadoViaje;
import entidades.usuarios.Pasajero;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table (name = "VIAJES")
@NamedQueries
({
	@NamedQuery (name = "viajePorChofer", query = "select v from Viaje v where v.turno.chofer.id = ?1"),
	@NamedQuery (name = "viajeActivoChofer", query = "select v from Viaje v where v.turno.chofer.id = ?1 and v.fin = null and v.estado != ?2"),
	@NamedQuery (name = "viajeActivoPasajero", query = "select v from Viaje v where v.pasajero.id = ?1 and v.fin = null and v.estado != ?2"),
	@NamedQuery (name = "viajesPorPasajeros", query = "select v from Viaje v where v.pasajero.id = ?1 and v.fin != null")
})
public class Viaje implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column (name = "ID_VIAJE")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    
    @Temporal (TemporalType.TIMESTAMP)
    @Column (name = "INICIO")
    private Calendar inicio;
    
    @Temporal (TemporalType.TIMESTAMP)
    @Column (name = "FIN")
    private Calendar fin;
    
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "CALIFICACION")
    private Calificacion calificacion;
    
    @ManyToOne (cascade = {CascadeType.REFRESH})
    @JoinColumn (name = "TURNO")
    private Turno turno;
    
    @ManyToOne (cascade = CascadeType.REFRESH)
    @JoinColumn (name = "PASAJERO")
    private Pasajero pasajero;
    
    @Enumerated(EnumType.STRING)
    @Column (name = "ESTADO")
    private EstadoViaje estado;
    
    @Column (name = "COMENTARIO")
    private String comentario;
    
    @OneToMany (cascade = CascadeType.ALL)
    private List<PuntoGeografico> puntos;
    
    public Viaje ()
    {
        
    }
    
    public Viaje (Pasajero pasajero)
    {
    	this.pasajero = pasajero;
    }
    
    public Viaje (Pasajero pasajero, String comentario)
    {
    	this.pasajero = pasajero;
    	this.comentario = comentario;
    }
    
    public Viaje(Calendar inicio, Pasajero pasajero)
    {
        this.inicio = inicio;
        this.pasajero = pasajero;
    }

    public Viaje(Calendar inicio, Turno turno, Pasajero pasajero)
    {
        this.inicio = inicio;
        this.turno = turno;
        this.pasajero = pasajero;
    }
    
    public boolean isActive ()
    {	
    	switch (estado)
    	{
    	case ASIGNADO:
    	case INICIADO:
    	case SIN_CHOFER:
    		return true;
    	case CANCELADO:
    	case FINALIZADO:
    		return false;
    	}
    	
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

    public Calificacion getCalificacion()
    {
        return calificacion;
    }

    public void setCalificacion(Calificacion calificacion)
    {
        this.calificacion = calificacion;
    }

    public Turno getTurno()
    {
        return turno;
    }

    public void setTurno(Turno turno)
    {
        this.turno = turno;
    }

    public EstadoViaje getEstado()
    {
        return estado;
    }

    public void setEstado(EstadoViaje estado)
    {
        this.estado = estado;
    }

    public List<PuntoGeografico> getPuntos()
    {
        return puntos;
    }

    public void setPuntos(List<PuntoGeografico> puntos)
    {
        this.puntos = puntos;
    }

    public Pasajero getPasajero()
    {
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero)
    {
        this.pasajero = pasajero;
    }

	public String getComentario()
	{
		return comentario;
	}

	public void setComentario(String comentario)
	{
		this.comentario = comentario;
	}
}
