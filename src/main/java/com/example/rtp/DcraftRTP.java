package com.example.rtp;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class DcraftRTP extends JavaPlugin implements CommandExecutor, Listener {

    private final Map<UUID, Boolean> guvenliIsinlanma = new HashMap<>();

    @Override
    public void onEnable() {
        if (getCommand("rtp") != null) getCommand("rtp").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Dcraft RTP Sistemi aktif!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;
        anaMenuAc(p);
        return true;
    }

    private void anaMenuAc(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8§l» §6§lIŞINLANMA MENÜSÜ §8§l«");
        
        ItemStack cam = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta cm = cam.getItemMeta(); if (cm != null) { cm.setDisplayName(" "); cam.setItemMeta(cm); }
        for (int i = 0; i < 27; i++) inv.setItem(i, cam);

        ItemStack smp = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta sMeta = smp.getItemMeta();
        if (sMeta != null) {
            sMeta.setDisplayName("§a§lSMP DÜNYASI");
            sMeta.setLore(Arrays.asList("§7Tıkla ve rastgele bir yere ışınlan.", "§8(Dünya: smp)"));
            sMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
            sMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            smp.setItemMeta(sMeta);
        }
        inv.setItem(11, smp);

        ItemStack nether = new ItemStack(Material.NETHERRACK);
        ItemMeta nMeta = nether.getItemMeta();
        if (nMeta != null) {
            nMeta.setDisplayName("§c§lNETHER");
            nMeta.setLore(Arrays.asList("§7Tıkla ve netherda rastgele ışınlan.", "§8(Dünya: world_nether)"));
            nether.setItemMeta(nMeta);
        }
        inv.setItem(13, nether);

        ItemStack ayar = new ItemStack(Material.COMPARATOR);
        ItemMeta aMeta = ayar.getItemMeta();
        if (aMeta != null) {
            aMeta.setDisplayName("§e§lAYARLAR");
            aMeta.setLore(Arrays.asList("§7Işınlanma ayarlarını değiştir."));
            ayar.setItemMeta(aMeta);
        }
        inv.setItem(15, ayar);

        ItemStack kapat = new ItemStack(Material.BARRIER);
        ItemMeta kMeta = kapat.getItemMeta();
        if (kMeta != null) { kMeta.setDisplayName("§c§lKapat"); kapat.setItemMeta(kMeta); }
        inv.setItem(26, kapat);

        p.openInventory(inv);
        p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f);
    }

    private void ayarlarMenuAc(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8§l» §6§lRTP AYARLARI §8§l«");
        
        ItemStack cam = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta cm = cam.getItemMeta(); if (cm != null) { cm.setDisplayName(" "); cam.setItemMeta(cm); }
        for (int i = 0; i < 27; i++) inv.setItem(i, cam);

        boolean guvenli = guvenliIsinlanma.getOrDefault(p.getUniqueId(), true);

        ItemStack guvenliBtn = new ItemStack(guvenli ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        ItemMeta gMeta = guvenliBtn.getItemMeta();
        if (gMeta != null) {
            gMeta.setDisplayName("§e§lGüvenli Işınlanma: " + (guvenli ? "§aAÇIK" : "§cKAPALI"));
            gMeta.setLore(Arrays.asList("§7Açıkken seni lavın veya suyun içine", "§7atmasını engeller.", "§aTıkla ve değiştir."));
            guvenliBtn.setItemMeta(gMeta);
        }
        inv.setItem(11, guvenliBtn);

        ItemStack geri = new ItemStack(Material.ARROW);
        ItemMeta rMeta = geri.getItemMeta();
        if (rMeta != null) {
            rMeta.setDisplayName("§c§lGeri Dön");
            geri.setItemMeta(rMeta);
        }
        inv.setItem(15, geri);

        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;
        String title = e.getView().getTitle();
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null) return;

        if (title.equals("§8§l» §6§lIŞINLANMA MENÜSÜ §8§l«")) {
            e.setCancelled(true);
            
            if (clicked.getType() == Material.BARRIER) {
                p.closeInventory();
            } else if (clicked.getType() == Material.GRASS_BLOCK) {
                p.closeInventory();
                isinlan(p, "smp"); 
            } else if (clicked.getType() == Material.NETHERRACK) {
                p.closeInventory();
                isinlan(p, "world_nether"); 
            } else if (clicked.getType() == Material.COMPARATOR) {
                ayarlarMenuAc(p);
            }
            return;
        }

        if (title.equals("§8§l» §6§lRTP AYARLARI §8§l«")) {
            e.setCancelled(true);
            
            if (clicked.getType() == Material.LIME_STAINED_GLASS_PANE || clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
                boolean mevcut = guvenliIsinlanma.getOrDefault(p.getUniqueId(), true);
                guvenliIsinlanma.put(p.getUniqueId(), !mevcut);
                ayarlarMenuAc(p);
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (clicked.getType() == Material.ARROW) {
                anaMenuAc(p);
            }
            return;
        }
    }

    private void isinlan(Player p, String dunyaAdi) {
        World hedefDunya = Bukkit.getWorld(dunyaAdi);
        
        if (hedefDunya == null) {
            p.sendMessage("§c" + dunyaAdi + " dünyası sunucuda bulunamadı!");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        p.sendMessage("§eRastgele bir konum aranıyor, lütfen bekle...");
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);

        Random rand = new Random();
        int x = rand.nextInt(10000) - 5000;
        int z = rand.nextInt(10000) - 5000;

        int y = hedefDunya.getHighestBlockYAt(x, z);
        Location hedef = new Location(hedefDunya, x + 0.5, y + 1, z + 0.5);

        boolean guvenli = guvenliIsinlanma.getOrDefault(p.getUniqueId(), true);
        if (guvenli) {
            Material blockType = hedefDunya.getBlockAt(x, y, z).getType();
            int deneme = 0;
            while ((blockType == Material.LAVA || blockType == Material.WATER || blockType == Material.CACTUS || blockType == Material.MAGMA_BLOCK) && deneme < 5) {
                x = rand.nextInt(10000) - 5000;
                z = rand.nextInt(10000) - 5000;
                y = hedefDunya.getHighestBlockYAt(x, z);
                hedef = new Location(hedefDunya, x + 0.5, y + 1, z + 0.5);
                blockType = hedefDunya.getBlockAt(x, y, z).getType();
                deneme++;
            }
        }

        p.teleport(hedef);
        
        p.sendMessage("§a§l✔ §aBaşarıyla ışınlandın!");
        p.sendMessage("§7Konum: §e" + hedef.getBlockX() + ", " + hedef.getBlockY() + ", " + hedef.getBlockZ());
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        
        p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 50, 0.5, 0.5, 0.5, 1);
    }
                        }
