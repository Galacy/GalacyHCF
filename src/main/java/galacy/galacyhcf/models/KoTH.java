package galacy.galacyhcf.models;

import com.alibaba.fastjson.annotation.JSONField;
import galacy.galacyhcf.borders.Border;

public class KoTH {
    @JSONField(serialize = false)
    public Border captureZone;

    @JSONField(serialize = false)
    public Border border;

    @JSONField(name = "name")
    public String name;

    @JSONField(name = "first_position")
    public String firstPosition;

    @JSONField(name = "second_position")
    public String secondPosition;

    public KoTH() {

    }
}
