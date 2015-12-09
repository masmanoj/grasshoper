package in.grasshoper.core.infra;

import java.lang.reflect.Type;

import org.joda.time.LocalDate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JodaLocalDateAdapter implements JsonSerializer<LocalDate> {

    @Override
    public JsonElement serialize(final LocalDate src, final Type typeOfSrc, final JsonSerializationContext context) {

        JsonArray array = null;
        if (src != null) {
            array = new JsonArray();
            array.add(new JsonPrimitive(src.getYearOfEra()));
            array.add(new JsonPrimitive(src.getMonthOfYear()));
            array.add(new JsonPrimitive(src.getDayOfMonth()));
        }

        return array;
    }
}