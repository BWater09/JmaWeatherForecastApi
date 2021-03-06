package net.boilingwater.jma.json.bosai.common.constant;

import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.ToString;

@ToString
public class Anniversary extends LinkedList<OffsetDateTime> {
    private static Anniversary INSTANCE;

    private Anniversary() {

    }

    public static Anniversary getAnniversary() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new ObjectMapper().registerModule(new JavaTimeModule())
                        .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                        .readValue(new URL("https://www.jma.go.jp/bosai/forecast/const/anniversary.json"), Anniversary.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }
}
