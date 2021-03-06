package net.boilingwater.jma.api.forecast.impl.threedays;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.ToString;
import net.boilingwater.jma.api.forecast.AbstractOneDayWeatherForecast;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.Forecast;
import org.apache.commons.lang3.StringUtils;

/**
 * 3日間予報で1日分の天気予報を示すクラス
 */
@Getter
@ToString
public class OneDayWeatherForecastForThreeDays extends AbstractOneDayWeatherForecast {
    protected String weather;
    protected String wind;
    protected String wave;

    public OneDayWeatherForecastForThreeDays(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex, List<String> amedasAreaCodeList, int day) {
        super(timeSeries, areaIndex, amedasAreaCodeList, day);
    }

    @Override
    protected void setForecast(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex, List<String> amedasAreaCodeList, int day) {
        super.setForecast(timeSeries, areaIndex, amedasAreaCodeList, day);
        //天気情報テロップ
        weather = timeSeries.get(0).areas.get(areaIndex).weathers.get(day);
        //風
        if (timeSeries.get(0).areas.get(areaIndex).winds != null) {
            wind = timeSeries.get(0).areas.get(areaIndex).winds.get(day);
        }
        //波
        if (timeSeries.get(0).areas.get(areaIndex).waves != null) {
            wave = timeSeries.get(0).areas.get(areaIndex).waves.get(day);
        }
    }

    @Override
    protected List<Temperature> migrateTemp(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex, List<String> amedasAreaCodeList, int day) {
        return timeSeries.get(2).areas
                .stream()
                .filter(area -> amedasAreaCodeList.stream().anyMatch(s -> s.equals(area.area.code)))
                .map(area -> {
                    Integer min = null;
                    Integer max = null;
                    //最低気温
                    int index =
                            timeSeries.get(2).timeDefines
                                    .indexOf(
                                            OffsetDateTime.of(
                                                    LocalDateTime.of(
                                                            timeDefine.toLocalDate(),
                                                            TemperatureTime.MIN.hour.toLocalTime()
                                                    ),
                                                    ZoneOffset.ofHours(9)
                                            )
                                    );
                    if (index != -1) {
                        min = StringUtils.isNotEmpty(area.temps.get(index)) ? Integer.parseInt(area.temps.get(index)) : null;
                    }

                    //最高気温
                    index =
                            timeSeries.get(2).timeDefines
                                    .indexOf(
                                            OffsetDateTime.of(
                                                    LocalDateTime.of(
                                                            timeDefine.toLocalDate(),
                                                            TemperatureTime.MAX.hour.toLocalTime()
                                                    ),
                                                    ZoneOffset.ofHours(9)
                                            )
                                    );
                    if (index != -1) {
                        max = StringUtils.isNotEmpty(area.temps.get(index)) ? Integer.parseInt(area.temps.get(index)) : null;
                    }
                    return new Temperature(area.area.name, min, max);
                }).collect(Collectors.toList());
    }

    @Override
    protected List<Integer> migratePop(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex) {
        List<Integer> p = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            int index =
                    timeSeries.get(1).timeDefines
                            .indexOf(
                                    OffsetDateTime.of(
                                            LocalDateTime.of(
                                                    timeDefine.toLocalDate(),
                                                    PopTime.values()[i].hour.toLocalTime()
                                            ),
                                            ZoneOffset.ofHours(9)
                                    )
                            );
            if (index != -1) {
                p.add(
                        StringUtils.isNotEmpty(timeSeries.get(1).areas.get(areaIndex).pops.get(index))
                                ? Integer.parseInt(timeSeries.get(1).areas.get(areaIndex).pops.get(index))
                                : null
                );
            } else {
                p.add(null);
            }

        }
        return p;
    }
}
