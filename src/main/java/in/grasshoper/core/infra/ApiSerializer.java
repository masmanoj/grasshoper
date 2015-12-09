package in.grasshoper.core.infra;

import java.lang.reflect.Type;
import java.util.Collection;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public final class ApiSerializer<T> {

	private final Gson gson;

	public ApiSerializer() {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(LocalDate.class, new JodaLocalDateAdapter());
		builder.registerTypeAdapter(DateTime.class, new JodaDateTimeAdapter());
		builder.registerTypeAdapter(MonthDay.class, new JodaMonthDayAdapter());

		this.gson = builder.create();
	}

	public String serialize(final Collection<T> collection) {
		return setBuilder().create().toJson(collection);
	}

	public String serialize(final T object) {
		return setBuilder().create().toJson(object);
	}

	private GsonBuilder setBuilder() {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(LocalDate.class, new JodaLocalDateAdapter());
		builder.registerTypeAdapter(DateTime.class, new JodaDateTimeAdapter());
		builder.setPrettyPrinting();
		return builder;
	}

	public String serializeDefault(final Object result) {
		return this.gson.toJson(result);
	}

	private static class JodaMonthDayAdapter implements
			JsonSerializer<MonthDay> {

		@SuppressWarnings("unused")
		@Override
		public JsonElement serialize(final MonthDay src, final Type typeOfSrc,
				final JsonSerializationContext context) {

			JsonArray array = null;
			if (src != null) {
				array = new JsonArray();
				array.add(new JsonPrimitive(src.getMonthOfYear()));
				array.add(new JsonPrimitive(src.getDayOfMonth()));
			}

			return array;
		}
	}

}
