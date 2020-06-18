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

    @JSONField(name = "kills")
    public int kills;

    @JSONField(name = "deaths")
    public int deaths;

    @JSONField(name = "diamonds")
    public int diamonds;

    @JSONField(name = "gold")
    public int gold;

    @JSONField(name = "iron")
    public int iron;

    @JSONField(name = "redstone")
    public int redstone;

    @JSONField(name = "lapis")
    public int lapis;

    @JSONField(name = "galacy_kit")
    public long galacyKit;

    @JSONField(name = "diamond_kit")
    public long diamondKit;

    @JSONField(name = "rogue_kit")
    public long rogueKit;

    @JSONField(name = "bard_kit")
    public long bardKit;

    @JSONField(name = "miner_kit")
    public long minerKit;

    public RedisPlayer(String xuid, int pvptime, int deathban, int lives, int kills, int deaths,
                       int diamonds, int gold, int iron, int redstone, int lapis,
                       long galacyKit, long diamondKit, long rogueKit, long bardKit, long minerKit) {
        this.xuid = xuid;
        this.pvptime = pvptime;
        this.deathban = deathban;
        this.lives = lives;
        this.kills = kills;
        this.deaths = deaths;
        this.diamonds = diamonds;
        this.gold = gold;
        this.iron = iron;
        this.redstone = redstone;
        this.lapis = lapis;
        this.galacyKit = galacyKit;
        this.diamondKit = diamondKit;
        this.rogueKit = rogueKit;
        this.bardKit = bardKit;
        this.minerKit = minerKit;
    }

    public void updateLives(int lives) {
        this.lives = lives;
        update(GalacyHCF.redis);
    }

    public void updateDeathban(int time) {
        deathban = time;
        update(GalacyHCF.redis);
    }

    public void addKill() {
        kills++;
        update(GalacyHCF.redis);
    }

    public void addDeath() {
        deaths++;
        update(GalacyHCF.redis);
    }

    public void update(Redis redis) {
        redis.setPlayer(xuid, this);
    }
}
