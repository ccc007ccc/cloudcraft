package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.config.CloudCraftConfig;
import com.tr1c.cloudcraft.progression.CloudProgressionRules;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class JetpackStackState {
    private static final String PRESSURE_KEY = "pressure";

    private JetpackStackState() {
    }

    public static int getPressure(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return CompressedAirRules.clamp(data.copyTag().getIntOr(PRESSURE_KEY, 0), maxPressure(stack));
    }

    public static void setPressure(ItemStack stack, int pressure) {
        int maxPressure = maxPressure(stack);
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt(PRESSURE_KEY, CompressedAirRules.clamp(pressure, maxPressure)));
    }

    private static int maxPressure(ItemStack stack) {
        if (!CloudTechItems.isJetpack(stack)) {
            return CloudCraftConfig.scalePressureCapacity(CompressedAirRules.MAX_PRESSURE);
        }
        return CloudProgressionRules.maxPressure(CloudTechItems.jetpackId(stack));
    }
}
