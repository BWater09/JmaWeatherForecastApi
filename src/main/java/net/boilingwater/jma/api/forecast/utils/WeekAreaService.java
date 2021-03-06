package net.boilingwater.jma.api.forecast.utils;

import net.boilingwater.jma.json.bosai.common.constant.WeekArea;

public class WeekAreaService {
    public static WeekArea.InnerWeekArea getInnerWeekArea(String officeAreaCode, String class15AreaCode) {
        return WeekArea.getWeekArea().get(officeAreaCode).stream().filter(iwa -> iwa.week.equals(class15AreaCode)).findFirst().orElse(null);
    }
}
