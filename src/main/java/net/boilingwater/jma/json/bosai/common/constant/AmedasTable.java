package net.boilingwater.jma.json.bosai.common.constant;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;

@ToString
public class AmedasTable extends HashMap<String, AmedasTable.InnerAmedasTable> {
    public static AmedasTable INSTANCE;

    private AmedasTable() {
    }

    public static AmedasTable getAmedasTable() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new ObjectMapper().readValue(new URL("https://www.jma.go.jp/bosai/amedas/const/amedastable.json"), AmedasTable.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    @ToString
    public static class InnerAmedasTable {
        public String type;
        public String elems;
        public List<Double> lat;
        public List<Double> lon;
        public Integer alt;
        public String kjName;
        public String knName;
        public String enName;
    }
}
