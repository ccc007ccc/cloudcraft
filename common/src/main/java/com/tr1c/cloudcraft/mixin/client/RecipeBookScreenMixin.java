package com.tr1c.cloudcraft.mixin.client;

import com.tr1c.cloudcraft.cloudtech.BackTankAccess;
import com.tr1c.cloudcraft.cloudtech.BackTankMenuSlot;
import com.tr1c.cloudcraft.cloudtech.BackTankScreenSupport;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.inventory.RecipeBookMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractRecipeBookScreen.class)
public abstract class RecipeBookScreenMixin<T extends RecipeBookMenu> extends AbstractContainerScreen<T> {
    protected RecipeBookScreenMixin(T menu, net.minecraft.world.entity.player.Inventory playerInventory, net.minecraft.network.chat.Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "hasClickedOutside", at = @At("HEAD"), cancellable = true)
    private void cloudcraft$keepBackTankInside(double mouseX, double mouseY, int leftPos, int topPos, CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof InventoryScreen)) {
            return;
        }
        if (!(this.menu instanceof BackTankAccess access)) {
            return;
        }
        BackTankMenuSlot slot = access.cloudcraft$getBackTankSlot();
        if (slot != null && BackTankScreenSupport.isBackTankHovered(
                this.leftPos,
                this.topPos,
                BackTankScreenSupport.INVENTORY_SLOT_X,
                BackTankScreenSupport.INVENTORY_SLOT_Y,
                mouseX,
                mouseY)) {
            cir.setReturnValue(false);
        }
    }
}
