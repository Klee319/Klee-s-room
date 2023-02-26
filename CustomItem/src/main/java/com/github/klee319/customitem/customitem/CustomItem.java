package com.github.klee319.customitem.customitem;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Collection;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.Nullable;
//getLogger().info("test")
public class CustomItem extends JavaPlugin implements Listener {
    private static CustomItem plugin;
    //private boolean isParticleEnabled = true;

    @Override
    public void onEnable() {
        plugin = this;
        getServer().getPluginManager().registerEvents(this, this);
        giveparticle();
        Material[] materials =new Material[3];
        materials[0]=Material.IRON_PICKAXE;
        materials[1]=Material.GOLDEN_PICKAXE;
        materials[2]=Material.DIAMOND_PICKAXE;
        ItemStack result=new ItemStack(Material.IRON_PICKAXE);
        CreateSmitingRecipe(CustomTag.setDisplay(result,"&3光の加護","&c玉ねぎ","&e玉ねぎ"),materials, new CustomTagRecipeChoice(setItem()),"ParticlePickaxe");
    }
    public static CustomItem getPlugin(){ return plugin; }
    public static ItemStack setItem() {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize("&6玉ねぎの &b&lたまこ&c!");
        meta.displayName(component);
        List<Component> ComponentList = new ArrayList<>();
        ComponentList.add(component);
        ComponentList.add(component);
        meta.lore(ComponentList);
        NamespacedKey nameKey = new NamespacedKey(plugin, "CustomTagParticle");
        PersistentDataContainer tag = meta.getPersistentDataContainer();
        tag.set(nameKey, PersistentDataType.STRING,"Onion");
        item.setItemMeta(meta);
        return item;
    }

    public static void giveItem(Player player,ItemStack item) {
        player.getInventory().addItem(item);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("giveGold")) {
            if (sender instanceof Player player) {
                giveItem(player,setItem()) ;
            }
        }
        return false;
    }

    public void giveparticle() {
        new BukkitRunnable() {
        @Override
        public void run () {
            final Collection<? extends Player> players=  getServer().getOnlinePlayers();
            for (Player player : players) {
                final ItemStack itemstack= player.getInventory().getItemInMainHand();
                     if (Objects.equals(CustomTag.getCustomTag(itemstack,"CustomTagParticle"),"Onion")) {
                         player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 5, 0.1, 1, 0.1,0.1);
                     }

            }
        }
        }.runTaskTimer(plugin, 0L,5L);
    }


    public void CreateSmitingRecipe(ItemStack item, Material[] materialA, CustomTagRecipeChoice addition, String key) {
        NamespacedKey nameKey = new NamespacedKey(plugin, key);
        RecipeChoice.MaterialChoice base= new RecipeChoice.MaterialChoice(materialA);
        //RecipeChoice.MaterialChoice addition= new RecipeChoice.MaterialChoice(materialB);
       SmithingRecipe recipe= new SmithingRecipe(nameKey,item, base,addition,true);
       getServer().addRecipe(recipe);
    }


    @EventHandler
    public void onSmitingItem(PrepareSmithingEvent e) {
            SmithingInventory inv = e.getInventory();
            ItemStack itemresult = e.getResult();
            if(itemresult!=null) {
                    ItemStack itembase = inv.getInputEquipment();
                    ItemStack itemaddition = inv.getInputMineral();
                    if(Objects.equals(CustomTag.getCustomTag(itembase, "CustomTagParticle"), "Onion")){
                        e.setResult(new ItemStack(Material.AIR));
                    }
                    else if (!Objects.equals(CustomTag.getCustomTag(itemresult, "CustomTagParticle"), "Onion")) {

                        if (Objects.equals(CustomTag.getCustomTag(itemaddition, "CustomTagParticle"), "Onion")) {
                            ItemStack newresult = Objects.requireNonNull(itembase).clone();
                            ItemStack item = CustomTag.setCustomTag(newresult, "Onion", "CustomTagParticle");
                            e.setResult( CustomTag.addLore(item, "&a玉ねぎ"));
                        }else e.setResult(new ItemStack(Material.AIR));
                    }



            }




    }

    @Override
    public void onDisable() {
        NamespacedKey nameKey = new NamespacedKey(plugin, "ParticlePickaxe");
        getServer().removeRecipe(nameKey);
    }


}

