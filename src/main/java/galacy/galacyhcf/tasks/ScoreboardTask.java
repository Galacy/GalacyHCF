package galacy.galacyhcf.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;

public class ScoreboardTask extends PluginTask<GalacyHCF> {

    public ScoreboardTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        for (Player player : GalacyHCF.instance.getServer().getOnlinePlayers().values()) {
            if (player instanceof GPlayer) {
                ((GPlayer) player).sb.resetAllScores();
                ((GPlayer) player).sb.setScore(1, TextFormat.GRAY + "------------------", 1);
                i = 2;
                if (((GPlayer) player).homeTeleport) {
                    ((GPlayer) player).sb.setScore(i, TextFormat.BOLD + String.valueOf(TextFormat.YELLOW) + "Home: " + TextFormat.RESET + TextFormat.GRAY + ((GPlayer) player).teleportTime, i);
                    i++;
                }
                if (((GPlayer) player).stuckTeleport) {
                    ((GPlayer) player).sb.setScore(i, TextFormat.BOLD + String.valueOf(TextFormat.YELLOW) + "Stuck: " + TextFormat.RESET + TextFormat.GRAY + ((GPlayer) player).teleportTime, i);
                    i++;
                }
                if (((GPlayer) player).fightTime != 0) {
                    ((GPlayer) player).sb.setScore(i, TextFormat.BOLD + String.valueOf(TextFormat.RED) + "CombatTag: " + TextFormat.RESET + TextFormat.GRAY + ((GPlayer) player).fightTime, i);
                }

                ((GPlayer) player).sb.addPlayer(player);
            }
        }
    }
}
