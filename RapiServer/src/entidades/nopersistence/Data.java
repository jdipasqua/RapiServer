/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades.nopersistence;

/**
 *
 * @author fcarou
 */
public class Data
{
	private String tipo;
	private Object object;
	
	public Data (String tipo, Object object)
	{
		this.tipo = tipo;
		this.object = object;
	}

	public String getTipo()
	{
		return tipo;
	}

	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}

	public Object getObject()
	{
		return object;
	}

	public void setObject(Object object)
	{
		this.object = object;
	}
}
