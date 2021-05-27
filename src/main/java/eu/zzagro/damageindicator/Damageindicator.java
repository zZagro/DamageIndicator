package eu.zzagro.damageindicator;

import eu.zzagro.damageindicator.listeners.DamageListener;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Damageindicator extends JavaPlugin{

    public DamageListener damageListener = new DamageListener(this);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);

    }

    @Override
    public void onDisable() {
        for (ArmorStand stand : damageListener.stands) {
            stand.remove();
        }
    }

    public static double roundDouble(double d, int places) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
