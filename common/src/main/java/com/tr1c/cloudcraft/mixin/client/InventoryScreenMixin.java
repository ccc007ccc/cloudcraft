package com.tr1c.cloudcraft.mixin.client;

import com.tr1c.cloudcraft.cloudtech.BackTankAccess;
import com.tr1c.cloudcraft.cloudtech.BackTankMenuSlot;
import com.tr1c.cloudcraft.cloudtech.BackTankScreenSupport;
import com.tr1c.cloudcraft.cloudtech.BackTankSlotUi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractContainerScreen<InventoryMenu> {
    protected InventoryScreenMixin(InventoryMenu menu, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.network.chat.Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void cloudcraft$updateBackTankOpenState(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BackTankMenuSlot slot = cloudcraft$getBackTankSlot();
        if (slot != null) {
            slot.setOpen(BackTankScreenSupport.isInventoryAreaHovered(this.leftPos, this.topPos, mouseX, mouseY));
        }
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void cloudcraft$renderBackTankBackground(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        BackTankMenuSlot slot = cloudcraft$getBackTankSlot();
        if (slot == null || !slot.isOpen()) {
            return;
        }
        BackTankSlotUi.render(guiGraphics, this.leftPos, this.topPos, BackTankScreenSupport.INVENTORY_SLOT_X, BackTankScreenSupport.INVENTORY_SLOT_Y);
    }


    @Unique
    private BackTankMenuSlot cloudcraft$getBackTankSlot() {
        return ((BackTankAccess) this.menu).cloudcraft$getBackTankSlot();
    }
}
