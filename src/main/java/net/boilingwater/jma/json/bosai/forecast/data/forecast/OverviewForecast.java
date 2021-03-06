package net.boilingwater.jma.json.bosai.forecast.data.forecast;

import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.ToString;

@ToString
public class OverviewForecast {
    public String publishingOffice;
    public OffsetDateTime reportDatetime;
    public String targetArea;
    public String headlineText;
    public String text;

    private OverviewForecast() {

    }

    public static OverviewForecast fetchOverviewForecast(String areaCode) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule())
                    .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                    .readValue(new URL("https://www.jma.go.jp/bosai/forecast/data/overview_forecast/" + areaCode + ".json"), OverviewForecast.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
