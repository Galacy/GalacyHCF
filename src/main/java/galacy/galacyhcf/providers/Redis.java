package galacy.galacyhcf.providers;

import cn.nukkit.utils.TextFormat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.RedisPlayer;
import redis.clients.jedis.Jedis;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Redis {

    public Jedis jedis;

    public Redis(String address) {
        jedis = new Jedis(address);
        jedis.connect();
        if (jedis.isConnected()) {
            GalacyHCF.instance.getLogger().info(TextFormat.AQUA + "[Redis] connection initialized.");
        } else {
            GalacyHCF.instance.getLogger().critical(TextFormat.AQUA + "[Redis] couldn't connect.");
        }
    }

    public RedisPlayer getPlayerByName(String name) {
        try {
            ResultSet result = GalacyHCF.mysql.query(SQLStatements.playerByName);
            if (result.next()) {
                return getPlayer(result.getString("xuid"));
            } else return null;
        } catch (SQLException e) {
            GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues removing members after disband: " + e);
            return null;
        }
    }

    public RedisPlayer getPlayer(String xuid) {
        if (jedis.exists(xuid)) {
            JSONObject json = JSON.parseObject(jedis.get(xuid));

            return new RedisPlayer(xuid, json.getIntValue("pvptime"), json.getIntValue("deathban"), json.getIntValue("lives"), json.getIntValue("kills"), json.getIntValue("deaths"),
                    json.getIntValue("diamonds"), json.getIntValue("gold"), json.getIntValue("iron"), json.getIntValue("redstone"), json.getIntValue("lapis"));
        } else {
            RedisPlayer player = new RedisPlayer(xuid, 15 * 60, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            setPlayer(xuid, player);

            return player;
        }
    }

    public void setPlayer(String xuid, RedisPlayer player) {
        jedis.set(xuid, JSON.toJSONString(player));
    }

    public void close() {
        if (jedis.isConnected()) jedis.disconnect();
    }
}
