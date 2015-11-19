package utiles.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/** 
 * Facilita las tareas de conversion objeto-JSON y viceversa.
 * @author fcarou
 * @param <T> el tipo de objeto del/hacia el cual se convertira.
 */
public class JSONParser <T>
{
    private final Gson gson;
    private final Class<?> clazz;
    
    public JSONParser (Class<?> clazz)
    {
        this.clazz = clazz;
        
        GsonBuilder b = new GsonBuilder();
        b.registerTypeAdapter(Calendar.class, new CalendarSerializer());
        gson = b.create();
    }
    
    /**
     * Convierte una cadena que representa un arreglo de objetos en formato JSON a una lista de ese objeto.
     * @param array la cadena que contiene el arreglo en formato JSON.
     * @return una lista que contiene los objetos, null si fallo.
     */
    public List<T> parseArray (String array)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(array);
            List<T> list = new ArrayList<T>();
            
            if (jsonArray.length() == 0)
                return list;
            
            for (int i = 0; i < jsonArray.length(); i++)
                list.add(fromJson(jsonArray.getJSONObject(i)));
            
            return list;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Transofrma un objeto en formato JSON a un objeto de clase T.
     * @param object el objeto a transformar.
     * @return el objeto transformado. Null si fallo.
     */
    public T fromJson (JSONObject object)
    {
        return fromJson(object.toString());
    }
    
    /**
     * Transforma un objeto en formato String a un objeto de clase T.
     * @param object el string representando el objeto en formato JSON.
     * @return el objeto, null si fallo.
     */
    @SuppressWarnings("unchecked")
	public T fromJson (String object)
    {
        try
        {
            return (T) gson.fromJson(object, clazz);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Transforma un objeto a formato JSON.
     * @param object el objeto a transformar.
     * @return la cadena que representa al objeto en formato JSON, null si fallo.
     */
    public String toJson (T object)
    {
        try
        {
            return gson.toJson(object);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}