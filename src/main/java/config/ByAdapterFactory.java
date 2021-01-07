package config;

import org.openqa.selenium.By;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

enum ByAdapterFactory implements TypeAdapterFactory {
    INSTANCE;

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (By.class.isAssignableFrom(type.getRawType())) {
            return (TypeAdapter<T>) gson.getAdapter(By.class);
        } else {
            return null;
        }
    }
}
