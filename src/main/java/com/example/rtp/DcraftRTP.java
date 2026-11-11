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

    private boolean guvenliIsinlanma = true;

    @Override
    public void onEnable() {
        if (getCommand("rtp") != null) getCommand("rtp").setExecutor(this);
        if (getCommand("rtpadmin") != null) getCommand("rtpadmin").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Dcraft RTP Sistemi aktif!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) return true;

        if (cmd.getName().equalsIgnoreCase("rtp")) {
            anaMenuAc(p);
        } else if (cmd.getName().equalsIgnoreCase("rtpadmin")) {
            if (!p.hasPermission("rtp.admin")) {
                p.sendMessage("§cBu komutu kullanma yetkin yok!");
                return true;
            }
            ayarlarMenuAc(p);
        }
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

        ItemStack end = new ItemStack(Material.END_STONE);
        ItemMeta eMeta = end.getItemMeta();
        if (eMeta != null) {
            eMeta.setDisplayName("§5§lEND DÜNYASI");
            eMeta.setLore(Arrays.asList("§7Tıkla ve End'de rastgele ışınlan.", "§8(Dünya: world_the_end)"));
            eMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
            eMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            end.setItemMeta(eMeta);
        }
        inv.setItem(15, end);

        ItemStack kapat = new ItemStack(Material.BARRIER);
        ItemMeta kMeta = kapat.getItemMeta();
        if (kMeta != null) { kMeta.setDisplayName("§c§lKapat"); kapat.setItemMeta(kMeta); }
        inv.setItem(26, kapat);

        p.openInventory(inv);
        p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f);
    }

    private void ayarlarMenuAc(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8§l» §6§lRTP ADMİN AYARLARI §8§l«");
        
        ItemStack cam = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta cm = cam.getItemMeta(); if (cm != null) { cm.setDisplayName(" "); cam.setItemMeta(cm); }
        for (int i = 0; i < 27; i++) inv.setItem(i, cam);

        ItemStack guvenliBtn = new ItemStack(guvenliIsinlanma ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
        ItemMeta gMeta = guvenliBtn.getItemMeta();
        if (gMeta != null) {
            gMeta.setDisplayName("§e§lGüvenli Işınlanma: " + (guvenliIsinlanma ? "§aAÇIK" : "§cKAPALI"));
            gMeta.setLore(Arrays.asList("§7Açıkken oyuncuları lavın veya suyun", "§7içine atmasını engeller.", "§c(Sadece Adminler Görebilir)", "§aTıkla ve değiştir."));
            guvenliBtn.setItemMeta(gMeta);
        }
        inv.setItem(11, guvenliBtn);

        ItemStack kapat = new ItemStack(Material.BARRIER);
        ItemMeta kMeta = kapat.getItemMeta();
        if (kMeta != null) { kMeta.setDisplayName("§c§lKapat"); kapat.setItemMeta(kMeta); }
        inv.setItem(15, kapat);

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
            } else if (clicked.getType() == Material.END_STONE) {
                p.closeInventory();
                isinlan(p, "world_the_end"); 
            }
            return;
        }

        if (title.equals("§8§l» §6§lRTP ADMİN AYARLARI §8§l«")) {
            e.setCancelled(true);
            
            if (clicked.getType() == Material.LIME_STAINED_GLASS_PANE || clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
                guvenliIsinlanma = !guvenliIsinlanma; 
                ayarlarMenuAc(p); 
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
            } else if (clicked.getType() == Material.BARRIER) {
                p.closeInventory();
            }
            return;
        }
    }

    // --- GÜNCELLENMİŞ IŞINLANMA KODU (Nether ve End Koruması) ---
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
        Location hedef = null;
        boolean bulundu = false;
        int deneme = 0;

        // En fazla 50 kere güvenli bir yer aramayı dene
        while (!bulundu && deneme < 50) {
            int x = rand.nextInt(10000) - 5000;
            int z = rand.nextInt(10000) - 5000;
            int y;

            if (dunyaAdi.equals("world_nether")) {
                // Nether'da çatıya çıkmamak için Y seviyesini rastgele 32 ile 120 arasında seç
                y = rand.nextInt(88) + 32;
            } else {
                // SMP ve End için en yüksek bloğu bul
                y = hedefDunya.getHighestBlockYAt(x, z);
            }

            Location testLoc = new Location(hedefDunya, x + 0.5, y + 1, z + 0.5);
            
            // Blokları kontrol et
            Material altBlok = hedefDunya.getBlockAt(x, y, z).getType();
            Material ayakBlok = hedefDunya.getBlockAt(x, y + 1, z).getType();
            Material kafaBlok = hedefDunya.getBlockAt(x, y + 2, z).getType();

            if (guvenliIsinlanma) {
                // Güvenli ışınlanma açıksa: Ayaklar ve kafa boşluk olmalı, altı katı olmalı (lav/su/bedrock değil)
                if (ayakBlok == Material.AIR && kafaBlok == Material.AIR && 
                    altBlok != Material.AIR && altBlok != Material.LAVA && 
                    altBlok != Material.WATER && altBlok != Material.MAGMA_BLOCK && 
                    altBlok != Material.CACTUS && altBlok != Material.BEDROCK) {
                    hedef = testLoc;
                    bulundu = true;
                }
            } else {
                // Güvenli ışınlanma kapalıysa direkt ışınla
                hedef = testLoc;
                bulundu = true;
            }
            deneme++;
        }

        // Eğer güvenli yer bulunamazsa, son denenen yere ışınla (veya hata ver)
        if (hedef == null) {
            p.sendMessage("§cGüvenli bir yer bulunamadı, tekrar dener misin?");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }

        p.teleport(hedef);
        
        p.sendMessage("§a§l✔ §aBaşarıyla ışınlandın!");
        p.sendMessage("§7Konum: §e" + hedef.getBlockX() + ", " + hedef.getBlockY() + ", " + hedef.getBlockZ());
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        
        p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 50, 0.5, 0.5, 0.5, 1);
    }
            }
