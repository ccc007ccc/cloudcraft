package com.tr1c.cloudcraft.mixin.client;

import com.tr1c.cloudcraft.cloudtech.BackTankContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Unique
    private static final float CLOUDCRAFT_BACK_TANK_ITEM_SCALE = 0.875F;

    @Inject(method = "renderSlot", at = @At("HEAD"), cancellable = true)
    private void cloudcraft$renderScaledBackTankItem(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!(slot.container instanceof BackTankContainer) || slot.getItem().isEmpty()) {
            return;
        }
        ItemStack stack = slot.getItem();
        Matrix3x2fStack pose = guiGraphics.pose();
        float inset = (16.0F - 16.0F * CLOUDCRAFT_BACK_TANK_ITEM_SCALE) / 2.0F;
        pose.pushMatrix();
        pose.translate(slot.x + inset, slot.y + inset);
        pose.scale(CLOUDCRAFT_BACK_TANK_ITEM_SCALE, CLOUDCRAFT_BACK_TANK_ITEM_SCALE);
        guiGraphics.renderItem(stack, 0, 0);
        pose.popMatrix();
        guiGraphics.renderItemDecorations(((AbstractContainerScreenAccessor) this).cloudcraft$getFont(), stack, slot.x, slot.y);
        ci.cancel();
    }
}
