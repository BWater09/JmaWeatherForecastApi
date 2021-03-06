# JmaWeatherForecastApi

[気象庁の天気予報ページ](https://www.jma.go.jp/bosai/forecast/)から気象予報を取得するJava用APIです。

## 機能

現在、市町村レベルでの情報取得にのみ対応しています。

今後、都道府県・振興局レベル/全国レベルでの取得に対応させる予定です。

## 使い方

```java
//area_codeは気象庁天気予報サイトのURL GETパラメータを参照
//例）富山県 黒部市
ForecastData fd = ForecastData.fetchForecastData(ForecastData.AreaType.CLASS20S/*現在はclass20sのみ対応*/, "1620700"/*area_code*/);
```

で取得できます。

気象庁の[利用規約](https://www.jma.go.jp/jma/kishou/info/coment.html)に則ってご使用ください。
