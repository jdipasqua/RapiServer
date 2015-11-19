/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FAVORITOS")
public class Favorito
{
	@Id
	@Column(name = "ID_FAVORITO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "NOMBRE")
	private String nombre;

	@OneToOne
	@JoinColumn(name = "PUNTO")
	private PuntoGeografico punto;

	public Favorito()
	{

	}

	public Favorito(String nombre, PuntoGeografico punto)
	{
		super();
		this.nombre = nombre;
		this.punto = punto;
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

	public PuntoGeografico getPunto()
	{
		return punto;
	}

	public void setPunto(PuntoGeografico punto)
	{
		this.punto = punto;
	}
}
