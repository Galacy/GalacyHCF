package galacy.galacyhcf.managers;

import cn.nukkit.math.Vector3;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.utils.Utils;

public class ClaimProcess {
    public Vector3 firstPos;
    public Vector3 secondPos;
    public int type;
    public long expireAt;
    public GPlayer player;
    public State state = State.firstPosition;
    public int price;

    public ClaimProcess(int type, GPlayer player) {
        this.type = type;
        this.player = player;
        expireAt = (System.currentTimeMillis() / 1000L) + 60;
        GalacyHCF.instance.getServer().getScheduler().scheduleDelayedTask(GalacyHCF.instance, () -> {
            if (state != State.finished) {
                if (player.isOnline()) {
                    player.claimProcess = null;
                    player.sendMessage(Utils.prefix + TextFormat.YELLOW + "Claiming process just expired!");
                }
            }
        }, 20 * 60);
    }

    public boolean expired() {
        return (System.currentTimeMillis() / 1000L) >= expireAt;
    }

    public enum State {
        firstPosition,
        secondPosition,
        waiting,
        finished
    }
}
