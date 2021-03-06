package net.boilingwater.jma.api.forecast.impl.threedays;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.ToString;
import net.boilingwater.jma.api.forecast.AbstractDaysWeatherForecast;
import net.boilingwater.jma.api.forecast.AreaData;
import net.boilingwater.jma.api.forecast.utils.ForecastAreaService;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.Forecast;

/**
 * 3日間予報を示すクラス
 */
@Getter
@ToString
public class ThreeDaysForecast extends AbstractDaysWeatherForecast {

    public ThreeDaysForecast(Forecast f, AreaData areaData) {
        super(f, areaData, 3);
    }

    @Override
    protected void setForecast(Forecast f, AreaData areaData, int dayCount) {
        int class10Id = getIndex(f.getFirst().timeSeries.get(0).areas, areaData.getClass10AreaCode());
        List<String> amedasAreaCodeList = ForecastAreaService.getAmedasList(areaData.getOfficeAreaCode(), areaData.getClass10AreaCode(), areaData.getClass20AreaCode());
        publishingOffice = f.getFirst().publishingOffice;
        areaName = f.getFirst().timeSeries.get(0).areas.get(class10Id).area.name;
        reportDateTime = f.getFirst().reportDatetime;
        for (int day = 0; day < dayCount; day++) {
            dayForecasts.add(new OneDayWeatherForecastForThreeDays(f.getFirst().timeSeries, class10Id, amedasAreaCodeList, day));
        }
    }

    public List<OneDayWeatherForecastForThreeDays> getDaysForecasts() {
        return dayForecasts.stream().map(OneDayWeatherForecastForThreeDays.class::cast).collect(Collectors.toList());
    }
}
