package galacy.galacyhcf.managers;

import cn.nukkit.block.BlockID;
import cn.nukkit.item.*;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.utils.TextFormat;

public class KitsManager {

    // One day in milliseconds.
    public final static long cooldown = 86400000;

    public static Item[] Galacy() {
        Item sword = Item.get(ItemID.DIAMOND_SWORD).setCustomName(TextFormat.BOLD + "" + TextFormat.DARK_PURPLE + "Galacy Sword");
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DAMAGE_ALL).setLevel(3));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(4));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_LOOTING).setLevel(2));

        Item helmet = Item.get(ItemID.DIAMOND_HELMET).setCustomName(TextFormat.BOLD + "" + TextFormat.DARK_PURPLE + "Galacy Helmet");
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item chest = Item.get(ItemID.DIAMOND_CHESTPLATE).setCustomName(TextFormat.BOLD + "" + TextFormat.DARK_PURPLE + "Galacy Chestplate");
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item leg = Item.get(ItemID.DIAMOND_LEGGINGS).setCustomName(TextFormat.BOLD + "" + TextFormat.DARK_PURPLE + "Galacy Leggins");
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item boots = Item.get(ItemID.DIAMOND_BOOTS).setCustomName(TextFormat.BOLD + "" + TextFormat.DARK_PURPLE + "Galacy Boots");
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item bow = Item.get(ItemID.BOW).setCustomName(TextFormat.BOLD + "" + TextFormat.DARK_PURPLE + "Galacy Bow");
        bow.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_BOW_POWER).setLevel(1));

        return new Item[]{
                sword,
                new ItemEnderPearl(0, 16),
                helmet,
                chest,
                leg,
                boots,
                bow,
                new ItemArrow(0, 64),
                new ItemPotatoBaked(0, 64),
                new ItemPotion(ItemPotion.SPEED_II, 3),
                new ItemPotion(ItemPotion.FIRE_RESISTANCE_LONG, 2),
                new ItemPotionSplash(ItemPotion.INSTANT_HEALTH_II, 26)
        };
    }

    public static Item[] Diamond() {
        Item sword = Item.get(ItemID.DIAMOND_SWORD).setCustomName(TextFormat.BOLD + "" + TextFormat.AQUA + "Diamond Sword");
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DAMAGE_ALL).setLevel(2));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(4));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_LOOTING).setLevel(2));

        Item helmet = Item.get(ItemID.DIAMOND_HELMET).setCustomName(TextFormat.BOLD + "" + TextFormat.AQUA + "Diamond Helmet");
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(2));
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item chest = Item.get(ItemID.DIAMOND_CHESTPLATE).setCustomName(TextFormat.BOLD + "" + TextFormat.AQUA + "Diamond Chestplate");
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(2));
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item leg = Item.get(ItemID.DIAMOND_LEGGINGS).setCustomName(TextFormat.BOLD + "" + TextFormat.AQUA + "Diamond Leggins");
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(2));
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item boots = Item.get(ItemID.DIAMOND_BOOTS).setCustomName(TextFormat.BOLD + "" + TextFormat.AQUA + "Diamond Boots");
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(2));
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        return new Item[]{
                sword,
                new ItemEnderPearl(0, 16),
                helmet,
                chest,
                leg,
                boots,
                new ItemPotatoBaked(0, 64),
                new ItemPotion(ItemPotion.SPEED_II, 3),
                new ItemPotion(ItemPotion.FIRE_RESISTANCE_LONG, 2),
                new ItemPotionSplash(ItemPotion.INSTANT_HEALTH_II, 28)
        };
    }

    public static Item[] Archer() {
        Item sword = Item.get(ItemID.DIAMOND_SWORD).setCustomName(TextFormat.BOLD + "" + TextFormat.BLUE + "Archer Sword");
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DAMAGE_ALL).setLevel(2));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_LOOTING).setLevel(2));

        Item helmet = Item.get(ItemID.LEATHER_CAP).setCustomName(TextFormat.BOLD + "" + TextFormat.BLUE + "Archer Helmet");
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item chest = Item.get(ItemID.LEATHER_TUNIC).setCustomName(TextFormat.BOLD + "" + TextFormat.BLUE + "Archer Chestplate");
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item leg = Item.get(ItemID.LEATHER_PANTS).setCustomName(TextFormat.BOLD + "" + TextFormat.BLUE + "Archer Leggins");
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item boots = Item.get(ItemID.LEATHER_BOOTS).setCustomName(TextFormat.BOLD + "" + TextFormat.BLUE + "Archer Boots");
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item bow = Item.get(ItemID.BOW).setCustomName(TextFormat.BOLD + "" + TextFormat.BLUE + "Archer Bow");
        bow.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_BOW_POWER).setLevel(2));
        bow.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_BOW_FLAME).setLevel(1));
        bow.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_BOW_INFINITY).setLevel(1));
        bow.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(2));


        return new Item[]{
                sword,
                new ItemEnderPearl(0, 16),
                helmet,
                chest,
                leg,
                boots,
                bow,
                new ItemArrow(0, 64),
                new ItemPotatoBaked(0, 64),
                new ItemPotion(ItemPotion.FIRE_RESISTANCE_LONG, 2),
                new ItemPotionSplash(ItemPotion.INSTANT_HEALTH_II, 29)
        };
    }

    public static Item[] Rogue() {
        Item sword = Item.get(ItemID.DIAMOND_SWORD).setCustomName(TextFormat.BOLD + "" + TextFormat.WHITE + "Rogue Sword");
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DAMAGE_ALL).setLevel(2));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_LOOTING).setLevel(2));

        Item helmet = Item.get(ItemID.CHAIN_HELMET).setCustomName(TextFormat.BOLD + "" + TextFormat.WHITE + "Rogue Helmet");
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item chest = Item.get(ItemID.CHAIN_CHESTPLATE).setCustomName(TextFormat.BOLD + "" + TextFormat.WHITE + "Rogue Chestplate");
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item leg = Item.get(ItemID.CHAIN_LEGGINGS).setCustomName(TextFormat.BOLD + "" + TextFormat.WHITE + "Rogue Leggins");
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item boots = Item.get(ItemID.CHAIN_BOOTS).setCustomName(TextFormat.BOLD + "" + TextFormat.WHITE + "Rogue Boots");
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        return new Item[]{
                sword,
                new ItemEnderPearl(0, 16),
                helmet,
                chest,
                leg,
                boots,
                new ItemSwordGold(0, 4),
                new ItemPotatoBaked(0, 64),
                new ItemPotion(ItemPotion.FIRE_RESISTANCE_LONG, 2),
                new ItemPotionSplash(ItemPotion.INSTANT_HEALTH_II, 27)
        };
    }

    public static Item[] Bard() {
        Item sword = Item.get(ItemID.DIAMOND_SWORD).setCustomName(TextFormat.BOLD + "" + TextFormat.GOLD + "Bard Sword");
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DAMAGE_ALL).setLevel(2));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));
        sword.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_LOOTING).setLevel(2));

        Item helmet = Item.get(ItemID.GOLD_HELMET).setCustomName(TextFormat.BOLD + "" + TextFormat.GOLD + "Bard Helmet");
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item chest = Item.get(ItemID.GOLD_CHESTPLATE).setCustomName(TextFormat.BOLD + "" + TextFormat.GOLD + "Bard Chestplate");
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item leg = Item.get(ItemID.GOLD_LEGGINGS).setCustomName(TextFormat.BOLD + "" + TextFormat.GOLD + "Bard Leggins");
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item boots = Item.get(ItemID.GOLD_BOOTS).setCustomName(TextFormat.BOLD + "" + TextFormat.GOLD + "Bard Boots");
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        return new Item[]{
                sword,
                new ItemEnderPearl(0, 16),
                helmet,
                chest,
                leg,
                boots,
                new ItemSugar(0, 64),
                new ItemFeather(0, 64),
                new ItemIngotIron(0, 64),
                new ItemGhastTear(0, 64),
                new ItemBlazePowder(0, 64),
                new ItemPotatoBaked(0, 64),
                new ItemPotionSplash(ItemPotion.INSTANT_HEALTH_II, 28)
        };
    }

    public static Item[] Miner() {
        Item pickaxe = Item.get(ItemID.DIAMOND_PICKAXE).setCustomName(TextFormat.BOLD + "" + TextFormat.GREEN + "Miner Pickaxe");
        pickaxe.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_EFFICIENCY).setLevel(4));
        pickaxe.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_FORTUNE_DIGGING).setLevel(2));
        pickaxe.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item shovel = Item.get(ItemID.DIAMOND_SHOVEL).setCustomName(TextFormat.BOLD + "" + TextFormat.GREEN + "Miner Shovel");
        shovel.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_EFFICIENCY).setLevel(4));
        shovel.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_FORTUNE_DIGGING).setLevel(2));
        shovel.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(4));

        Item axe = Item.get(ItemID.DIAMOND_AXE).setCustomName(TextFormat.BOLD + "" + TextFormat.GREEN + "Miner Axe");
        axe.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_EFFICIENCY).setLevel(4));
        axe.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_FORTUNE_DIGGING).setLevel(2));
        axe.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(4));

        Item helmet = Item.get(ItemID.IRON_HELMET).setCustomName(TextFormat.BOLD + "" + TextFormat.GREEN + "Miner Helmet");
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        helmet.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item chest = Item.get(ItemID.IRON_CHESTPLATE).setCustomName(TextFormat.BOLD + "" + TextFormat.GREEN + "Miner Chestplate");
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        chest.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item leg = Item.get(ItemID.IRON_LEGGINGS).setCustomName(TextFormat.BOLD + "" + TextFormat.GREEN + "Miner Leggins");
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        leg.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));

        Item boots = Item.get(ItemID.IRON_BOOTS).setCustomName(TextFormat.BOLD + "" + TextFormat.GREEN + "Miner Boots");
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_ALL).setLevel(3));
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_DURABILITY).setLevel(3));
        boots.addEnchantment(Enchantment.getEnchantment(Enchantment.ID_PROTECTION_FALL).setLevel(3));

        return new Item[]{
                pickaxe,
                pickaxe,
                shovel,
                axe,
                helmet,
                chest,
                leg,
                boots,
                Item.get(BlockID.CRAFTING_TABLE, 0, 64),
                Item.get(BlockID.FURNACE, 0, 64),
                new ItemPotatoBaked(0, 64)
        };
    }

    public enum Kits {
        Galacy,
        Diamond,
        Bard,
        Archer,
        Rogue,
        Miner
    }
}
