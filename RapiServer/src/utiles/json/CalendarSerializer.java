package utiles.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author fcarou
 */
public class CalendarSerializer implements JsonDeserializer<Calendar>, JsonSerializer<GregorianCalendar>
{

    @Override
    public Calendar deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(je.getAsJsonPrimitive().getAsLong());
        return cal;
    }

    @Override
    public JsonElement serialize(GregorianCalendar t, Type type, JsonSerializationContext jsc)
    {
        return new JsonPrimitive(t.getTimeInMillis());
    }
    
}