package com.tr1c.cloudcraft.mixin;

import com.tr1c.cloudcraft.cloudtech.BackTankAccess;
import com.tr1c.cloudcraft.cloudtech.BackTankService;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow
    @Final
    public ServerPlayer player;

    @Inject(method = "handleSetCreativeModeSlot", at = @At("HEAD"), cancellable = true)
    private void cloudcraft$handleBackTankCreativeSlot(ServerboundSetCreativeModeSlotPacket packet, CallbackInfo ci) {
        if (!(this.player.inventoryMenu instanceof BackTankAccess access)) {
            return;
        }
        int backTankSlotIndex = access.cloudcraft$getBackTankSlotIndex();
        if (backTankSlotIndex < 0 || access.cloudcraft$getBackTankSlot() == null || packet.slotNum() != backTankSlotIndex) {
            return;
        }
        ItemStack stack = packet.itemStack();
        if (!stack.isEmpty() && !access.cloudcraft$getBackTankSlot().mayPlace(stack)) {
            ci.cancel();
            return;
        }
        BackTankService.set(this.player, stack);
        this.player.inventoryMenu.broadcastChanges();
        ci.cancel();
    }
}
