package net.boilingwater.jma.api.forecast;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.Forecast;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.embed.Embed;

/**
 * 一日分の天気予報情報を示す抽象クラス
 */
@Getter
@ToString
public abstract class AbstractOneDayWeatherForecast {
    protected OffsetDateTime timeDefine;
    protected String weatherCode;
    protected List<Integer> pop;
    protected List<Temperature> temp;
    protected IconUrl iconUrl;

    public AbstractOneDayWeatherForecast(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex, List<String> amedasAreaCodeList, int day) {
        timeDefine = OffsetDateTime.MIN;
        weatherCode = "";
        pop = new ArrayList<>(4);
        temp = new LinkedList<>();
        setForecast(timeSeries, areaIndex, amedasAreaCodeList, day);
    }

    /**
     * 自分自身に天気予報を設定する
     *
     * @param timeSeries         {@link Forecast.InnerForecast.TimeSeries} API取得結果の一部
     * @param areaIndex          {@link AbstractDaysWeatherForecast}.getIndex()
     * @param amedasAreaCodeList 対象のアメダスコードのリスト
     * @param day                対象の日付(今日を0とする)
     */
    protected void setForecast(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex, List<String> amedasAreaCodeList, int day) {
        timeDefine = timeSeries.get(0).timeDefines.get(day);
        weatherCode = timeSeries.get(0).areas.get(areaIndex).weatherCodes.get(day);
        pop = migratePop(timeSeries, areaIndex);
        temp = migrateTemp(timeSeries, areaIndex, amedasAreaCodeList, day);
        iconUrl = new IconUrl(weatherCode);
    }

    /**
     * 気温情報を取得するメソッド
     *
     * @param timeSeries         {@link Forecast.InnerForecast.TimeSeries} API取得結果の一部
     * @param areaIndex          {@link AbstractDaysWeatherForecast}.getIndex()
     * @param amedasAreaCodeList 対象のアメダスコードのリスト
     * @param day                対象の日付(今日を0とする)
     * @return {@link Temperature} 気温情報のリスト
     */
    protected abstract List<Temperature> migrateTemp(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex, List<String> amedasAreaCodeList, int day);

    /**
     * 降水確率情報を取得するメソッド
     *
     * @param timeSeries {@link Forecast.InnerForecast.TimeSeries} API取得結果の一部
     * @param areaIndex  {@link AbstractDaysWeatherForecast}.getIndex()
     * @return {@link Temperature} 降水確率情報のリスト
     */
    protected abstract List<Integer> migratePop(List<Forecast.InnerForecast.TimeSeries> timeSeries, int areaIndex);

    /**
     * 降水確率情報の時刻を定義する列挙型
     */
    protected enum PopTime {
        H00(OffsetTime.of(LocalTime.of(0, 0), ZoneOffset.ofHours(9))),
        H06(OffsetTime.of(LocalTime.of(6, 0), ZoneOffset.ofHours(9))),
        H12(OffsetTime.of(LocalTime.of(12, 0), ZoneOffset.ofHours(9))),
        H18(OffsetTime.of(LocalTime.of(18, 0), ZoneOffset.ofHours(9)));
        public final OffsetTime hour;

        PopTime(OffsetTime hour) {
            this.hour = hour;
        }
    }

    /**
     * 気温情報の時刻を定義する列挙型
     */
    protected enum TemperatureTime {
        MIN(OffsetTime.of(LocalTime.of(0, 0), ZoneOffset.ofHours(9))),
        MAX(OffsetTime.of(LocalTime.of(9, 0), ZoneOffset.ofHours(9)));
        public final OffsetTime hour;

        TemperatureTime(OffsetTime hour) {
            this.hour = hour;
        }
    }

    /**
     * 一日分の気温情報を示すクラス
     * 3日間予報ではUpper,Lowerはnullになります
     */
    @Getter
    @AllArgsConstructor
    @ToString
    public static class Temperature {
        protected String amedasName;
        protected Integer min;
        protected Integer minLower;
        protected Integer minUpper;
        protected Integer max;
        protected Integer maxLower;
        protected Integer maxUpper;

        public Temperature(String amedasName, Integer min, Integer max) {
            this.amedasName = amedasName;
            this.min = min;
            this.max = max;
        }
    }

    /**
     * 天気を示すアイコンのURLを定義するクラス
     */
    @Getter
    @ToString
    public static class IconUrl {
        private URL dayIconUrl;
        private URL nightIconUrl;

        public IconUrl(String weatherCode) {
            try {
                dayIconUrl = new URL(Embed.imgBaseUrl + Embed.getInstance().telops.dayIconUrls.get(weatherCode));
                nightIconUrl = new URL(Embed.imgBaseUrl + Embed.getInstance().telops.nightIconUrls.get(weatherCode));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                dayIconUrl = null;
                nightIconUrl = null;
            }
        }
    }
}