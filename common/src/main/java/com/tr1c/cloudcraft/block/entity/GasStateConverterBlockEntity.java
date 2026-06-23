package com.tr1c.cloudcraft.block.entity;

import com.tr1c.cloudcraft.block.custom.GasStateConverterOperation;
import com.tr1c.cloudcraft.registry.CloudCraftBlockEntityDefinitions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class GasStateConverterBlockEntity extends BlockEntity {
    private static final String LAST_OPERATION_KEY = "last_operation";
    private static final String LAST_CONVERTED_COUNT_KEY = "last_converted_count";
    private static final String TOTAL_CONVERTED_COUNT_KEY = "total_converted_count";

    private GasStateConverterOperation lastOperation;
    private int lastConvertedCount;
    private int totalConvertedCount;

    public GasStateConverterBlockEntity(BlockPos pos, BlockState blockState) {
        super(CloudCraftBlockEntityDefinitions.gasStateConverterType(), pos, blockState);
    }

    public void recordConversion(GasStateConverterOperation operation, int convertedCount) {
        lastOperation = operation;
        lastConvertedCount = convertedCount;
        totalConvertedCount += convertedCount;
        setChanged();
    }

    public GasStateConverterOperation lastOperation() {
        return lastOperation;
    }

    public int lastConvertedCount() {
        return lastConvertedCount;
    }

    public int totalConvertedCount() {
        return totalConvertedCount;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        lastOperation = GasStateConverterOperation.byId(input.getStringOr(LAST_OPERATION_KEY, ""));
        lastConvertedCount = Math.max(0, input.getIntOr(LAST_CONVERTED_COUNT_KEY, 0));
        totalConvertedCount = Math.max(0, input.getIntOr(TOTAL_CONVERTED_COUNT_KEY, 0));
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (lastOperation != null) {
            output.putString(LAST_OPERATION_KEY, lastOperation.id());
        }
        output.putInt(LAST_CONVERTED_COUNT_KEY, lastConvertedCount);
        output.putInt(TOTAL_CONVERTED_COUNT_KEY, totalConvertedCount);
    }
}
