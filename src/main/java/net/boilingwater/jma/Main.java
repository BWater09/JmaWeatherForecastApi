package net.boilingwater.jma;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

import net.boilingwater.jma.api.forecast.ForecastData;
import net.boilingwater.jma.api.forecast.utils.AreaService;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.OverviewWeek;
import net.boilingwater.jma.json.bosai.forecast.data.forecast.embed.Embed;
import org.apache.commons.lang3.StringUtils;

public class Main {


    /**
     * デバッグ用
     *
     * @param args 実行時引数
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String center;
        do {
            System.out.println("天気予報を見たい地方を選択してください");
            AreaService.getCenterAreaMap().entrySet().stream().sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getKey()))).forEach(e -> System.out.printf("    %5s : %s\n", e.getKey(), e.getValue()));
            System.out.print(">> ");
            center = StringUtils.trim(scanner.nextLine());
        } while (!AreaService.getCenterAreaMap().containsKey(center));

        String office = "";
        do {
            System.out.println(AreaService.getCenterAreaMap().get(center));
            AreaService.getOfficeAreaMap(center).entrySet().stream().sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getKey()))).forEach(e -> System.out.printf("    %5s : %s\n", e.getKey(), e.getValue()));
            System.out.print(">> ");
            office = StringUtils.trim(scanner.nextLine());
        } while (!AreaService.getOfficeAreaMap(center).containsKey(office));

        String area_code = "";
        do {
            System.out.println(AreaService.getOfficeAreaMap(center).get(office));
            AreaService.getCityAreaMap(office).entrySet().stream().sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getKey()))).forEach(e -> System.out.printf("    %5s : %s\n", e.getKey(), e.getValue()));
            System.out.print(">> ");
            area_code = StringUtils.trim(scanner.nextLine());
        } while (!AreaService.getCityAreaMap(office).containsKey(area_code));

        //デバッグ出力
        ForecastData data = ForecastData.fetchForecastData(ForecastData.AreaType.CLASS20S, area_code);
        System.out.println("天気予報取得エリア");
        System.out.printf("    %8s : %s\n", data.getAreaData().getCenterAreaCode(), data.getAreaData().getCenterAreaName());
        System.out.printf("    %8s : %s\n", data.getAreaData().getOfficeAreaCode(), data.getAreaData().getOfficeAreaName());
        System.out.printf("    %8s : %s\n", data.getAreaData().getClass10AreaCode(), data.getAreaData().getClass10AreaName());
        System.out.printf("    %8s : %s\n", data.getAreaData().getClass15AreaCode(), data.getAreaData().getClass15AreaName());
        System.out.printf("    %8s : %s\n", data.getAreaData().getClass20AreaCode(), data.getAreaData().getClass20AreaName());
        System.out.println();

        System.out.println(data.getThreeDaysForecast().getAreaName() + "の天気  (" + data.getThreeDaysForecast().getPublishingOffice() + " 発表)");
        data.getThreeDaysForecast().getDaysForecasts().forEach(f -> {
            System.out.println(f.getTimeDefine().format(DateTimeFormatter.ofPattern("M/d(EE)")) + "の天気：" + Embed.getInstance().telops.kjTelops.get(f.getWeatherCode()) + "(" + f.getIconUrl().getDayIconUrl() + ", " + f.getIconUrl().getNightIconUrl() + ")");
            System.out.println("    " + "降水確率：" + Arrays.toString(f.getPop().toArray()));
            if (f.getWind() != null)
                System.out.println("    " + "風：" + f.getWind());
            if (f.getWave() != null)
                System.out.println("    " + "波：" + f.getWave());

            System.out.print("    ");
            System.out.println(
                    f.getTemp().stream()
                            .filter(temp -> temp.getMin() != null && temp.getMax() != null)
                            .map(temp -> String.format("%s： %d℃/%d℃", temp.getAmedasName(), temp.getMin(), temp.getMax()))
                            .collect(Collectors.joining(" | "))
            );
        });
        System.out.println();
        System.out.println(data.getSevenDaysForecast().getAreaName() + "の天気  (" + data.getSevenDaysForecast().getPublishingOffice() + " 発表)");
        data.getSevenDaysForecast().getDaysForecasts().forEach(f -> {
            System.out.println(f.getTimeDefine().format(DateTimeFormatter.ofPattern("M/d(EE)")) + "の天気：" + Embed.getInstance().telops.kjTelops.get(f.getWeatherCode()) + "(予報精度 : " + f.getReliability() + ")" + "(" + f.getIconUrl().getDayIconUrl() + ", " + f.getIconUrl().getNightIconUrl() + ")");
            System.out.println("    " + "降水確率：" + Arrays.toString(f.getPop().toArray()));
            System.out.print("    ");
            System.out.println(
                    f.getTemp().stream()
                            .filter(temp -> temp.getMin() != null && temp.getMax() != null)
                            .map(temp -> String.format("%s： %d℃(%d℃～%d℃)/%d℃(%d℃～%d℃)", temp.getAmedasName(), temp.getMin(), temp.getMinLower(), temp.getMinUpper(), temp.getMax(), temp.getMaxLower(), temp.getMaxUpper()))
                            .collect(Collectors.joining(" | "))
            );
        });

    }
}
