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
import galacy.galacyhcf.managers.ClaimProcess;
import galacy.galacyhcf.models.Claim;
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
            if (((GPlayer) player).claimProcess != null) {
                if (((GPlayer) player).claimProcess.state == ClaimProcess.State.waiting) {
                    event.setCancelled(true);
                    if (event.getMessage().equalsIgnoreCase("yes") || event.getMessage().equalsIgnoreCase("accept")) {
                        Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) player).factionId);
                        if (faction.balance < ((GPlayer) player).claimProcess.price) {
                            player.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have enough money in it's balance, claim cancelled.");
                            ((GPlayer) player).claimProcess.state = ClaimProcess.State.finished;
                            ((GPlayer) player).claimProcess = null;
                        } else {
                            if (GalacyHCF.claimsManager.createClaim(((GPlayer) player).claimProcess)) {
                                faction.updateBalance(faction.balance - ((GPlayer) player).claimProcess.price);
                                player.sendMessage(Utils.prefix + TextFormat.GREEN + "Your faction acquired a new claim, congrats!");
                            } else {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "There was an issue with your claim operation!");
                                ((GPlayer) player).claimProcess.state = ClaimProcess.State.finished;
                                ((GPlayer) player).claimProcess = null;
                            }
                        }
                    }
                }
            }
            if (((GPlayer) player).chatType == GPlayer.Chat.Faction) {
                event.setCancelled(true);
                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) player).factionId);
                for (GPlayer member : faction.onlineMembers()) {
                    member.sendMessage(TextFormat.DARK_GREEN + player.getName() + ": " + TextFormat.GREEN + event.getMessage());
                }
            } else {
                if (((GPlayer) player).factionId != 0) {
                    Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) player).factionId);
                    event.setFormat(TextFormat.DARK_GRAY + "[" + TextFormat.GRAY + faction.name + TextFormat.DARK_GRAY + "]" + TextFormat.GRAY + player.getName() + TextFormat.DARK_GRAY + ": " + TextFormat.GRAY + TextFormat.clean(event.getMessage().toLowerCase()));
                } else {
                    event.setFormat(TextFormat.GRAY + player.getName() + TextFormat.DARK_GRAY + ": " + TextFormat.GRAY + TextFormat.clean(event.getMessage().toLowerCase()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            if (((GPlayer) player).claimProcess != null) {
                if (!((GPlayer) player).claimProcess.expired()) {
                    if (player.distance(player.getServer().getDefaultLevel().getSpawnLocation()) < 300) {
                        player.sendMessage(Utils.prefix + TextFormat.RED + "You have to be at least 300 blocks away from spawn to claim.");
                        return;
                    }
                    switch (((GPlayer) player).claimProcess.state) {
                        case firstPosition:
                            Claim claim = GalacyHCF.claimsManager.findClaim((int) event.getBlock().x, (int) event.getBlock().z);
                            if (claim != null) {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "Sorry but this land has already been claimed by " + claim.factionName + ".");
                                break;
                            }
                            ((GPlayer) player).claimProcess.firstPos = event.getBlock().asBlockVector3().asVector3();
                            ((GPlayer) player).claimProcess.state = ClaimProcess.State.secondPosition;
                            player.sendMessage(Utils.prefix + TextFormat.GREEN + "First position set! now click on the next one to complete configuration.");
                            ((GPlayer) player).buildPillar((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, 930, 132);
                            break;
                        case secondPosition:
                            Claim claim2 = GalacyHCF.claimsManager.findClaim((int) event.getBlock().x, (int) event.getBlock().z);
                            if (claim2 != null) {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "Sorry but this land has already been claimed by " + claim2.factionName + ".");
                                break;
                            }
                            if (((GPlayer) player).claimProcess.firstPos.distance(event.getBlock().asBlockVector3().asVector3()) < 4) {
                                System.out.println("Small:" + ((GPlayer) player).claimProcess.firstPos.distance(event.getBlock().asBlockVector3().asVector3()));
                                player.sendMessage(Utils.prefix + TextFormat.RED + "Sorry but your claim is too small! try again.");
                                break;
                            }
                            if (((GPlayer) player).claimProcess.firstPos.distance(event.getBlock().asBlockVector3().asVector3()) > 100) {
                                System.out.println("Big:" + ((GPlayer) player).claimProcess.firstPos.distance(event.getBlock().asBlockVector3().asVector3()));
                                player.sendMessage(Utils.prefix + TextFormat.RED + "Sorry but your claim is too big! try again.");
                                break;
                            }
                            ((GPlayer) player).claimProcess.secondPos = event.getBlock().asBlockVector3().asVector3();
                            ((GPlayer) player).claimProcess.state = ClaimProcess.State.waiting;
                            player.sendMessage(Utils.prefix + TextFormat.GREEN + "Second position set!");
                            ((GPlayer) player).buildPillar((int) event.getBlock().x, (int) event.getBlock().y, (int) event.getBlock().z, 930, 132);
                            GalacyHCF.claimsManager.tryClaiming(((GPlayer) player).claimProcess);
                            break;
                    }
                } else {
                    ((GPlayer) player).claimProcess = null;
                }
            }
        }
    }
}
