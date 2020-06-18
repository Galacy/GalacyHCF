package galacy.galacyhcf.models;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.StringEntityData;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector2;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.TextFormat;
import galacy.galacyhcf.GalacyHCF;
import galacy.galacyhcf.managers.BorderFace;
import galacy.galacyhcf.managers.ClaimProcess;
import galacy.galacyhcf.managers.KitsManager;
import galacy.galacyhcf.managers.SetsManager;
import galacy.galacyhcf.providers.SQLStatements;
import galacy.galacyhcf.scoreboardapi.scoreboard.SimpleScoreboard;
import galacy.galacyhcf.utils.Utils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class GPlayer extends Player {

    // Currently USELESS so I commented it: public int rowId;
    public Date createdAt;
    public Date updatedAt;
    public String dbUsername;
    public String xuid;
    public int rank;
    public int balance;
    public int factionId;

    // Local cached data
    public boolean homeTeleport = false;
    public boolean stuckTeleport = false;
    public int teleportTime;
    public Position teleportPosition;

    public boolean moved = false;
    public int fightTime = 0;

    public int invitedTo = 0;
    public SimpleScoreboard sb;
    public Chat chatType = Chat.Public;
    public ClaimProcess claimProcess;
    public Claim claim;
    public RedisPlayer redis;
    public BorderFace borderFace;
    public boolean pvptimer = false;
    public long freeze = System.currentTimeMillis() / 1000L;
    public boolean coords = false;
    public SetsManager.Sets set = SetsManager.Sets.Nothing;
    public int bardEnergy = 100;
    public int bardCooldown = 0;
    public int rogueCooldown = 0;
    public Shop shop;
    public boolean mapShown = false;
    public int enderpearlCountdown = 0;
    public boolean firstTime = false;
    public int lastChatted = 0;
    public String lastMessage = "";

    // RANKS
    public static final int DEFAULT = 0;
    public static final int MEMBER = 1;
    public static final int STAR = 2;
    public static final int NOVA = 3;
    public static final int NEBULA = 4;
    public static final int SOLAR = 5;
    public static final int GALACY = 6;
    public static final int PARTNER = 7;
    public static final int MOD = 8;
    public static final int ADMIN = 9;
    public static final int DEVELOPER = 10;

    public void loadData() {
        try {
            ResultSet result = GalacyHCF.mysql.query(SQLStatements.playerById.replace("$xuid", getLoginChainData().getXUID()));
            if (result.next()) {
                // Currently USELESS so I commented it: rowId = result.getInt("id");
                createdAt = result.getDate("created_at");
                updatedAt = result.getDate("updated_at");
                dbUsername = result.getString("username");
                xuid = result.getString("xuid");
                rank = result.getInt("rank");
                balance = result.getInt("balance");
                factionId = result.getInt("faction_id");

                if (!getName().equals(dbUsername)) {
                    try {
                        String currentTime = Utils.dateFormat.format(new java.util.Date());

                        GalacyHCF.mysql.exec(SQLStatements.updatePlayerUsernameById.
                                replace("$updated_at", currentTime).
                                replace("$xuid", getLoginChainData().getXUID()).
                                replace("$username", getName()));
                        GalacyHCF.instance.getLogger().info(dbUsername + " has changed his name to " + getName());
                        dbUsername = getName();
                    } catch (SQLException e) {
                        GalacyHCF.instance.getLogger().info(TextFormat.RED + "[MySQL]: Had issues changing player username: " + e);
                    }
                }
            } else {
                GalacyHCF.instance.getLogger().info(TextFormat.YELLOW + "[MySQL]: Couldn't find the player in the database, creating a new one.");
                firstTime = true;
                try {
                    String currentTime = Utils.dateFormat.format(new java.util.Date());
                    GalacyHCF.mysql.exec(SQLStatements.createPlayer.
                            replace("$created_at", currentTime).
                            replace("$updated_at", currentTime).
                            replace("$username", getName()).
                            replace("$xuid", getLoginChainData().getXUID()).
                            replace("$rank", "0").
                            replace("$balance", "0").
                            replace("$faction_id", "0"));

                    loadData();
                } catch (SQLException e) {
                    getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues creating a new player: " + e);
                }
            }
            result.close();
        } catch (SQLException e) {
            getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues finding faction by name: " + e);
        }

        pvptimer = redisData().pvptime > 0 && redisData().pvptime < 60 * 15;
    }


    public GPlayer(SourceInterface interfaz, Long clientID, String ip, int port) {
        super(interfaz, clientID, ip, port);
    }

    public void leaveFaction() {
        try {
            Faction fac = new Faction(GalacyHCF.mysql, factionId);
            GalacyHCF.mysql.exec(SQLStatements.setPlayerFactionById.
                    replace("$faction_id", "0").
                    replace("$xuid", xuid).
                    replace("$updated_at", Utils.dateFormat.format(new java.util.Date())));
            factionId = 0;
            fac.updateMaxDtr();
            updateNametag();
        } catch (SQLException e) {
            getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues removing player from faction: " + e);
        }
    }

    public void updateBalance(int newBalance) {
        try {
            GalacyHCF.mysql.exec(SQLStatements.updatePlayerBalanceById.
                    replace("$balance", String.valueOf(newBalance)).
                    replace("$xuid", xuid).
                    replace("$updated_at", Utils.dateFormat.format(new java.util.Date())));
            balance = newBalance;
        } catch (SQLException e) {
            getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues updating player balance: " + e);
        }
    }

    public void inviteTo(int factionId) {
        invitedTo = factionId;
        getServer().getScheduler().scheduleDelayedTask(GalacyHCF.instance, () -> invitedTo = 0, 20 * 30, true);
    }

    public void joinFaction(int newFactionId) {
        try {
            GalacyHCF.mysql.exec(SQLStatements.setPlayerFactionById.
                    replace("$faction_id", String.valueOf(newFactionId)).
                    replace("$xuid", xuid).
                    replace("$updated_at", Utils.dateFormat.format(new java.util.Date())));
            factionId = newFactionId;
            new Faction(GalacyHCF.mysql, factionId).updateMaxDtr();
            updateNametag();
        } catch (SQLException e) {
            getServer().getLogger().info(TextFormat.RED + "[MySQL]: Had issues adding player to faction: " + e);
        }
    }

    public void buildPillar(int x, int y, int z, int firstBlockId, int secondBlockId) {
        GalacyHCF.instance.getServer().getScheduler().scheduleTask(GalacyHCF.instance, () -> {
            int blocks = 0;
            for (int i = y; i < 128; i++) {
                UpdateBlockPacket packet = new UpdateBlockPacket();
                packet.x = x;
                packet.y = i;
                packet.z = z;
                if (blocks == 4) {
                    packet.blockRuntimeId = firstBlockId;
                    blocks = 0;
                } else {
                    packet.blockRuntimeId = secondBlockId;
                }
                dataPacket(packet);

                blocks++;
            }
        }, true);
    }

    public void buildPillar(int x, int y, int z, int blockId) {
        GalacyHCF.instance.getServer().getScheduler().scheduleTask(GalacyHCF.instance, () -> {
            for (int i = y; i < 128; i++) {
                UpdateBlockPacket packet = new UpdateBlockPacket();
                packet.x = x;
                packet.y = i;
                packet.z = z;
                packet.blockRuntimeId = blockId;
                dataPacket(packet);
            }
        }, true);
    }

    public RedisPlayer redisData() {
        if (redis == null)
            redis = GalacyHCF.redis.getPlayer(getLoginChainData().getXUID());

        return redis;
    }

    public void applySet(boolean ItemsConsumed) {
        if (getInventory().getHelmet().getId() == ItemID.IRON_HELMET && getInventory().getChestplate().getId() == ItemID.IRON_CHESTPLATE && getInventory().getLeggings().getId() == ItemID.IRON_LEGGINGS && getInventory().getBoots().getId() == ItemID.IRON_BOOTS) {
            if (set != SetsManager.Sets.Miner) {
                set = SetsManager.Sets.Miner;
                for (Effect effect : SetsManager.minerEffects) {
                    addEffect(effect);
                }
            }
        } else if (getInventory().getHelmet().getId() == ItemID.GOLD_HELMET && getInventory().getChestplate().getId() == ItemID.GOLD_CHESTPLATE && getInventory().getLeggings().getId() == ItemID.GOLD_LEGGINGS && getInventory().getBoots().getId() == ItemID.GOLD_BOOTS) {
            if (set != SetsManager.Sets.Bard) {
                set = SetsManager.Sets.Bard;
                removeAllEffects();
                for (Effect effect : SetsManager.bardEffects) {
                    addEffect(effect);
                }
                return;
            }
            if (ItemsConsumed) {
                removeAllEffects();
                for (Effect effect : SetsManager.bardEffects) {
                    addEffect(effect);
                }
            }
        } else if (getInventory().getHelmet().getId() == ItemID.LEATHER_CAP && getInventory().getChestplate().getId() == ItemID.LEATHER_TUNIC && getInventory().getLeggings().getId() == ItemID.LEATHER_PANTS && getInventory().getBoots().getId() == ItemID.LEATHER_BOOTS) {
            if (set != SetsManager.Sets.Archer) {
                set = SetsManager.Sets.Archer;
                for (Effect effect : SetsManager.archerEffects) {
                    addEffect(effect);
                }
            }
        } else if (getInventory().getHelmet().getId() == ItemID.CHAIN_HELMET && getInventory().getChestplate().getId() == ItemID.CHAIN_CHESTPLATE && getInventory().getLeggings().getId() == ItemID.CHAIN_LEGGINGS && getInventory().getBoots().getId() == ItemID.CHAIN_BOOTS) {
            if (set != SetsManager.Sets.Rogue) {
                set = SetsManager.Sets.Rogue;
                for (Effect effect : SetsManager.rogueEffects) {
                    addEffect(effect);
                }
            }
        } else {
            if (set != SetsManager.Sets.Nothing) {
                switch (set) {
                    case Bard:
                        for (Effect effect : SetsManager.bardEffects) removeEffect(effect.getId());
                        sendMessage(Utils.prefix + TextFormat.YELLOW + "Removed your Bard set.");
                        break;

                    case Miner:
                        for (Effect effect : SetsManager.minerEffects) removeEffect(effect.getId());
                        if (hasEffect(Effect.INVISIBILITY)) removeEffect(Effect.INVISIBILITY);
                        sendMessage(Utils.prefix + TextFormat.YELLOW + "Removed your Miner set.");
                        break;

                    case Archer:
                        for (Effect effect : SetsManager.archerEffects) removeEffect(effect.getId());
                        sendMessage(Utils.prefix + TextFormat.YELLOW + "Removed your Archer set.");
                        break;

                    case Rogue:
                        for (Effect effect : SetsManager.rogueEffects) removeEffect(effect.getId());
                        sendMessage(Utils.prefix + TextFormat.YELLOW + "Removed your Rogue set.");
                        break;
                }
                set = SetsManager.Sets.Nothing;
            }
        }
    }

    public void applyBardItem(int id) {
        if (set != SetsManager.Sets.Bard) return;
        if (bardCooldown != 0) {
            sendMessage(Utils.prefix + TextFormat.RED + "You have to wait " + bardCooldown + "s before using bard items again.");
            return;
        }

        switch (id) {
            case ItemID.SUGAR:
                if (bardEnergy < 20) {
                    sendPopup(Utils.prefix + TextFormat.RED + "You don't have enough bard energy to use this.");
                } else {
                    bardCooldown = 15;
                    bardEnergy -= 20;
                    Effect effect = Effect.getEffect(Effect.SPEED).setAmplifier(1).setDuration(120);
                    getInventory().remove(new Item(id, 0, 1));
                    forceTeamEffects(effect);
                }
                break;

            case ItemID.FEATHER:
                if (bardEnergy < 20) {
                    sendPopup(Utils.prefix + TextFormat.RED + "You don't have enough bard energy to use this.");
                } else {
                    bardCooldown = 15;
                    bardEnergy -= 20;
                    Effect effect = Effect.getEffect(Effect.JUMP).setAmplifier(3).setDuration(160);
                    getInventory().remove(new Item(id, 0, 1));
                    forceTeamEffects(effect);
                }
                break;

            case ItemID.IRON_INGOT:
                if (bardEnergy < 20) {
                    sendPopup(Utils.prefix + TextFormat.RED + "You don't have enough bard energy to use this.");
                } else {
                    bardCooldown = 15;
                    bardEnergy -= 20;
                    Effect effect = Effect.getEffect(Effect.DAMAGE_RESISTANCE).setAmplifier(2).setDuration(160);
                    getInventory().remove(new Item(id, 0, 1));
                    forceTeamEffects(effect);
                }
                break;

            case ItemID.GHAST_TEAR:
                if (bardEnergy < 30) {
                    sendPopup(Utils.prefix + TextFormat.RED + "You don't have enough bard energy to use this.");
                } else {
                    bardCooldown = 15;
                    bardEnergy -= 30;
                    Effect effect = Effect.getEffect(Effect.REGENERATION).setAmplifier(1).setDuration(100);
                    getInventory().remove(new Item(id, 0, 1));
                    forceTeamEffects(effect);
                }
                break;

            case ItemID.BLAZE_POWDER:
                if (bardEnergy <= 40) {
                    sendPopup(Utils.prefix + TextFormat.RED + "You don't have enough bard energy to use this.");
                } else {
                    bardCooldown = 15;
                    bardEnergy -= 40;
                    Effect effect = Effect.getEffect(Effect.STRENGTH).setAmplifier(1).setDuration(80);
                    getInventory().remove(new Item(id, 0, 1));
                    forceTeamEffects(effect);
                }
                break;
        }
    }

    public void forceTeamEffects(Effect effect) {
        if (factionId == 0) {
            addEffect(effect);
            getServer().getScheduler().scheduleDelayedTask(GalacyHCF.instance, () -> applySet(true), effect.getDuration());

            return;
        }
        Faction faction = new Faction(GalacyHCF.mysql, factionId);
        for (GPlayer member : faction.onlineMembers()) {
            if (distance(member) > 10) continue;
            member.addEffect(effect);
            getServer().getScheduler().scheduleDelayedTask(GalacyHCF.instance, () -> member.applySet(true), effect.getDuration());
        }

    }

    public void useRogueSword(GPlayer player, EntityDamageByEntityEvent event) {
        if (set != SetsManager.Sets.Rogue) return;
        if (rogueCooldown > 0) return;
        if (getInventory().getItemInHand().getId() != ItemID.GOLD_SWORD) return;
        if (player.getAim(this) > 0.5) {
            switch (set) {
                case Bard:
                case Archer:
                case Miner:
                case Rogue:
                    event.setDamage((float) player.getMaxHealth() / 2);
                    break;
                default:
                    event.setDamage((float) player.getMaxHealth() / 3);
                    break;
            }

            sendMessage(Utils.prefix + TextFormat.GREEN + "Back-stabbed " + player.getName() + ".");
            rogueCooldown = 15;
            addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(20 * 60));
            getInventory().setItemInHand(null);
        }
    }

    public double getAim(Entity entity) {
        Vector2 directionPlane = getDirectionPlane();
        Double dot1 = directionPlane.dot(new Vector2(x, z));
        Double dot2 = directionPlane.dot(new Vector2(entity.x, entity.z));

        return dot1 - dot2;
    }

    public void setNameTag(String name, Player player) {
        SetEntityDataPacket pk = new SetEntityDataPacket();
        pk.eid = this.getId();
        pk.metadata = getDataProperties().put(new StringEntityData(4, name));
        player.dataPacket(pk);
    }

    public void setNameTag(String name, Player[] players) {
        for (Player player : players) {
            if (player.getId() == getId()) continue;
            setNameTag(name, player);
        }
    }

    public void setNameTag(String name, Map<UUID, Player> players) {
        players.forEach((uuid, player) -> {
            if (uuid != this.uuid) {
                setNameTag(name, player);
            }
        });
    }

    public void updateNametag() {
        if (factionId != 0) {
            getServer().getOnlinePlayers().forEach((uuid, player) -> {
                if (player instanceof GPlayer) {
                    if (((GPlayer) player).factionId == factionId) {
                        ((GPlayer) player).setNameTag(TextFormat.GREEN + player.getName(), this);
                        setNameTag(TextFormat.GREEN + getName(), player);
                    } else {
                        ((GPlayer) player).setNameTag(TextFormat.YELLOW + player.getName(), this);
                        setNameTag(TextFormat.YELLOW + getName(), player);
                    }
                }
            });
        } else {
            setNameTag(TextFormat.YELLOW + getName(), getServer().getOnlinePlayers());
            getServer().getOnlinePlayers().forEach((uuid, player) -> {
                if (player instanceof GPlayer) {
                    ((GPlayer) player).setNameTag(TextFormat.YELLOW + player.getName(), this);
                }
            });
        }
    }

    public int highestBlockAt(int x, int z) {
        int y = 128;
        while (getLevel().getBlockIdAt(x, y, z) == 0 && y > 0) y--;

        return y;
    }

    public void showMap() {
        int[] ids = new int[]{1, 2, 3, 4, 5, 6, 7, 11, 12, 13, 14, 15, 16, 30, 101, 102, 133, 134, 147, 148, 149, 247, 248};
        int lastId = 0;
        for (Claim claim : GalacyHCF.claimsManager.claims(getFloorX() - 25, getFloorX() + 25, getFloorZ() - 25, getFloorZ() + 25)) {
            buildPillar(claim.x1, highestBlockAt(claim.x1, claim.z1) + 1, claim.z1, ids[lastId], 132);
            buildPillar(claim.x2, highestBlockAt(claim.x2, claim.z2) + 1, claim.z2, ids[lastId], 132);
            lastId++;
        }
        mapShown = true;
        getServer().getScheduler().scheduleDelayedTask(GalacyHCF.instance, () -> {
            hideMap();
            sendMessage(Utils.prefix + TextFormat.YELLOW + "Claims map now hidden.");
        }, 20 * 30);
    }

    public void hideMap() {
        for (Claim claim : GalacyHCF.claimsManager.claims(getFloorX() - 50, getFloorX() + 50, getFloorZ() - 50, getFloorZ() + 50)) {
            buildPillar(claim.x1, highestBlockAt(claim.x1, claim.z1) + 1, claim.z1, 0);
            buildPillar(claim.x2, highestBlockAt(claim.x2, claim.z2) + 1, claim.z2, 0);
        }
        mapShown = false;
    }

    public String rankName() {
        switch (rank) {
            case MEMBER:
                return TextFormat.BOLD + "" + TextFormat.GRAY + getName() + TextFormat.RESET;

            case STAR:
                return TextFormat.BOLD + "" + TextFormat.WHITE + getName() + TextFormat.RESET;

            case NOVA:
                return TextFormat.BOLD + "" + TextFormat.YELLOW + getName() + TextFormat.RESET;

            case NEBULA:
                return TextFormat.DARK_GREEN + "Nebula " + TextFormat.BOLD + "" + TextFormat.GREEN + getName() + TextFormat.RESET;

            case SOLAR:
                return TextFormat.YELLOW + "Solar " + TextFormat.BOLD + "" + TextFormat.GOLD + getName() + TextFormat.RESET;

            case GALACY:
                return TextFormat.LIGHT_PURPLE + "[Galacy] " + TextFormat.BOLD + "" + TextFormat.DARK_PURPLE + getName() + TextFormat.RESET;

            case PARTNER:
                return TextFormat.BOLD + "" + TextFormat.RED + getName() + TextFormat.RESET;

            case MOD:
                return TextFormat.BOLD + "" + TextFormat.AQUA + "[STAFF] " + getName() + TextFormat.RESET;

            case ADMIN:
                return TextFormat.BOLD + "" + TextFormat.DARK_AQUA + "[STAFF] " + getName() + TextFormat.RESET;

            case DEVELOPER:
                return TextFormat.BOLD + "" + TextFormat.GREEN + "[DEVELOPER] " + getName() + TextFormat.RESET;

            default:
                return TextFormat.GRAY + getName();
        }
    }

    public void strikeLightning() {
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityUniqueId = cn.nukkit.entity.Entity.entityCount++;
        pk.entityRuntimeId = cn.nukkit.entity.Entity.entityCount++;
        pk.type = EntityLightning.NETWORK_ID;
        pk.x = (float) getX();
        pk.y = (float) getY();
        pk.z = (float) getZ();
        pk.speedX = 0.0f;
        pk.speedY = 0.0f;
        pk.speedZ = 0.0f;
        pk.yaw = (float) getYaw();
        pk.pitch = (float) getPitch();

        for (Player p : getLevel().getPlayers().values()) {
            p.dataPacket(pk);
        }
    }

    public void giveKit(KitsManager.Kits kit) {
        switch (kit) {
            case Galacy:
                for (Item item : KitsManager.Galacy()) {
                    forceAddArmor(item);
                }
                break;
            case Diamond:
                for (Item item : KitsManager.Diamond()) {
                    forceAddArmor(item);
                }
                break;
            case Rogue:
                for (Item item : KitsManager.Rogue()) {
                    forceAddArmor(item);
                }
                break;
            case Archer:
                for (Item item : KitsManager.Archer()) {
                    forceAddArmor(item);
                }
                break;
            case Bard:
                for (Item item : KitsManager.Bard()) {
                    forceAddArmor(item);
                }
                break;
            case Miner:
                for (Item item : KitsManager.Miner()) {
                    forceAddArmor(item);
                }
                break;
        }
    }

    public void forceAddItem(Item item) {
        if (getInventory().canAddItem(item)) getInventory().addItem(item);
        else getLevel().dropItem(asVector3f().asVector3(), item);
    }

    public void forceAddArmor(Item item) {
        if (item.isHelmet()) {
            if (getInventory().getHelmet() == null) getInventory().setHelmet(item);
            else forceAddItem(item);
        } else if (item.isChestplate()) {
            if (getInventory().getChestplate() == null) getInventory().setChestplate(item);
            else forceAddItem(item);
        } else if (item.isLeggings()) {
            if (getInventory().getLeggings() == null) getInventory().setLeggings(item);
            else forceAddItem(item);
        } else if (item.isBoots()) {
            if (getInventory().getBoots() == null) getInventory().setBoots(item);
            else forceAddItem(item);
        } else forceAddItem(item);
    }

    public enum Chat {
        Public,
        Faction
    }
}
