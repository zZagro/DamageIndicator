package eu.zzagro.damageindicator.listeners;

import de.tr7zw.nbtapi.NBTEntity;
import eu.zzagro.damageindicator.Damageindicator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class DamageListener implements Listener {

    private final Damageindicator plugin;

    public List<ArmorStand> stands = new ArrayList<>();

    public DamageListener(Damageindicator plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntityType().equals(EntityType.ARMOR_STAND)) {
            return;
        }
        if (e.getEntity() instanceof LivingEntity && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            ArmorStand stand = (ArmorStand) e.getEntity().getWorld().spawnEntity(((LivingEntity) e.getEntity()).getEyeLocation().subtract(0, 0.5, 0), EntityType.ARMOR_STAND);
            stand.setGravity(false);
            stand.setVisible(false);
            stand.setMarker(true);
            stand.setCustomNameVisible(true);
            stand.setCustomName(ChatColor.translateAlternateColorCodes('&', "&7" + String.valueOf(Math.round(e.getDamage())).replace(".0", "")));
            NBTEntity nbtStand = new NBTEntity((Entity) stand);
            nbtStand.setBoolean("Invulnerable", Boolean.TRUE);
            stands.add(stand);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (!stand.isDead()) {
                        stand.remove();
                    }
                    if (stand.isDead()) {
                        stands.remove(stand);
                    }
                }
            }, 20);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            boolean crit = player.getFallDistance() > 0.0F && !player.isOnGround() && !onClimb(player) && !player.getLocation().getBlock().isLiquid() && !(player.getActivePotionEffects() == PotionEffectType.BLINDNESS) && player.getVehicle() == null && !player.isSprinting() && !player.isFlying() && e.getEntity() instanceof LivingEntity;
            if (e.getEntityType().equals(EntityType.ARMOR_STAND)) {
                return;
            }
            if (e.getEntity() instanceof LivingEntity) {
                ArmorStand stand = (ArmorStand) e.getEntity().getWorld().spawnEntity(((LivingEntity) e.getEntity()).getEyeLocation().subtract(0, 0.5, 0), EntityType.ARMOR_STAND);
                stand.setGravity(false);
                stand.setVisible(false);
                stand.setMarker(true);
                stand.setCustomNameVisible(true);
                String damage = String.valueOf(Math.round(e.getDamage()));
                if (crit) {
                    stand.setCustomName(ChatColor.translateAlternateColorCodes('&', "&c✧&6" + String.valueOf(Damageindicator.roundDouble(e.getDamage(), 1)).replace(".0", "")));

                } else {
                    stand.setCustomName(ChatColor.translateAlternateColorCodes('&', "&7" + damage.replace(".0", "")));
                }
                NBTEntity nbtStand = new NBTEntity((Entity) stand);
                nbtStand.setBoolean("Invulnerable", Boolean.TRUE);
                stands.add(stand);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (!stand.isDead()) {
                            stand.remove();
                        }
                        if (stand.isDead()) {
                            stands.remove(stand);
                        }
                    }
                }, 20);
            }
        }
    }

    public boolean onClimb(Player player) {
        if (player.getLocation().getBlock().getType() == Material.LADDER || player.getLocation().getBlock().getType() == Material.VINE) {
            return true;
        }
        return false;
    }
}
