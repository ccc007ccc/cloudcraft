package com.tr1c.cloudcraft.item.custom;

import com.tr1c.cloudcraft.cloudtech.BackTankSlot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class JetpackItem extends Item {
    public JetpackItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        ItemStack stack = player.getItemInHand(hand);
        ItemStack original = stack.copy();
        BackTankSlot.equip(player, stack);
        return ItemStack.matches(original, stack) ? InteractionResult.PASS : InteractionResult.SUCCESS_SERVER;
    }
}
