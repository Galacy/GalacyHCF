package galacy.galacyhcf.listerners;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.models.Faction;
import galacy.galacyhcf.models.GPlayer;
import galacy.galacyhcf.providers.SQLStatements;
import galacy.galacyhcf.scoreboardapi.ScoreboardAPI;
import galacy.galacyhcf.utils.Utils;

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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
                } catch (SQLException e) {
                    GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues changing player username: " + e);
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            ((GPlayer) player).sb = new ScoreboardAPI.Companion.Builder().build();
            ((GPlayer) player).sb.setDisplayName(Utils.prefix + TextFormat.RESET + TextFormat.GRAY + " (Map 0.5)");
            ((GPlayer) player).sb.setScore(1, TextFormat.GRAY + "------------------", 1);

            ((GPlayer) player).sb.addPlayer(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            if (((GPlayer) player).fightTime != 0) {
                player.kill();
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            if (!((GPlayer) player).moved) {
                player.sendMessage(Utils.prefix + TextFormat.RED + "Teleportation failed because you moved.");
                ((GPlayer) player).homeTeleport = false;
                ((GPlayer) player).stuckTeleport = false;
                ((GPlayer) player).moved = true;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity player = event.getEntity();
        if (player instanceof GPlayer) {
            Entity damager = event.getDamager();
            if (damager instanceof GPlayer) {
                if (((GPlayer) player).factionId == ((GPlayer) damager).factionId) {
                    event.setCancelled(true);
                    ((GPlayer) damager).sendMessage(TextFormat.YELLOW + "You can not hurt " + TextFormat.GREEN + player.getName());
                } else {
                    ((GPlayer) player).sendMessage(Utils.prefix + TextFormat.RED + "Teleportation failed because you moved.");
                    ((GPlayer) player).homeTeleport = false;
                    ((GPlayer) player).stuckTeleport = false;
                    ((GPlayer) player).moved = true;
                    ((GPlayer) player).fightTime = 30;

                    ((GPlayer) damager).sendMessage(Utils.prefix + TextFormat.RED + "Teleportation failed because you moved.");
                    ((GPlayer) damager).homeTeleport = false;
                    ((GPlayer) damager).stuckTeleport = false;
                    ((GPlayer) damager).moved = true;
                    ((GPlayer) damager).fightTime = 30;
                }
            } else {
                ((GPlayer) player).sendMessage(Utils.prefix + TextFormat.RED + "Teleportation failed because you moved.");
                ((GPlayer) player).homeTeleport = false;
                ((GPlayer) player).stuckTeleport = false;
                ((GPlayer) player).moved = true;
                ((GPlayer) player).fightTime = 30;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            if (((GPlayer) player).chatType == GPlayer.Chat.Faction) {
                event.setCancelled(true);
                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) player).factionId);
                for (GPlayer member : faction.onlineMembers()) {
                    member.sendMessage(TextFormat.DARK_GREEN + player.getName() + ": " + TextFormat.GREEN + event.getMessage());
                }
            }
        }
    }
}
