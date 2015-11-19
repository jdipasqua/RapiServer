/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utiles;

import entidades.PuntoGeografico;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author fcarou
 */
public class GoogleGeoCodeHelper
{
    private static final String base = "http://maps.googleapis.com/maps/api/geocode/json";
    private static final String baseGoogleDirections = "http://maps.googleapis.com/maps/api/directions/json";
    
    private static final String STRING_STATUS = "status";
    private static final String CIUDAD = "madryn";
    
    public String getDireccion (PuntoGeografico punto)
    {
        try
        {
            String res = new HttpHelper(base).doGet("latlng=" + punto.getLatitud() + "," + punto.getLongitud() + "&sensor=true");
            
            JSONObject obj = new JSONObject(res);
            
            if (!obj.getString(STRING_STATUS).equals("OK"))
                return null;
            
            JSONArray array = obj.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
            
            String altura = array.getJSONObject(0).getString("long_name");
            String calle = array.getJSONObject(1).getString("long_name");
            
            return calle + " " + altura;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            
            return null;
        }
    }
    
    public PuntoGeografico getPuntoGeografico (String direccion)
    {
        try
        {
            String params = "address=" + direccion + "+" + CIUDAD + "&components=country:AR";
            String res = new HttpHelper(base).doGet(params.replace(' ', '+'));
            
            JSONObject obj = new JSONObject(res);
            
            if (!obj.getString(STRING_STATUS).equals("OK"))
                return null;
            
            obj = obj.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            PuntoGeografico puntoNuevo = new PuntoGeografico(obj.getDouble("lat"), obj.getDouble("lng"));
            puntoNuevo.setDireccion(direccion);
            
            return puntoNuevo;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    
    public int getDistancia (PuntoGeografico origen, PuntoGeografico destino)
    {    	
    	try
    	{
    		JSONObject obj = getRecorrido(origen, destino);
    		
    		if (obj == null)
    			return -1;
    		
    		obj = obj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance");
    		
    		return obj.getInt("value");
    	}
    	catch (Exception e)
    	{
    		
    	}
    	
    	return 0;
    }
    
    public String getTiempoEstimado (PuntoGeografico origen, PuntoGeografico destino)
    {
    	if (origen == null || destino == null)
    		return null;
    	
    	try
    	{
    		JSONObject obj = getRecorrido(origen, destino);
    		
    		if (obj == null)
    			return null;
    		
    		obj  = obj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration");
    		
    		return obj.getString("text");
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
    
    private JSONObject getRecorrido (PuntoGeografico origen, PuntoGeografico destino)
    {
    	try
    	{
    		String params = "origin=" + origen.getLatitud() + "," + origen.getLongitud() + "&destination=" + destino.getLatitud() + "," + destino.getLongitud();
    		String res = new HttpHelper(baseGoogleDirections).doGet(params);
    		
    		JSONObject obj = new JSONObject(res);
    		
    		if (!obj.getString(STRING_STATUS).equals("OK"))
    			return null;
    		
    		return obj;
    	}
    	catch (Exception e)
    	{
    		return null;
    	}
    }
    
    public void completarPunto (PuntoGeografico punto)
    {
    	if (punto.getDireccion() == null || punto.getDireccion().equals(""))
    		punto.setDireccion(this.getDireccion(punto));
    	else
    	{
    		if (punto.getLatitud() == 0 && punto.getLongitud() == 0)
    		{
    			PuntoGeografico p = getPuntoGeografico(punto.getDireccion());
    			
    			punto.setLatitud(p.getLatitud());
    			punto.setLongitud(p.getLongitud());
    		}
    	}
    }
}
