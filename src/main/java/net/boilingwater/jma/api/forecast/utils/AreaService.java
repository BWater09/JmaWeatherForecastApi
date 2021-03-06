package net.boilingwater.jma.api.forecast.utils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import net.boilingwater.jma.json.bosai.common.constant.Area;

public class AreaService {
    public static Map<String, String> getCenterAreaMap() {
        return Area.getArea().centers.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, a -> a.getValue().name));
    }

    public static Map<String, String> getOfficeAreaMap(String centerAreaCode) {
        Optional<Map.Entry<String, Area.Center>> centerOptional = Area.getArea().centers.entrySet()
                .stream()
                .filter(a -> a.getKey().equals(centerAreaCode))
                .findFirst();
        return centerOptional.map(stringCenterEntry -> stringCenterEntry.getValue().children
                .stream()
                .collect(Collectors.toMap(c -> c, c -> Area.getArea().offices.get(c).name))).orElse(Collections.emptyMap());
    }

    public static Map<String, String> getCityAreaMap(String officeAreaCode) {
        Optional<Map.Entry<String, Area.Office>> officeOptional = Area.getArea().offices.entrySet()
                .stream()
                .filter(a -> a.getKey().equals(officeAreaCode))
                .findFirst();
        return officeOptional.map(stringOfficeEntry -> stringOfficeEntry.getValue()
                .children.stream()
                .flatMap(s -> Area.getArea().class10s.entrySet().stream().filter(c10 -> s.equals(c10.getKey())).flatMap(entry -> entry.getValue().children.stream()))
                .flatMap(c15s -> Area.getArea().class15s.entrySet().stream().filter(c15 -> c15s.equals(c15.getKey())).flatMap(entry -> entry.getValue().children.stream()))
                .collect(Collectors.toMap(c20s -> c20s, c20s -> Area.getArea().class20s.get(c20s).name))).orElse(Collections.emptyMap());
    }
}
