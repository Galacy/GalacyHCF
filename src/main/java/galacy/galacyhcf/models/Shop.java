package galacy.galacyhcf.models;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class Shop {
    @JSONField(serialize = false)
    public static final int sell = 0;

    @JSONField(serialize = false)
    public static final int buy = 1;

    @JSONField(name = "name")
    public String name;

    @JSONField(name = "item_id")
    public int itemId;

    @JSONField(name = "type")
    public int type;

    @JSONField(name = "amount")
    public int amount;

    @JSONField(name = "price")
    public int price;

    @JSONField(name = "position")
    public String position;

    public Shop(String name, int itemId, int type, int amount, int price, String position) {
        this.name = name;
        this.itemId = itemId;
        this.type = type;
        this.amount = amount;
        this.price = price;
        this.position = position;
    }

    public Shop(JSONObject json) {
        name = json.getString("name");
        itemId = json.getIntValue("item_id");
        type = json.getIntValue("type");
        amount = json.getIntValue("amount");
        price = json.getIntValue("price");
        position = json.getString("position");
    }
}
