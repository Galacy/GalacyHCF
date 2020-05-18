package galacy.galacyhcf.models;

import com.alibaba.fastjson.annotation.JSONField;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.providers.Redis;

public class RedisPlayer {
    @JSONField(serialize = false, deserialize = false)
    public String xuid;

    @JSONField(name = "pvptime")
    public int pvptime;

    @JSONField(name = "deathban")
    public int deathban;

    @JSONField(name = "lives")
    public int lives;

    public RedisPlayer(String xuid, int pvptime, int deathban, int lives) {
        this.xuid = xuid;
        this.pvptime = pvptime;
        this.deathban = deathban;
        this.lives = lives;
    }

    public void updateLives(int lives) {
        this.lives = lives;
        update(GalacyHCF.redis);
    }

    public void updateDeathban(int time) {
        deathban = time;
        update(GalacyHCF.redis);
    }

    public void update(Redis redis) {
        redis.setPlayer(xuid, this);
    }
}
