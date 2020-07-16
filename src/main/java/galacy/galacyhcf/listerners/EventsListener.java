package galacy.galacyhcf.listerners;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemCompass;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.ItemSteak;
import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DummyBossBar;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.entities.LogoutVillager;
import galacy.galacyhcf.managers.BorderFace;
import galacy.galacyhcf.managers.ClaimProcess;
import galacy.galacyhcf.managers.KitsManager;
import galacy.galacyhcf.managers.SetsManager;
import galacy.galacyhcf.models.*;
import galacy.galacyhcf.utils.Utils;

import java.util.Date;

public class EventsListener implements Listener {

    @EventHandler
    public void on(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        if (args[0].equals("/me")) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(PlayerCreationEvent event) {
        event.setPlayerClass(GPlayer.class);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerJoinEvent event) {
        event.setJoinMessage("");
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            if (((GPlayer) player).firstTime)
                player.getInventory().addItem(new ItemSteak(0, 64), new ItemCompass(0, 1));
            ((GPlayer) player).applySet(false);

            player.getServer().getScheduler().scheduleDelayedTask(GalacyHCF.instance, () -> {
                ((GPlayer) player).updateNametag();
                DummyBossBar bar = new DummyBossBar.Builder(player).text(TextFormat.BOLD + "" + TextFormat.GRAY + "You're playing on " + TextFormat.GOLD + "Galacy.me | 19132").color(BlockColor.GRAY_BLOCK_COLOR).build();
                player.createBossBar(bar);
            }, 20);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            ((GPlayer) player).loadData();
            if (((GPlayer) player).rank == GPlayer.ADMIN || ((GPlayer) player).rank == GPlayer.MOD) {
                player.getServer().getPluginManager().subscribeToPermission("nukkit.command.ban.player", player);
                player.getServer().getPluginManager().subscribeToPermission("nukkit.command.gamemode.spectator", player);
                player.getServer().getPluginManager().subscribeToPermission("nukkit.command.gamemode.survival", player);
                player.getServer().getPluginManager().subscribeToPermission("nukkit.command.kick", player);
                player.getServer().getPluginManager().subscribeToPermission("nukkit.command.unban.player", player);
                player.getServer().getPluginManager().subscribeToPermission("nukkit.command.teleport", player);
                player.recalculatePermissions();
            }
            RedisPlayer data = ((GPlayer) player).redisData();
            if (player.isOp()) return;
            if ((System.currentTimeMillis() / 1000) < data.deathban)
                player.getServer().getScheduler().scheduleDelayedTask(GalacyHCF.instance, () ->
                                player.kick(TextFormat.RED + "You're deathbanned for " + Utils.timerMinutes.format(new Date((data.deathban * 1000L) - System.currentTimeMillis())), false),
                        10, true);
        }
    }

