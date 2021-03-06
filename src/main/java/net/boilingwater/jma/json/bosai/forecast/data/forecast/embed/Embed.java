package net.boilingwater.jma.json.bosai.forecast.data.forecast.embed;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;

@ToString
public class Embed {
    public static final String imgBaseUrl = "https://www.jma.go.jp/bosai/forecast/img/";
    public static Embed INSTANCE;
    public Telops telops;
    public AreaFuken areaFuken;


    private Embed() throws IOException {
        telops = new Telops();
        areaFuken = new AreaFuken();
    }

    public static Embed getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new Embed();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    @ToString
    static class InnerAreaFuken {
        public String center;
        public List<String> offices;

    }

    @ToString
    public static class Telops {
        public Map<String, String> dayIconUrls;
        public Map<String, String> nightIconUrls;
        public Map<String, String> ids;
        public Map<String, String> kjTelops;
        public Map<String, String> enTelops;

        Telops() {
            dayIconUrls = new HashMap<String, String>();
            nightIconUrls = new HashMap<String, String>();
            ids = new HashMap<String, String>();
            kjTelops = new HashMap<String, String>();
            enTelops = new HashMap<String, String>();
            try {
                loadJson();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void loadJson() throws IOException {
            Map<String, List<String>> map = new ObjectMapper().readValue(ClassLoader.getSystemClassLoader().getResourceAsStream("Telops.json"), new TypeReference<Map<String, List<String>>>() {
            });
            map.forEach((s, strings) -> {
                dayIconUrls.put(s, strings.get(0));
                nightIconUrls.put(s, strings.get(1));
                ids.put(s, strings.get(2));
                kjTelops.put(s, strings.get(3));
                enTelops.put(s, strings.get(4));
            });
        }
    }

    @ToString
    public static class AreaFuken {
        public Map<String, String> map;

        AreaFuken() {
            try {
                loadJson();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void loadJson() throws IOException {
            List<InnerAreaFuken> list = new ObjectMapper().readValue(ClassLoader.getSystemClassLoader().getResourceAsStream("AreaFuken.json"), new TypeReference<List<InnerAreaFuken>>() {
            });
            map = new HashMap<>();
            list.forEach(innerAreaFuken -> {
                innerAreaFuken.offices.forEach(s -> map.put(s, innerAreaFuken.center));
            });
        }

    }

}

