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
public class Contenido
{
    private String[] registration_ids;
    private Data data;

    public Contenido(String[] registration_ids, Data data)
    {
        this.registration_ids = registration_ids;
        this.data = data;
    }

    public String[] getRegistration_ids()
    {
        return registration_ids;
    }

    public void setRegistration_ids(String[] registration_ids)
    {
        this.registration_ids = registration_ids;
    }

    public Data getData()
    {
        return data;
    }

    public void setData(Data data)
    {
        this.data = data;
    }
}