    @EventHandler
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player instanceof GPlayer) {

            ((GPlayer) player).strikeLightning();
            ((GPlayer) player).fightTime = 0;
            if (((GPlayer) player).factionId != 0) {
                Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) player).factionId);
                faction.updateDtr(faction.dtr - 1);
                for (GPlayer member : faction.onlineMembers()) {
                    member.sendMessage(Utils.prefix + TextFormat.YELLOW + "A member of your faction died.");
                }
                GalacyHCF.dtrRegenerationTask.freeze(faction.id);
            }
            RedisPlayer redis = ((GPlayer) player).redisData();
            redis.addDeath();
            if (redis.lives > 0) {
                redis.updateLives(redis.lives - 1);
                player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've been saved from death ban using your second life!");
            } else if (!player.isOp()) {
                ((GPlayer) player).fightTime = 0;
                int banTime;
                switch (((GPlayer) player).rank) {
                    case GPlayer.STAR:
                        banTime = 55;
                        break;
                    case GPlayer.NOVA:
                        banTime = 45;
                        break;
                    case GPlayer.NEBULA:
                        banTime = 35;
                        break;
                    case GPlayer.SOLAR:
                        banTime = 25;
                        break;
                    case GPlayer.GALACY:
                        banTime = 15;
                        break;
                    case GPlayer.PARTNER:
                    case GPlayer.ADMIN:
                    case GPlayer.MOD:
                    case GPlayer.DEVELOPER:
                        banTime = 0;
                        break;
                    default:
                        banTime = 60;
                        break;
                }
                if (banTime != 0) {
                    redis.updateDeathban((int) (System.currentTimeMillis() / 1000) + 60 * banTime);
                    player.sendMessage(Utils.prefix + TextFormat.RED + "You're deathbanned for " + banTime + " minutes.");
                    player.getServer().getScheduler().scheduleDelayedTask(GalacyHCF.instance, () -> {
                        //player.sendMessage(TextFormat.RED + "You're deathbanned for 30 minutes.");
                        player.kick(TextFormat.RED + "You're deathbanned for 30 minutes.", false);
                        //player.transfer(new InetSocketAddress("178.62.193.12", 19232));
                    }, 20, true);
                }
            }
            EntityDamageEvent cause = player.getLastDamageCause();
            String victim = TextFormat.RED + player.getName() + TextFormat.DARK_RED + "[" + TextFormat.RED + ((GPlayer) player).redisData().kills + TextFormat.DARK_RED + "]";
            if (cause instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) cause).getDamager() instanceof GPlayer) {
                GPlayer damager = (GPlayer) ((EntityDamageByEntityEvent) cause).getDamager();
                damager.redisData().addKill();
                String killer = TextFormat.RED + damager.getName() + TextFormat.DARK_RED + "[" + TextFormat.RED + damager.redisData().kills + TextFormat.DARK_RED + "]";
                event.setDeathMessage(victim + TextFormat.YELLOW + " was killed by " + killer);
            } else event.setDeathMessage(victim + TextFormat.YELLOW + " died."); // TODO: More specefic messages.
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void on(PlayerQuitEvent event) {
        event.setQuitMessage("");
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            if (((GPlayer) player).fightTime != 0) {
                player.kill();
                player.teleport(player.getServer().getDefaultLevel().getSpawnLocation());
            }
            ((GPlayer) player).redisData().update(GalacyHCF.redis);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void on(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player instanceof GPlayer && (event.getFrom().getFloorX() != event.getTo().getFloorX() || event.getFrom().getFloorZ() != event.getTo().getFloorZ())) {
            if (((GPlayer) player).freeze > (System.currentTimeMillis() / 1000L)) {
                event.setCancelled(true);
                return;
            }
            if (((GPlayer) player).set == SetsManager.Sets.Miner) {
                if (event.getTo().getFloorY() != event.getFrom().getFloorY()) {
                    if (event.getTo().getFloorY() < 30) {
                        if (!player.hasEffect(Effect.INVISIBILITY))
                            player.addEffect(Effect.getEffect(Effect.INVISIBILITY).setDuration(999999999));
                    } else {
                        if (player.hasEffect(Effect.INVISIBILITY)) player.removeEffect(Effect.INVISIBILITY);
                    }
                }
            }
            if (((GPlayer) player).homeTeleport || ((GPlayer) player).stuckTeleport) {
                if (!((GPlayer) player).moved) {
                    player.sendMessage(Utils.prefix + TextFormat.RED + "Teleportation failed because you moved.");
                    ((GPlayer) player).homeTeleport = false;
                    ((GPlayer) player).stuckTeleport = false;
                    ((GPlayer) player).moved = true;
                }
            }
            Claim claim = GalacyHCF.claimsManager.findClaim(player.getFloorX(), player.getFloorZ());
            if (((GPlayer) player).pvptimer) {
                if (claim != null)
                    if (claim.type == Claim.factionClaim) {
                        player.sendMessage(Utils.prefix + TextFormat.RED + "You can't go into claims while your pvp is disabled! You can enable it with: /pvp.");
                        event.setCancelled(true);

                        return;
                    }
            }
            if (((GPlayer) player).fightTime != 0) {
                if (GalacyHCF.spawnBorder.insideSpawn(event.getTo().getFloorX(), event.getTo().getFloorZ())) {
                    player.sendMessage(Utils.prefix + TextFormat.RED + "You can't go to spawn while you're in combat!");
                    event.setCancelled(true);

                    return;
                } else {
                    BorderFace face = GalacyHCF.spawnBorder.borderFace(event.getTo().getFloorX(), event.getTo().getFloorZ());
                    if (face == null) return;
                    // NOTICE: This has to be optimized.
                    if (new Vector2(0, 0).distance(new Vector2(player.x, player.z)) <= GalacyHCF.spawnBorder.maxX + 50) {
                        if (((GPlayer) player).borderFace == null) {
                            face.build(player);
                            ((GPlayer) player).borderFace = face;
                        } else if (!((GPlayer) player).borderFace.equals(face)) {
                            ((GPlayer) player).borderFace.remove(player);
                            face.build(player);
                            ((GPlayer) player).borderFace = face;
                        }
                    } else if (((GPlayer) player).borderFace != null) {
                        ((GPlayer) player).borderFace.remove(player);
                        ((GPlayer) player).borderFace = null;
                    }
                }
            }
            if (GalacyHCF.worldBorder.outside(event.getTo().getFloorX(), event.getTo().getFloorZ())) {
                player.sendMessage(Utils.prefix + TextFormat.RED + "You've reached the world border!");
                event.setCancelled(true);
                return;
            }
                if (claim == null) {
                    if (((GPlayer) player).claim != null) {
                        if (((GPlayer) player).redisData().pvptime != 0 && !((GPlayer) player).pvptimer)
                            ((GPlayer) player).pvptimer = true;
                        player.sendMessage(TextFormat.YELLOW + "Now Leaving " + TextFormat.GRAY + ((GPlayer) player).claim.factionName + TextFormat.YELLOW + " (" + (((GPlayer) player).claim.type == 1 ? TextFormat.GREEN + "Non-Deathban" : TextFormat.RED + "Deathban") + TextFormat.YELLOW + ")");
                        player.sendMessage(TextFormat.YELLOW + "Now Entering " + TextFormat.GRAY + "Wilderness" + TextFormat.YELLOW + " (" + TextFormat.RED + "Deathban" + TextFormat.YELLOW + ")");

                        ((GPlayer) player).claim = null;
                    }
                } else {
                    if (((GPlayer) player).pvptimer) {
                        player.sendMessage(Utils.prefix + TextFormat.RED + "You can't go into claims while you still have your pvp timer! You can disable it with: /pvp on.");
                        event.setCancelled(true);
                    } else {
                        if (((GPlayer) player).claim == null) {
                            player.sendMessage(TextFormat.YELLOW + "Now Leaving " + TextFormat.GRAY + "Wilderness" + TextFormat.YELLOW + " (" + TextFormat.RED + "Deathban" + TextFormat.YELLOW + ")");
                            player.sendMessage(TextFormat.YELLOW + "Now Entering " + TextFormat.GRAY + claim.factionName + TextFormat.YELLOW + " (" + (claim.type == 1 ? TextFormat.GREEN + "Non-Deathban" : TextFormat.RED + "Deathban") + TextFormat.YELLOW + ")");
                            ((GPlayer) player).claim = claim;
                        } else if (((GPlayer) player).claim.id != claim.id) {
                            if (((GPlayer) player).redisData().pvptime != 0 && !((GPlayer) player).pvptimer)
                                ((GPlayer) player).pvptimer = true;
                            player.sendMessage(TextFormat.YELLOW + "Now Leaving " + TextFormat.GRAY + ((GPlayer) player).claim.factionName + TextFormat.YELLOW + " (" + (((GPlayer) player).claim.type == 1 ? TextFormat.GREEN + "Non-Deathban" : TextFormat.RED + "Deathban") + TextFormat.YELLOW + ")");
                            player.sendMessage(TextFormat.YELLOW + "Now Entering " + TextFormat.GRAY + claim.factionName + TextFormat.YELLOW + " (" + (claim.type == 1 ? TextFormat.GREEN + "Non-Deathban" : TextFormat.RED + "Deathban") + TextFormat.YELLOW + ")");
                            ((GPlayer) player).claim = claim;
                        }
                    }
                }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && GalacyHCF.sotwTask != null && GalacyHCF.sotwTask.started)
            event.setCancelled(true);
    }

    @EventHandler
    public void on(PlayerTeleportEvent event) {
        if (event.getPlayer() instanceof GPlayer) ((GPlayer) event.getPlayer()).updateNametag();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void on(EntityDamageByEntityEvent event) {
        event.setKnockBack(0.37F);
        Entity player = event.getEntity();
        if (GalacyHCF.sotwTask != null && GalacyHCF.sotwTask.started) return;
        if (player instanceof GPlayer) {
            if (GalacyHCF.spawnBorder.insideSpawn(player.getFloorX(), player.getFloorZ())) event.setCancelled(true);
            else {
                Entity damager = event.getDamager();
                if (damager instanceof GPlayer) {
                    if (((GPlayer) damager).pvptimer) {
                        ((GPlayer) damager).sendMessage(Utils.prefix + TextFormat.RED + "You can not hurt " + player.getName() + " because your PvP is not enabled yet!");
                        event.setCancelled(true);
                    } else if (((GPlayer) player).pvptimer) {
                        ((GPlayer) damager).sendMessage(Utils.prefix + TextFormat.RED + "You can not hurt " + player.getName() + " because his PvP is not enabled yet!");
                        event.setCancelled(true);
                    } else {
                        if (((GPlayer) player).factionId == 0 || ((GPlayer) damager).factionId == 0) {
                            ((GPlayer) damager).useRogueSword((GPlayer) player, event);
                            checkTeleport((GPlayer) player);
                            checkTeleport((GPlayer) damager);
                            checkFight((GPlayer) player);
                            checkFight((GPlayer) damager);
                        } else if (((GPlayer) player).factionId == ((GPlayer) damager).factionId) {
                            event.setCancelled(true);
                            ((GPlayer) damager).sendMessage(TextFormat.YELLOW + "You can not hurt " + TextFormat.GREEN + player.getName());
                        } else {
                            ((GPlayer) damager).useRogueSword((GPlayer) player, event);
                            checkTeleport((GPlayer) player);
                            checkTeleport((GPlayer) damager);
                            checkFight((GPlayer) player);
                            checkFight((GPlayer) damager);
                        }
                    }
                } else {
                    checkTeleport((GPlayer) player);
                }
            }
        }

    }

    public void checkTeleport(GPlayer player) {
        if (player.homeTeleport || player.stuckTeleport) {
            player.sendMessage(Utils.prefix + TextFormat.RED + "Teleportation failed because you moved.");
            player.homeTeleport = false;
            player.stuckTeleport = false;
            player.moved = true;
        }
    }

    public void checkFight(GPlayer player) {
        player.fightTime = 30;
        BorderFace face = GalacyHCF.spawnBorder.borderFace(player.getFloorX(), player.getFloorZ());
        if (face == null) return;

        // NOTICE: This has to be optimized.
        if (face.start.distance(new Vector3(player.x, player.y, player.z)) <= 5 || face.end.distance(new Vector3(player.x, player.y, player.z)) <= 5) {
            face.build(player);
            player.borderFace = face;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void on(PlayerChatEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().startsWith("./")) {
            event.setCancelled(true);
            player.getServer().dispatchCommand(player, event.getMessage().substring(2));
            return;
        }
        if (player instanceof GPlayer) {
            if (((GPlayer) player).claimProcess != null) {
                if (((GPlayer) player).claimProcess.state == ClaimProcess.State.waiting) {
                    event.setCancelled(true);
                    if (event.getMessage().equalsIgnoreCase("yes") || event.getMessage().equalsIgnoreCase("accept")) {
                        Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) player).factionId);
                        if (faction.balance < ((GPlayer) player).claimProcess.price) {
                            player.sendMessage(Utils.prefix + TextFormat.RED + "Your faction doesn't have enough money in it's balance, claim cancelled.");
                        } else {
                            if (GalacyHCF.claimsManager.createClaim(((GPlayer) player).claimProcess)) {
                                faction.updateBalance(faction.balance - ((GPlayer) player).claimProcess.price);
                                player.sendMessage(Utils.prefix + TextFormat.GREEN + "Your faction acquired a new claim, congrats!");
                            } else {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "There was an issue with your claim operation!");
                            }
                        }
                        ((GPlayer) player).claimProcess.state = ClaimProcess.State.finished;
                        ((GPlayer) player).claimProcess = null;
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
                if (System.currentTimeMillis() / 1000 > (((GPlayer) player).lastChatted + 3) || ((GPlayer) player).rank != GPlayer.DEFAULT) {
                    ((GPlayer) player).lastChatted = (int) System.currentTimeMillis() / 1000;
                    ((GPlayer) player).lastMessage = event.getMessage();
                    if (((GPlayer) player).factionId != 0) {
                        Faction faction = new Faction(GalacyHCF.mysql, ((GPlayer) player).factionId);
                        event.setFormat(TextFormat.DARK_GRAY + "[" + TextFormat.GRAY + faction.name + TextFormat.DARK_GRAY + "] " + ((GPlayer) player).rankName() + TextFormat.DARK_GRAY + ": " + TextFormat.GRAY + TextFormat.clean(event.getMessage()));
                    } else {
                        event.setFormat(((GPlayer) player).rankName() + TextFormat.DARK_GRAY + ": " + TextFormat.GRAY + TextFormat.clean(event.getMessage()));
                    }
                } else {
                    event.setCancelled(true);
                    player.sendMessage(Utils.prefix + TextFormat.RED + "Please don't spam in the chat.");
                }
            }
        }
    }

    @EventHandler
    public void on(ExplosionPrimeEvent event) {
        event.setBlockBreaking(false);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            if (player.isOp() && ((GPlayer) player).shop != null) {
                if (event.getBlock().getId() == BlockID.SIGN_POST || event.getBlock().getId() == BlockID.WALL_SIGN) {
                    BlockEntity blockEntity = player.getLevel().getBlockEntity(event.getBlock().asBlockVector3().asVector3());
                    if (blockEntity instanceof BlockEntitySign) {
                        ((GPlayer) player).shop.position = event.getBlock().getFloorX() + ":" + event.getBlock().getFloorY() + ":" + event.getBlock().getFloorZ();
                        GalacyHCF.shopsManager.addShop(((GPlayer) player).shop);
                        player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've successfully set the shop sign.");
                        ((BlockEntitySign) blockEntity).setText(
                                ((GPlayer) player).shop.type == Shop.sell ? TextFormat.GREEN + "[SELL]" : TextFormat.BLUE + "[BUY]",
                                ((GPlayer) player).shop.name + " x" + ((GPlayer) player).shop.amount,
                                "$" + ((GPlayer) player).shop.price
                        );
                        ((GPlayer) player).shop = null;
                    }

                    return;
                }
            }
            switch (event.getBlock().getId()) {
                case BlockID.LEVER:
                case BlockID.CHEST:
                case BlockID.TRAPPED_CHEST:
                case BlockID.ENDER_CHEST:
                case BlockID.STONE_PRESSURE_PLATE:
                case BlockID.WOODEN_PRESSURE_PLATE:
                case BlockID.HEAVY_WEIGHTED_PRESSURE_PLATE:
                case BlockID.LIGHT_WEIGHTED_PRESSURE_PLATE:
                case BlockID.STONE_BUTTON:
                case BlockID.WOODEN_BUTTON:
                case BlockID.CRAFTING_TABLE:
                case BlockID.ENCHANT_TABLE:
                case BlockID.FURNACE:
                case BlockID.HOPPER_BLOCK:
                case BlockID.DROPPER:
                case BlockID.DISPENSER:
                case BlockID.DARK_OAK_DOOR_BLOCK:
                case BlockID.ACACIA_DOOR_BLOCK:
                case BlockID.BIRCH_DOOR_BLOCK:
                case BlockID.IRON_DOOR_BLOCK:
                case BlockID.JUNGLE_DOOR_BLOCK:
                case BlockID.SPRUCE_DOOR_BLOCK:
                case BlockID.WOOD_DOOR_BLOCK:
                case BlockID.TNT:
                    editTerrainCheck(event, event.getBlock(), player, false);
                    break;

                case BlockID.FENCE_GATE:
                case BlockID.FENCE_GATE_ACACIA:
                case BlockID.FENCE_GATE_BIRCH:
                case BlockID.FENCE_GATE_DARK_OAK:
                case BlockID.FENCE_GATE_SPRUCE:
                case BlockID.FENCE_GATE_JUNGLE:
                case BlockID.IRON_TRAPDOOR:
                case BlockID.TRAPDOOR:
                    editTerrainCheck(event, event.getBlock(), player, true);
                    break;
            }
            if (event.getItem() != null) {
                switch (event.getItem().getId()) {
                    case ItemID.BUCKET:
                    case ItemID.DIAMOND_HOE:
                    case ItemID.GOLD_HOE:
                    case ItemID.IRON_HOE:
                    case ItemID.STONE_HOE:
                    case ItemID.WOODEN_HOE:
                    case ItemID.DIAMOND_SHOVEL:
                    case ItemID.GOLD_SHOVEL:
                    case ItemID.IRON_SHOVEL:
                    case ItemID.STONE_SHOVEL:
                    case ItemID.WOODEN_SHOVEL:
                        editTerrainCheck(event, event.getBlock(), player, false);
                        break;
                }
                if (!event.isCancelled() && (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR)) {
                    ((GPlayer) player).applyBardItem(event.getItem());
                    if (event.getItem().getId() == ItemID.ENDER_PEARL) {
                        if (System.currentTimeMillis() / 1000 < ((GPlayer) player).enderpearlCountdown) {
                            event.setCancelled(true);
                            player.sendMessage(Utils.prefix + TextFormat.RED + "Wait " + (((GPlayer) player).enderpearlCountdown - (System.currentTimeMillis() / 1000)) + " more seconds before using enderpearls again.");

                            return;
                        } else ((GPlayer) player).enderpearlCountdown = (int) (System.currentTimeMillis() / 1000) + 15;
                    }
                }
            }

            if (event.getBlock().getId() == BlockID.SIGN_POST || event.getBlock().getId() == BlockID.WALL_SIGN) {
                Shop shop = GalacyHCF.shopsManager.shop(event.getBlock().asBlockVector3().asVector3());
                if (shop != null) {
                    if (shop.type == Shop.buy) {
                        if (((GPlayer) player).balance >= shop.price) {
                            Item item = new Item(shop.itemId, 0, shop.amount);
                            if (player.getInventory().canAddItem(item)) {
                                player.getInventory().addItem(item);
                                ((GPlayer) player).updateBalance(((GPlayer) player).balance - shop.price);
                                player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've purchased " + shop.name + " x" + shop.amount + " for $" + shop.price + ".");
                            } else
                                player.sendMessage(Utils.prefix + TextFormat.RED + "You don't have enough space in your inventory.");
                        } else player.sendMessage(Utils.prefix + TextFormat.RED + "You don't have enough money.");
                    } else if (shop.type == Shop.sell) {
                        Item item = new Item(shop.itemId, 0, shop.amount);
                        if (player.getInventory().contains(item)) {
                            player.getInventory().removeItem(item);
                            ((GPlayer) player).updateBalance(((GPlayer) player).balance + shop.price);
                            player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've sold " + shop.name + " x" + shop.amount + " for $" + shop.price + ".");
                        } else
                            player.sendMessage(Utils.prefix + TextFormat.RED + "You don't have enough of that to sell");
                    }
                }
            }

            if (((GPlayer) player).claimProcess != null) {
                if (!((GPlayer) player).claimProcess.expired()) {
                    if (player.distance(player.getServer().getDefaultLevel().getSpawnLocation()) < 300) {
                        player.sendMessage(Utils.prefix + TextFormat.RED + "You have to be at least 300 blocks away from spawn to claim.");

                        return;
                    }
                    if (GalacyHCF.claimsManager.isRoad((int) event.getBlock().x, (int) event.getBlock().z)) {
                        player.sendMessage(Utils.prefix + TextFormat.RED + "You can't claim on the roads.");

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
                            ((GPlayer) player).claimProcess.expireAt += 30;
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
                                player.sendMessage(Utils.prefix + TextFormat.RED + "Sorry but your claim is too small! try again.");
                                break;
                            }
                            if (((GPlayer) player).claimProcess.firstPos.distance(event.getBlock().asBlockVector3().asVector3()) > 100) {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "Sorry but your claim is too big! try again.");
                                break;
                            }
                            ((GPlayer) player).claimProcess.secondPos = event.getBlock().asBlockVector3().asVector3();
                            ((GPlayer) player).claimProcess.state = ClaimProcess.State.waiting;
                            ((GPlayer) player).claimProcess.expireAt += 30;
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

    public void editTerrainCheck(Event event, Block block, Player player, boolean blockMovement) {
        if (player.isOp() && player.isCreative()) return;
        if (player instanceof GPlayer) {
            Claim claim = GalacyHCF.claimsManager.findClaim(block.getFloorX(), block.getFloorZ());
            if (claim != null) {
                if (claim.type == Claim.spawnClaim) {
                    event.setCancelled(true);
                    return;
                }
                if (((GPlayer) player).factionId != claim.factionId) {
                    if (claim.type == Claim.factionClaim) {
                        if (new Faction(GalacyHCF.mysql, claim.factionId).dtr <= 0) return;
                        player.sendMessage(Utils.prefix + TextFormat.RED + "You can't edit terrain on " + claim.factionName + "'s claim.");
                    }
                    event.setCancelled(true);
                    ((GPlayer) player).freeze = System.currentTimeMillis() / 1000L + 2;
                }
            } else {
                if (GalacyHCF.claimsManager.isRoad((int) block.x, (int) block.z)) {
                    event.setCancelled(true);
                } else if (block.distance(player.getServer().getDefaultLevel().getSpawnLocation().asVector3f().asVector3()) < 300) {
                    event.setCancelled(true);
                    player.sendMessage(Utils.prefix + TextFormat.RED + "You have to be at least 300 blocks away from spawn to edit terrain.");
                }
            }
        } else event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(BlockBreakEvent event) {
        Player player = event.getPlayer();
        editTerrainCheck(event, event.getBlock(), event.getPlayer(), true);

        if (player.isOp()) {
            if (event.getBlock().getId() == BlockID.SIGN_POST || event.getBlock().getId() == BlockID.WALL_SIGN) {
                Shop shop = GalacyHCF.shopsManager.shop(event.getBlock().asBlockVector3().asVector3());
                if (shop != null) {
                    GalacyHCF.shopsManager.removeShop(shop);
                    player.sendMessage(Utils.prefix + TextFormat.YELLOW + "Shop sign removed.");
                }
            }
        }
        if (player instanceof GPlayer) {
            RedisPlayer data = ((GPlayer) player).redisData();
            switch (event.getBlock().getId()) {
                case BlockID.DIAMOND_ORE:
                    data.diamonds++;
                    data.update(GalacyHCF.redis);
                    break;
                case BlockID.GOLD_ORE:
                    data.gold++;
                    data.update(GalacyHCF.redis);
                    break;
                case BlockID.IRON_ORE:
                    data.iron++;
                    data.update(GalacyHCF.redis);
                    break;
                case BlockID.REDSTONE_ORE:
                    data.redstone++;
                    data.update(GalacyHCF.redis);
                    break;
                case BlockID.LAPIS_ORE:
                    data.lapis++;
                    data.update(GalacyHCF.redis);
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(BlockPlaceEvent event) {
        editTerrainCheck(event, event.getBlock(), event.getPlayer(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerBucketEmptyEvent event) {
        editTerrainCheck(event, event.getBlockClicked(), event.getPlayer(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerBucketFillEvent event) {
        editTerrainCheck(event, event.getBlockClicked(), event.getPlayer(), false);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void on(DataPacketReceiveEvent event) {
        Player player = event.getPlayer();
        if (player instanceof GPlayer) {
            if (((GPlayer) player).fightTime != 0) {
                if (event.getPacket() instanceof PlayerActionPacket) {
                    Block block = event.getPlayer().getLevel().getBlock(((PlayerActionPacket) event.getPacket()).x, ((PlayerActionPacket) event.getPacket()).y, ((PlayerActionPacket) event.getPacket()).z);
                    if (GalacyHCF.spawnBorder.insideSpawn(block.getFloorX(), block.getFloorZ())) {
                        if (block.getId() == 0) {
                            UpdateBlockPacket packet = new UpdateBlockPacket();
                            packet.x = block.getFloorX();
                            packet.y = block.getFloorY();
                            packet.z = block.getFloorZ();
                            packet.blockRuntimeId = 31;
                            event.getPlayer().dataPacket(packet);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerFormRespondedEvent event) {
        if (event.wasClosed() || event.getResponse() == null) return;
        if (event.getPlayer() instanceof GPlayer) {
            GPlayer player = (GPlayer) event.getPlayer();
            if (event.getWindow() instanceof FormWindowSimple) {
                if (event.getResponse() instanceof FormResponseSimple) {
                    switch (((FormResponseSimple) event.getResponse()).getClickedButtonId()) {
                        case 0:
                            if (player.rank < GPlayer.GALACY) {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "You don't have access to this kit, you have to purchase the Galacy rank from the store.");
                            } else {
                                if (player.redisData().galacyKit < System.currentTimeMillis()) {
                                    player.giveKit(KitsManager.Kits.Galacy);
                                    player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've received your Galacy kit!");
                                    player.redis.galacyKit = System.currentTimeMillis() + KitsManager.cooldown;
                                    player.redis.update(GalacyHCF.redis);
                                } else
                                    player.sendMessage(Utils.prefix + TextFormat.YELLOW + "You're still on cooldown for Galacy Kit!");
                            }
                            break;

                        case 1:
                            if (player.rank < GPlayer.SOLAR) {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "You don't have access to this kit, you have to purchase the Solar rank or higher from the store.");
                            } else {
                                if (player.redisData().diamondKit < System.currentTimeMillis()) {
                                    player.giveKit(KitsManager.Kits.Diamond);
                                    player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've received your Diamond kit!");
                                    player.redis.diamondKit = System.currentTimeMillis() + KitsManager.cooldown;
                                    player.redis.update(GalacyHCF.redis);
                                } else
                                    player.sendMessage(Utils.prefix + TextFormat.YELLOW + "You're still on cooldown for Diamond Kit!");
                            }
                            break;

                        case 2:
                            if (player.rank < GPlayer.NEBULA) {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "You don't have access to this kit, you have to purchase the Nebula rank or higher from the store.");
                            } else {
                                if (player.redisData().rogueKit < System.currentTimeMillis()) {
                                    player.giveKit(KitsManager.Kits.Rogue);
                                    player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've received your Rogue kit!");
                                    player.redis.rogueKit = System.currentTimeMillis() + KitsManager.cooldown;
                                    player.redis.update(GalacyHCF.redis);
                                } else
                                    player.sendMessage(Utils.prefix + TextFormat.YELLOW + "You're still on cooldown for Rogue Kit!");
                            }
                            break;

                        case 3:
                            if (player.rank < GPlayer.NOVA) {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "You don't have access to this kit, you have to purchase the Nova rank or higher from the store.");
                            } else {
                                if (player.redisData().archerKit < System.currentTimeMillis()) {
                                    player.giveKit(KitsManager.Kits.Archer);
                                    player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've received your Archer kit!");
                                    player.redis.archerKit = System.currentTimeMillis() + KitsManager.cooldown;
                                    player.redis.update(GalacyHCF.redis);
                                } else
                                    player.sendMessage(Utils.prefix + TextFormat.YELLOW + "You're still on cooldown for Archer Kit!");
                            }
                            break;

                        case 4:
                            if (player.rank < GPlayer.STAR) {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "You don't have access to this kit, you have to purchase the Star rank or higher from the store.");
                            } else {
                                if (player.redisData().bardKit < System.currentTimeMillis()) {
                                    player.giveKit(KitsManager.Kits.Bard);
                                    player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've received your Bard kit!");
                                    player.redis.bardKit = System.currentTimeMillis() + KitsManager.cooldown;
                                    player.redis.update(GalacyHCF.redis);
                                } else
                                    player.sendMessage(Utils.prefix + TextFormat.YELLOW + "You're still on cooldown for Bard Kit!");
                            }
                            break;

                        case 5:
                            if (player.rank < GPlayer.STAR) {
                                player.sendMessage(Utils.prefix + TextFormat.RED + "You don't have access to this kit, you have to purchase the Star rank or higher from the store.");
                            } else {
                                if (player.redisData().minerKit < System.currentTimeMillis()) {
                                    player.giveKit(KitsManager.Kits.Miner);
                                    player.sendMessage(Utils.prefix + TextFormat.GREEN + "You've received your Miner kit!");
                                    player.redis.minerKit = System.currentTimeMillis() + KitsManager.cooldown;
                                    player.redis.update(GalacyHCF.redis);
                                } else
                                    player.sendMessage(Utils.prefix + TextFormat.YELLOW + "You're still on cooldown for Miner Kit!");
                            }
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerItemConsumeEvent event) {
        switch (event.getItem().getId()) {
            case ItemID.GOLDEN_APPLE:
            case ItemID.GOLDEN_APPLE_ENCHANTED:
                event.setCancelled(true);
                event.getPlayer().sendMessage(Utils.prefix + TextFormat.RED + "This item is not allowed on the server.");

                break;
        }
    }

    //@EventHandler
    public void on(EntityDeathEvent event) {
        if (event.getEntity() instanceof LogoutVillager) {
            if (((LogoutVillager) event.getEntity()).inventory != null) {
                event.setDrops((Item[]) ((LogoutVillager) event.getEntity()).inventory.getContents().values().toArray());
            }
        }
    }
}
