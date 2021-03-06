package net.boilingwater.jma.api.forecast;

import lombok.Getter;
import lombok.ToString;
import net.boilingwater.jma.json.bosai.common.constant.Area;

@Getter
@ToString
public class AreaData {
    private final String centerAreaName;
    private final String centerAreaCode;
    private final String officeAreaName;
    private final String officeAreaCode;
    private final String class10AreaName;
    private final String class10AreaCode;
    private final String class15AreaName;
    private final String class15AreaCode;
    private final String class20AreaName;
    private final String class20AreaCode;

    public AreaData(String class20AreaCode) {
        Area.Class20 c20 = Area.getArea().class20s.get(class20AreaCode);
        Area.Class15 c15 = Area.getArea().class15s.get(c20.parent);
        Area.Class10 c10 = Area.getArea().class10s.get(c15.parent);
        Area.Office o = Area.getArea().offices.get(c10.parent);
        Area.Center c = Area.getArea().centers.get(o.parent);

        this.centerAreaName = c.name;
        this.centerAreaCode = o.parent;
        this.officeAreaName = o.name;
        this.officeAreaCode = c10.parent;
        this.class10AreaName = c10.name;
        this.class10AreaCode = c15.parent;
        this.class15AreaName = c15.name;
        this.class15AreaCode = c20.parent;
        this.class20AreaName = c20.name;
        this.class20AreaCode = class20AreaCode;
    }
}
