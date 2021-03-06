package net.boilingwater.jma.json.bosai.forecast.data.forecast;

import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.ToString;


@ToString
public class Forecast extends LinkedList<Forecast.InnerForecast> {

    private Forecast() {
    }

    public static Forecast fetchForecast(String areaCode) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule())
                    .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                    .readValue(new URL("https://www.jma.go.jp/bosai/forecast/data/forecast/" + areaCode + ".json"), Forecast.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @ToString
    public static class InnerForecast {
        public String publishingOffice;
        public OffsetDateTime reportDatetime;
        public List<TimeSeries> timeSeries;
        public Average tempAverage;
        public Average precipAverage;

        @ToString
        public static class TimeSeries {
            public List<OffsetDateTime> timeDefines;
            public List<Area> areas;

            @ToString
            public static class Area {
                public List<String> weatherCodes;
                public List<String> weathers;
                public List<String> winds;
                public List<String> waves;
                public List<String> pops;
                public List<String> temps;
                public List<String> reliabilities;
                public InnerForecast.InnerArea area;
                public List<String> tempsMin;
                public List<String> tempsMinUpper;
                public List<String> tempsMinLower;
                public List<String> tempsMax;
                public List<String> tempsMaxUpper;
                public List<String> tempsMaxLower;
            }
        }

        //template
        @ToString
        public static class Average {
            public List<Area> areas;

            @ToString
            public static class Area {
                public InnerForecast.InnerArea area;
                public String min;
                public String max;
            }
        }

        @ToString
        public static class InnerArea {
            public String name;
            public String code;

        }
    }

}
