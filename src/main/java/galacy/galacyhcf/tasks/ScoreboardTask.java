package galacy.galacyhcf.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.scoreboardapi.ScoreboardAPI;
import galacy.galacyhcf.utils.Utils;

import java.util.Date;

public class ScoreboardTask extends PluginTask<GalacyHCF> {

    public ScoreboardTask(GalacyHCF plugin) {
        super(plugin);
    }

    @Override
    public void onRun(int i) {
        String sotwTime = "";
        if (GalacyHCF.sotwTask != null && GalacyHCF.sotwTask.started)
            sotwTime = GalacyHCF.sotwTask.time > 60 * 60 ? Utils.timerHour.format(new Date(GalacyHCF.sotwTask.time * 1000L)) : Utils.timerMinutes.format(new Date(GalacyHCF.sotwTask.time * 1000L));
        for (Player player : GalacyHCF.instance.getServer().getOnlinePlayers().values()) {
            if (player instanceof GPlayer) {
                player.sendPopup(player.getFloorX() + ":" + player.getFloorY() + ":" + player.getFloorZ());
                if (((GPlayer) player).sb == null)
                    ((GPlayer) player).sb = new ScoreboardAPI.Companion.Builder().build();
                else ((GPlayer) player).sb.removePlayer(player);
                ((GPlayer) player).sb.resetAllScores();
                ((GPlayer) player).sb.setDisplayName(String.valueOf(TextFormat.BOLD) + TextFormat.GOLD + "GalacyHCF" + TextFormat.RESET + TextFormat.GRAY + " (Map 1)");
                ((GPlayer) player).sb.setScore(1, TextFormat.GRAY + "-----------------------", 1);
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
                    i++;
                }
                if (GalacyHCF.sotwTask != null && GalacyHCF.sotwTask.started) {
                    ((GPlayer) player).sb.setScore(i, TextFormat.BOLD + String.valueOf(TextFormat.DARK_BLUE) + "SoTW: " + TextFormat.RESET + TextFormat.GRAY + sotwTime, i);
                    i++;
                }
                if (((GPlayer) player).redisData().pvptime > 0) {
                    ((GPlayer) player).sb.setScore(i, TextFormat.BOLD + String.valueOf(TextFormat.RED) + "PvP: " + TextFormat.RESET + TextFormat.GRAY + Utils.timerMinutes.format(new Date(((GPlayer) player).redisData().pvptime * 1000L)), i);
                }

                ((GPlayer) player).sb.addPlayer(player);
            }
        }
    }
}
