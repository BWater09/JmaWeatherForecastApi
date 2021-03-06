package net.boilingwater.jma.json.bosai.common.constant;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;

@ToString
public class Area {
    private static Area AREA;

    public Map<String, Center> centers;
    public Map<String, Office> offices;
    public Map<String, Class10> class10s;
    public Map<String, Class15> class15s;
    public Map<String, Class20> class20s;

    private Area() {
    }

    public static Area getArea() {
        if (AREA == null) {
            try {
                AREA = new ObjectMapper().readValue(new URL("http://www.jma.go.jp/bosai/common/const/area.json"), Area.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return AREA;
    }

    @ToString
    public static class Center {
        public String name;
        public String enName;
        public String officeName;
        public List<String> children;
    }

    @ToString
    public static class Office {
        public String name;
        public String enName;
        public String officeName;
        public String parent;
        public List<String> children;
    }

    public static class Class10 {
        public String name;
        public String enName;
        public String parent;
        public List<String> children;
    }

    @ToString
    public static class Class15 {
        public String name;
        public String enName;
        public String parent;
        public List<String> children;
    }

    @ToString
    public static class Class20 {
        public String name;
        public String enName;
        public String kana;
        public String parent;
    }
}
