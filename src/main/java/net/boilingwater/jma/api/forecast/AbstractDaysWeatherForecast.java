package net.boilingwater.jma.api.forecast;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.Forecast;

/**
 * 数日分の天気予報を定義するクラス
 */
@Getter
@ToString
public abstract class AbstractDaysWeatherForecast {
    /**
     * 予報気象台
     */
    protected String publishingOffice;
    /**
     * 予報エリア名
     */
    protected String areaName;
    /**
     * 予報発表日時
     */
    protected OffsetDateTime reportDateTime;
    /**
     * 天気予報
     */
    protected List<AbstractOneDayWeatherForecast> dayForecasts;

    public AbstractDaysWeatherForecast(Forecast f, AreaData areaData, int dayCount) {
        publishingOffice = "";
        areaName = "";
        reportDateTime = OffsetDateTime.MIN;
        dayForecasts = new ArrayList<AbstractOneDayWeatherForecast>(dayCount);
        setForecast(f, areaData, dayCount);
    }

    /**
     * Forecast.InnerForecast.TimeSeries.Areaから対象の地域のINDEXを取得する
     *
     * @param areas {@link Forecast.InnerForecast.TimeSeries.Area} 地域ごとの天気予報データ
     * @param list  検索対象のareaCode
     * @return 出力対象のINDEX
     */
    protected static int getIndex(List<Forecast.InnerForecast.TimeSeries.Area> areas, List<String> list) {
        for (int i = 0; i < areas.size(); i++) {
            if (list.contains(areas.get(i).area.code)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Forecast.InnerForecast.TimeSeries.Areaから対象の地域のINDEXを取得する
     *
     * @param areas    {@link Forecast.InnerForecast.TimeSeries.Area} 地域ごとの天気予報データ
     * @param areaCode 検索対象のareaCode
     * @return 出力対象のINDEX
     */
    protected static int getIndex(List<Forecast.InnerForecast.TimeSeries.Area> areas, String areaCode) {
        return getIndex(areas, new ArrayList<String>() {{
            add(areaCode);
        }});
    }

    /**
     * 自分自身に天気予報情報を設定する
     *
     * @param f        {@link Forecast} API結果
     * @param areaData {@link AreaData} 地域情報
     * @param dayCount 天気予報の日数
     */
    protected abstract void setForecast(Forecast f, AreaData areaData, int dayCount);
}
