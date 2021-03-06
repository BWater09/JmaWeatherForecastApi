package net.boilingwater.jma.api.forecast.utils;

import java.util.Collections;
import java.util.List;

import net.boilingwater.jma.json.bosai.common.constant.ForecastArea;

public class ForecastAreaService {

    public static List<String> getAmedasList(String officeAreaCode, String class10AreaCode, String class20AreaCode) {
        ForecastArea forecastArea = ForecastArea.getForecastArea();
        for (int i = 0; i < forecastArea.get(officeAreaCode).size(); i++) {
            if (forecastArea.get(officeAreaCode).get(i).class10.equals(class10AreaCode)
                    || forecastArea.get(officeAreaCode).get(i).class20.equals(class20AreaCode)
            ) {
                return forecastArea.get(officeAreaCode).get(i).amedas;
            }
        }
        return Collections.emptyList();
    }
}
