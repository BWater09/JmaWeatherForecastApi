package net.boilingwater.jma.api.forecast.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.boilingwater.jma.json.bosai.common.constant.WeekArea05;

public class WeekArea05Service {
    public static List<String> get(String class10AreaCode) {
        return WeekArea05.getWeekArea05()
                .entrySet().stream()
                .filter(e -> e.getKey().equals(class10AreaCode))
                .findFirst()
                .map(Map.Entry::getValue).orElse(Collections.emptyList());
    }
}
