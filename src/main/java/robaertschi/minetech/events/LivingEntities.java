package robaertschi.minetech.events;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import robaertschi.minetech.Minetech;
import robaertschi.minetech.item.custom.modulararmor.ModularArmorItem;

@Mod.EventBusSubscriber(modid = Minetech.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEntities {

    @SubscribeEvent
    public static void onItemMoved(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack previousArmor = event.getFrom();
            ItemStack newArmor = event.getTo();
            if (newArmor.getItem() instanceof ModularArmorItem) {
                ((ModularArmorItem) newArmor.getItem()).onEquip(newArmor, player);
            } else if (previousArmor.getItem() instanceof ModularArmorItem) {
                ((ModularArmorItem) previousArmor.getItem()).onUnequip(previousArmor, player);
            }
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player && event.getSource() == player.damageSources().fall()) {
            for (ItemStack stack :
                    player.getInventory().armor) {
                if (stack.getItem() instanceof ModularArmorItem item && item.getType() == ArmorItem.Type.BOOTS) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
