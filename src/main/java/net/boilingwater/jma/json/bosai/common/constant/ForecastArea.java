package net.boilingwater.jma.json.bosai.common.constant;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;

@ToString
public class ForecastArea extends HashMap<String, LinkedList<ForecastArea.InnerForecastArea>> {
    private static ForecastArea INSTANCE;

    private ForecastArea() {

    }

    public static ForecastArea getForecastArea() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new ObjectMapper().readValue(new URL("https://www.jma.go.jp/bosai/forecast/const/forecast_area.json"), ForecastArea.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    @ToString
    public static class InnerForecastArea {
        public String class10;
        public List<String> amedas;
        public String class20;

    }
}
