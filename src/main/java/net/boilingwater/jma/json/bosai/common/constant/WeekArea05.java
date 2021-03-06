package net.boilingwater.jma.json.bosai.common.constant;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;

@ToString
public class WeekArea05 extends HashMap<String, List<String>> {
    public static WeekArea05 INSTANCE;

    private WeekArea05() {
    }

    public static WeekArea05 getWeekArea05() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new ObjectMapper().readValue(new URL("https://www.jma.go.jp/bosai/forecast/const/week_area05.json"), WeekArea05.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }
}
