/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.usuarios;

import entidades.Movil;
import entidades.PuntoGeografico;
import entidades.estados.Estados.EstadoChofer;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@NamedQueries
({
    @NamedQuery (name = "choferPorClave", query = "select c from Chofer c where c.id = ?1 and c.clave = ?2"),
    @NamedQuery (name = "choferPorEmail", query = "select c from Chofer c where c.email = ?1"),
    @NamedQuery (name = "choferPorEmailClave", query = "select c from Chofer c where c.email = ?1 and c.clave = ?2")
})
public class Chofer extends Usuario
{	
    private static final long serialVersionUID = 1L;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movil> moviles;

    @Enumerated (EnumType.STRING)
    @Column(name = "ESTADO_CHOFER")
    private EstadoChofer estado;

    @OneToOne (cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "UBICACION")
    private PuntoGeografico ubicacion;

    public Chofer()
    {
        super();
    }

    public Chofer(String nombre, String apellido, String telefono, String email, byte[] imagen, String clave, String gcm)
    {
        super(nombre, apellido, telefono, email, clave, imagen, gcm);
    }
    
    public Pasajero transformar ()
    {
    	return new Pasajero(nombre, apellido, telefono, email, clave, imagen, gcm);
    }

    public List<Movil> getMoviles()
    {
        return moviles;
    }

    public void setMoviles(List<Movil> moviles)
    {
        this.moviles = moviles;
    }

    public EstadoChofer getEstado()
    {
        return estado;
    }

    public void setEstado(EstadoChofer estado)
    {
        this.estado = estado;
    }

    public PuntoGeografico getUbicacion()
    {
        return ubicacion;
    }

    public void setUbicacion(PuntoGeografico ubicacion)
    {
        this.ubicacion = ubicacion;
    }
}
