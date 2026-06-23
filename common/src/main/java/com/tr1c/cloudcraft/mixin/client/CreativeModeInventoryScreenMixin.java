package com.tr1c.cloudcraft.mixin.client;

import com.tr1c.cloudcraft.cloudtech.BackTankAccess;
import com.tr1c.cloudcraft.cloudtech.BackTankMenuSlot;
import com.tr1c.cloudcraft.cloudtech.BackTankScreenSupport;
import com.tr1c.cloudcraft.cloudtech.BackTankSlotUi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends AbstractContainerScreen<AbstractContainerMenu> {
    @Shadow
    private boolean hasClickedOutside;

    protected CreativeModeInventoryScreenMixin(AbstractContainerMenu menu, Inventory playerInventory, net.minecraft.network.chat.Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void cloudcraft$positionBackTankSlot(CreativeModeTab tab, CallbackInfo ci) {
        Slot slot = cloudcraft$getVisibleBackTankSlot();
        Slot anchorSlot = BackTankScreenSupport.findBackTankAnchorSlot(this.menu);
        if (slot != null && anchorSlot != null) {
            ((SlotAccessor) slot).cloudcraft$setX(BackTankScreenSupport.expandedX(anchorSlot));
            ((SlotAccessor) slot).cloudcraft$setY(BackTankScreenSupport.expandedY(anchorSlot));
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void cloudcraft$updateBackTankOpenState(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BackTankMenuSlot slot = cloudcraft$getRealBackTankSlot();
        Slot visibleSlot = cloudcraft$getVisibleBackTankSlot();
        if (slot == null) {
            return;
        }
        slot.setOpen(BackTankScreenSupport.isCreativeAreaHovered(this.menu, this.leftPos, this.topPos, visibleSlot, mouseX, mouseY));
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void cloudcraft$renderBackTankBackground(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        BackTankMenuSlot slot = cloudcraft$getRealBackTankSlot();
        Slot visibleSlot = cloudcraft$getVisibleBackTankSlot();
        if (slot == null || !slot.isOpen() || visibleSlot == null) {
            return;
        }
        BackTankSlotUi.render(guiGraphics, this.leftPos, this.topPos, visibleSlot.x, visibleSlot.y);
    }

    @Inject(method = "hasClickedOutside", at = @At("HEAD"), cancellable = true)
    private void cloudcraft$keepBackTankInside(double mouseX, double mouseY, int leftPos, int topPos, CallbackInfoReturnable<Boolean> cir) {
        Slot visibleSlot = cloudcraft$getVisibleBackTankSlot();
        if (visibleSlot != null && BackTankScreenSupport.isBackTankHovered(this.leftPos, this.topPos, visibleSlot.x, visibleSlot.y, mouseX, mouseY)) {
            this.hasClickedOutside = false;
            cir.setReturnValue(false);
        }
    }

    @Unique
    private BackTankMenuSlot cloudcraft$getRealBackTankSlot() {
        if (this.minecraft == null || this.minecraft.player == null) {
            return null;
        }
        return ((BackTankAccess) this.minecraft.player.inventoryMenu).cloudcraft$getBackTankSlot();
    }

    @Unique
    private Slot cloudcraft$getVisibleBackTankSlot() {
        return BackTankScreenSupport.findVisibleBackTankSlot(this.menu);
    }

}
