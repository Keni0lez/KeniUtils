package org.keni.keniutils.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            if (isInvisible(killer)) {
                String newDeathMessage = victim.getName() + " was killed by §k?????";
                event.setDeathMessage(newDeathMessage);
            }
        }
    }

    private boolean isInvisible(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.INVISIBILITY)) {
                return true;
            }
        }
        return false;
    }
}
