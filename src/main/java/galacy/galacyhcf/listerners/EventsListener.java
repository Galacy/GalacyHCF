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
import galacy.galacyhcf.utils.Utils;

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
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        if (player instanceof GPlayer)
            ((GPlayer) player).loadData();
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
            if (((GPlayer) player).homeTeleport || ((GPlayer) player).stuckTeleport) {
                if (!((GPlayer) player).moved) {
                    player.sendMessage(Utils.prefix + TextFormat.RED + "Teleportation failed because you moved.");
                    ((GPlayer) player).homeTeleport = false;
                    ((GPlayer) player).stuckTeleport = false;
                    ((GPlayer) player).moved = true;
                }
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
                    checkTeleport((GPlayer) player);

                    checkTeleport((GPlayer) damager);
                }
            } else {
                checkTeleport((GPlayer) player);
            }
        }

    }

    public void checkTeleport(GPlayer player) {
        if (player.homeTeleport || player.stuckTeleport) {
            player.sendMessage(Utils.prefix + TextFormat.RED + "Teleportation failed because you moved.");
            player.homeTeleport = false;
            player.stuckTeleport = false;
            player.moved = true;
            player.fightTime = 30;
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
