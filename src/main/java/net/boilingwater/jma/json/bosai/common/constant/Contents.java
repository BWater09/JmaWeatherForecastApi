package net.boilingwater.jma.json.bosai.common.constant;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.ToString;

@ToString
public class Contents {
    private static Contents CONTENTS;

    public List<Group> groups;
    public List<Content> contents;

    private Contents() {
    }

    public static Contents getContents() {
        if (CONTENTS == null) {
            try {
                CONTENTS = new ObjectMapper().readValue(new URL("http://www.jma.go.jp/bosai/common/const/contents.json"), Contents.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return CONTENTS;
    }

    @ToString
    public static class Group {
        public String type;
        public String name;
        public String enName;
        public Integer priority;
    }

    @ToString
    public static class Content {
        public String id;
        public String icon;
        public String type;
        public String context;
        public String name;
        public String enName;
        public String kana;
        public List<String> keywords;
        public String url;
        public String enUrl;
        public String mapValue;
        public Integer priority;
        public List<String> realm;
        public Map<String, String> areaHandover;
    }
}
