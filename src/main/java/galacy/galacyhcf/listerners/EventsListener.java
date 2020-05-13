package galacy.galacyhcf.listerners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.*;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.providers.SQLStatements;
import galacy.galacyhcf.utils.Utils;
import galacy.galacyhcf.scoreboardapi.ScoreboardAPI;
import galacy.galacyhcf.scoreboardapi.scoreboard.*;

import java.sql.SQLException;

public class EventsListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        // TODO: Make cool down time between command executions.
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreation(PlayerCreationEvent event) {
        event.setPlayerClass(GPlayer.class);
    }

    public void onLogin(PlayerLoginEvent event) {
        Player p = event.getPlayer();
        if (p instanceof GPlayer) {
            ((GPlayer) p).loadData();
            if (!p.getName().equals(((GPlayer) p).dbUsername)) {
                try {
                    String currentTime = Utils.dateFormat.format(new java.util.Date());

                    GalacyHCF.mysql.exec(SQLStatements.updatePlayerUsernameById.
                            replace("$updated_at", currentTime).
                            replace("$xuid", p.getLoginChainData().getXUID()).
                            replace("$username", p.getName()));
                    GalacyHCF.instance.getLogger().info(((GPlayer) p).dbUsername + " has changed his name to " + p.getName());
                }  catch (SQLException e) {
                    GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues changing player username: " + e);
                }
            }
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer() instanceof GPlayer) {
            SimpleScoreboard scoreboard = new ScoreboardAPI.Companion.Builder().build();
            assert Utils.prefix != null;
            scoreboard.setDisplayName(Utils.prefix + TextFormat.RESET + TextFormat.BLACK + " | Map #1");
            scoreboard.setScore(1, TextFormat.GRAY + "----------------", 1);
            scoreboard.setScore(2, "yup", 2);

            scoreboard.addPlayer(event.getPlayer());

            scoreboard.setScore(3, "updated!", 3);
        }

        //FormWindowSimple form = new FormWindowSimple("Test title", "well this is supposed to be the paragraph.");
        //event.getPlayer().showFormWindow(form);
    }
}
