/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades.usuarios;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Table(name = "USUARIOS")
public abstract class Usuario implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_USUARIO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(name = "NOMBRE")
    protected String nombre;

    @Column(name = "APELLIDO")
    protected String apellido;

    @Column(name = "TELEFONO")
    protected String telefono;

    @Column(name = "EMAIL")
    protected String email;
    
    @Column(name = "CLAVE")
    protected String clave;
    
    @Column(name = "ID_GCM")
    protected String gcm;

    @Lob
    @Column(name = "IMAGEN")
    protected byte[] imagen;

    public Usuario()
    {

    }

    public Usuario(String nombre, String apellido, String telefono, String email, String clave, byte[] imagen, String gcm)
    {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.imagen = imagen;
        this.clave = clave;
        this.gcm = gcm;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellido()
    {
        return apellido;
    }

    public void setApellido(String apellido)
    {
        this.apellido = apellido;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public byte[] getImagen()
    {
        return imagen;
    }

    public void setImagen(byte[] imagen)
    {
        this.imagen = imagen;
    }

    public String getClave()
    {
        return clave;
    }

    public void setClave(String clave)
    {
        this.clave = clave;
    }

    public String getGcm()
    {
        return gcm;
    }

    public void setGcm(String gcm)
    {
        this.gcm = gcm;
    }
}
