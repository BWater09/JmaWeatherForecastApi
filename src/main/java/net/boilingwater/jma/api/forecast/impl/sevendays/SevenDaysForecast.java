package net.boilingwater.jma.api.forecast.impl.sevendays;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.boilingwater.jma.api.forecast.AbstractDaysWeatherForecast;
import net.boilingwater.jma.api.forecast.AreaData;
import net.boilingwater.jma.api.forecast.utils.ForecastAreaService;
import net.boilingwater.jma.api.forecast.utils.WeekArea05Service;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.Forecast;
import org.apache.commons.lang3.StringUtils;

/**
 * 1週間予報を示すクラス
 */
@Getter
@ToString
public class SevenDaysForecast extends AbstractDaysWeatherForecast {
    /**
     * 1週間の平年値（気温）
     */
    protected Average tempAverage;
    /**
     * 1週間の平年値（降水量の7日間合計）
     */
    protected Average precipAverage;

    public SevenDaysForecast(Forecast f, AreaData areaData) {
        super(f, areaData, 7);
        tempAverage = null;
        precipAverage = null;
    }

    @Override
    protected void setForecast(Forecast f, AreaData areaData, int dayCount) {
        List<String> list = WeekArea05Service.get(areaData.getClass10AreaCode());
        int class10Id = getIndex(f.getLast().timeSeries.get(0).areas, list);
        List<String> amedasAreaCodeList = ForecastAreaService.getAmedasList(areaData.getOfficeAreaCode(), areaData.getClass10AreaCode(), areaData.getClass20AreaCode());
        publishingOffice = f.getFirst().publishingOffice;
        areaName = f.getLast().timeSeries.get(0).areas.get(class10Id).area.name;
        reportDateTime = f.getFirst().reportDatetime;

        for (int day = 0; day < dayCount; day++) {
            dayForecasts.add(new OneDayWeatherForecastForSevenDays(f.getLast().timeSeries, class10Id, amedasAreaCodeList, day));
        }
        setTempAverage(f, amedasAreaCodeList.get(0));
        setPrecipAverage(f, amedasAreaCodeList.get(0));
    }

    /**
     * 1週間の平年値（降水量の7日間合計）を取得します
     *
     * @param f              {@link Forecast} API取得結果
     * @param amedasAreaCode 対象のアメダスコードのリスト
     */
    private void setPrecipAverage(Forecast f, String amedasAreaCode) {
        precipAverage = f.getLast().precipAverage.areas
                .stream()
                .filter(area -> amedasAreaCode.equals(area.area.code))
                .findFirst()
                .map(area ->
                        new SevenDaysForecast.Average(
                                area.area.name,
                                StringUtils.isNotEmpty(area.min) ? Double.parseDouble(area.min) : null,
                                StringUtils.isNotEmpty(area.max) ? Double.parseDouble(area.max) : null
                        )
                ).orElse(new Average("", Double.MIN_VALUE, Double.MAX_VALUE));
    }

    /**
     * 1週間の平年値（気温）を取得します
     *
     * @param f              {@link Forecast} API取得結果
     * @param amedasAreaCode 対象のアメダスコードのリスト
     */
    private void setTempAverage(Forecast f, String amedasAreaCode) {
        tempAverage = f.getLast().tempAverage.areas
                .stream()
                .filter(area -> amedasAreaCode.equals(area.area.code))
                .findFirst()
                .map(area ->
                        new SevenDaysForecast.Average(
                                area.area.name,
                                StringUtils.isNotEmpty(area.min) ? Double.parseDouble(area.min) : null,
                                StringUtils.isNotEmpty(area.max) ? Double.parseDouble(area.max) : null
                        )
                ).orElse(new Average("", Double.MIN_VALUE, Double.MAX_VALUE));
    }

    public List<OneDayWeatherForecastForSevenDays> getDaysForecasts() {
        return dayForecasts.stream().map(OneDayWeatherForecastForSevenDays.class::cast).collect(Collectors.toList());
    }

    /**
     * 1週間の平年値を表すクラス
     */
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Average {
        private String amedasName;
        private Double min;
        private Double max;
    }
}
