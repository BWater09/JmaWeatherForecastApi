package net.boilingwater.jma.json.bosai.common.constant;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;

@ToString
public class WeekArea extends HashMap<String, List<WeekArea.InnerWeekArea>> {
    public static WeekArea INSTANCE;

    private WeekArea() {
    }

    public static WeekArea getWeekArea() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new ObjectMapper().readValue(new URL("https://www.jma.go.jp/bosai/forecast/const/week_area.json"), WeekArea.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    @ToString
    public static class InnerWeekArea {
        public String srf;
        public String week;
        public String amedas;
    }
}

