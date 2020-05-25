package galacy.galacyhcf.managers;

import cn.nukkit.math.Vector3;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import galacy.galacyhcf.models.Shop;
import galacy.galacyhcf.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class ShopsManager {
    public File file;
    public ArrayList<Shop> shops = new ArrayList<>();

    public ShopsManager(String file) {
        this.file = new File(file);
        if (Files.exists(this.file.toPath())) {
            try {
                Object obj = JSON.parse(Utils.readFileAsString(file));
                JSONArray jsonObjects = (JSONArray) obj;

                for (Object o : jsonObjects) {
                    shops.add(new Shop((JSONObject) o));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                System.out.println(this.file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        try {
            FileWriter output = new FileWriter(file, false);
            output.write(JSON.toJSONString(shops));
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addShop(Shop shop) {
        shops.add(shop);
        update();
    }

    public Shop shop(Vector3 pos) {
        String formattedPos = pos.getFloorX() + ":" + pos.getFloorY() + ":" + pos.getFloorZ();

        for (Shop shop : shops) {
            if (shop.position.equals(formattedPos)) return shop;
        }

        return null;
    }

    public void removeShop(Shop shop) {
        shops.remove(shop);
        update();
    }
}
