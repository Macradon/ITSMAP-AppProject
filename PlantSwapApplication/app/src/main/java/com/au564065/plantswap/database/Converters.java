package com.au564065.plantswap.database;

import androidx.room.TypeConverter;

import com.au564065.plantswap.models.Plant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Plant fromString(String value) {
        Type plantType = new TypeToken<Plant>() {}.getType();
        return new Gson().fromJson(value, plantType);
    }
    @TypeConverter
    public static String fromPlant(Plant plant) {
        Gson gson = new Gson();
        String json = gson.toJson(plant);
        return json;
    }
}
