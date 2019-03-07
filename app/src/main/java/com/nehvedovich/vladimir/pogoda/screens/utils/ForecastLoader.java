package com.nehvedovich.vladimir.pogoda.screens.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//Вспомогательный класс для работы с API openweathermap.org и скачивания нужных данных

public final class ForecastLoader {
    private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&&apikey=%s&units=metric&lang=%s";
    //private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    //private static final String KEY = "x-api-key";
    //APP ID вашего аккаунта на сайте погоды: бесплатен
    //Рекомендую завести и скопировать сюда собственный ключ
    private static final String API_KEY = "bb0856d6336d3c2ca1a809b325fecefa";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int RESPONSE_SUCCESS = 200;
    //Единственный метод класса, который делает запрос на сервер и получает от него данные
    //Возвращает объект JSON или null
    public static JSONObject getJsonData(String city, String location) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city, API_KEY, location));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.addRequestProperty(KEY, API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;
            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable).append(NEW_LINE);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if (jsonObject.getInt(RESPONSE) == RESPONSE_SUCCESS) {
                return jsonObject;
            } else {
                return null;//FIXME Обработка ошибки
            }
        } catch (Exception e) {
            return null; //FIXME Обработка ошибки
        }
    }

    private ForecastLoader() {
    }
}
