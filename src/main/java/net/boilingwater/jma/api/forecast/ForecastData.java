package net.boilingwater.jma.api.forecast;

import lombok.Getter;
import lombok.ToString;
import net.boilingwater.jma.api.forecast.impl.sevendays.SevenDaysForecast;
import net.boilingwater.jma.api.forecast.impl.threedays.ThreeDaysForecast;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.Forecast;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.OverviewForecast;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.OverviewWeek;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.embed.Embed;
import org.apache.commons.lang3.StringUtils;

@Getter
@ToString
public class ForecastData {
    protected boolean isSuccessful;
    protected AreaData areaData;
    protected OverviewForecast overviewForecast;
    protected OverviewWeek overviewWeek;
    protected ThreeDaysForecast threeDaysForecast;
    protected SevenDaysForecast sevenDaysForecast;
    protected Forecast origin;

    private ForecastData() {
        isSuccessful = false;
        overviewForecast = null;
        overviewWeek = null;
        areaData = null;
        threeDaysForecast = null;
        sevenDaysForecast = null;
    }

    public static ForecastData fetchForecastData(AreaType areaType, String class20AreaCode) {
        if (areaType == null || StringUtils.isEmpty(class20AreaCode)) {
            throw new IllegalArgumentException();
        }
        if (areaType == AreaType.OFFICE) {
            throw new UnsupportedOperationException("都道府県・振興局レベルでの天気予報の取得は現在非対応です。");
        }
        return new ForecastData() {{
            areaData = new AreaData(class20AreaCode);
            origin = Forecast.fetchForecast(areaData.getOfficeAreaCode());
            if (origin != null) {
                isSuccessful = true;
                overviewForecast = OverviewForecast.fetchOverviewForecast(areaData.getOfficeAreaCode());
                overviewWeek = OverviewWeek.fetchOverviewWeek(Embed.getInstance().areaFuken.map.get(areaData.getOfficeAreaCode()));
                threeDaysForecast = new ThreeDaysForecast(origin, areaData);
                sevenDaysForecast = new SevenDaysForecast(origin, areaData);
            }
        }};
    }

    @ToString
    public enum Days {
        TODAY, TOMORROW, DAY_AFTER_TOMORROW, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH
    }

    @ToString
    public enum AreaType {
        OFFICE, CLASS20S
    }

}
