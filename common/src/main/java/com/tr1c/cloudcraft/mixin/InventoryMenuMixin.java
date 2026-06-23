package com.tr1c.cloudcraft.mixin;

import com.tr1c.cloudcraft.cloudtech.BackTankAccess;
import com.tr1c.cloudcraft.cloudtech.BackTankContainer;
import com.tr1c.cloudcraft.cloudtech.BackTankMenuSlot;
import com.tr1c.cloudcraft.cloudtech.CloudTechItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin implements BackTankAccess {
    @Unique
    private static final int CLOUDCRAFT_BACK_TANK_X = -11;
    @Unique
    private static final int CLOUDCRAFT_BACK_TANK_Y = 25;
    @Unique
    private BackTankContainer cloudcraft$backTankContainer;
    @Unique
    private BackTankMenuSlot cloudcraft$backTankSlot;
    @Unique
    private int cloudcraft$backTankSlotIndex = -1;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void cloudcraft$addBackTankSlot(Inventory inventory, boolean active, Player owner, CallbackInfo ci) {
        cloudcraft$backTankContainer = new BackTankContainer(owner);
        cloudcraft$backTankSlot = new BackTankMenuSlot(cloudcraft$backTankContainer, CLOUDCRAFT_BACK_TANK_X, CLOUDCRAFT_BACK_TANK_Y);
        cloudcraft$backTankSlot.setOpen(false);
        ((AbstractContainerMenuInvoker) this).cloudcraft$invokeAddSlot(cloudcraft$backTankSlot);
        cloudcraft$backTankSlotIndex = ((InventoryMenu) (Object) this).slots.indexOf(cloudcraft$backTankSlot);
    }

    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void cloudcraft$quickMoveBackTank(Player player, int slotIndex, CallbackInfoReturnable<ItemStack> cir) {
        if (cloudcraft$backTankSlot == null || cloudcraft$backTankSlotIndex < 0) {
            return;
        }
        InventoryMenu menu = (InventoryMenu) (Object) this;
        AbstractContainerMenuInvoker menuInvoker = (AbstractContainerMenuInvoker) this;
        Slot slot = menu.getSlot(slotIndex);
        if (!slot.hasItem()) {
            return;
        }

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (slotIndex == cloudcraft$backTankSlotIndex) {
            if (!menuInvoker.cloudcraft$invokeMoveItemStackTo(stack, InventoryMenu.INV_SLOT_START, InventoryMenu.USE_ROW_SLOT_END + 1, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        } else if (CloudTechItems.isJetpack(stack) && !cloudcraft$backTankSlot.hasItem()) {
            if (!menuInvoker.cloudcraft$invokeMoveItemStackTo(stack, cloudcraft$backTankSlotIndex, cloudcraft$backTankSlotIndex + 1, false)) {
                cir.setReturnValue(ItemStack.EMPTY);
                return;
            }
        } else {
            return;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY, original);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }
        slot.onTake(player, stack);
        cir.setReturnValue(original);
    }

    @Override
    public BackTankMenuSlot cloudcraft$getBackTankSlot() {
        return cloudcraft$backTankSlot;
    }

    @Override
    public int cloudcraft$getBackTankSlotIndex() {
        return cloudcraft$backTankSlotIndex;
    }

}
