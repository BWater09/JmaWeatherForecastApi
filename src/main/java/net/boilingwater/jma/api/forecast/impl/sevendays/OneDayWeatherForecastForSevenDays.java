package net.boilingwater.jma.api.forecast.impl.sevendays;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;
import net.boilingwater.jma.api.forecast.AbstractOneDayWeatherForecast;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.Forecast;
import org.apache.commons.lang3.StringUtils;

/**
 * 1週間予報で1日分の天気予報を示すクラス
 */
@Getter
@ToString
public class OneDayWeatherForecastForSevenDays extends AbstractOneDayWeatherForecast {
    protected String reliability;

    public OneDayWeatherForecastForSevenDays(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex, List<String> amedasAreaCodeList, int day) {
        super(timeSeries, areaIndex, amedasAreaCodeList, day);
    }

    @Override
    public void setForecast(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex, List<String> amedasAreaCodeList, int day) {
        super.setForecast(timeSeries, areaIndex, amedasAreaCodeList, day);
        //信頼度
        reliability = timeSeries.get(0).areas.get(areaIndex).reliabilities.get(day);
    }

    @Override
    protected List<Temperature> migrateTemp(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex, List<String> amedasAreaCodeList, int day) {
        Forecast.InnerForecast.TimeSeries.Area area = timeSeries.get(1).areas.get(0);
        Integer min = null, minLower = null, minUpper = null, max = null, maxLower = null, maxUpper = null;
        int index =
                timeSeries.get(1).timeDefines
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
            min = StringUtils.isNotEmpty(area.tempsMin.get(index)) ? Integer.parseInt(area.tempsMin.get(index)) : null;
            minLower = StringUtils.isNotEmpty(area.tempsMinLower.get(index)) ? Integer.parseInt(area.tempsMinLower.get(index)) : null;
            minUpper = StringUtils.isNotEmpty(area.tempsMinUpper.get(index)) ? Integer.parseInt(area.tempsMinUpper.get(index)) : null;
            max = StringUtils.isNotEmpty(area.tempsMax.get(index)) ? Integer.parseInt(area.tempsMax.get(index)) : null;
            maxLower = StringUtils.isNotEmpty(area.tempsMaxLower.get(index)) ? Integer.parseInt(area.tempsMaxLower.get(index)) : null;
            maxUpper = StringUtils.isNotEmpty(area.tempsMaxUpper.get(index)) ? Integer.parseInt(area.tempsMaxUpper.get(index)) : null;
        }
        Temperature t = new Temperature(area.area.name, min, minLower, minUpper, max, maxLower, maxUpper);
        return new LinkedList<Temperature>() {{
            add(t);
        }};
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
                        StringUtils.isNotEmpty(timeSeries.get(0).areas.get(areaIndex).pops.get(index))
                                ? Integer.parseInt(timeSeries.get(0).areas.get(areaIndex).pops.get(index))
                                : null
                );
            }
        }
        return p;
    }
}
