package com.tr1c.cloudcraft.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CloudWispEntity extends Ghast {
    private static final double DRIFT_SPEED = 0.015;
    private static final double DRIFT_DAMPING = 0.92;
    private static final int DRIFT_INTERVAL_TICKS = 30;

    public CloudWispEntity(EntityType<? extends Ghast> entityType, Level level) {
        super(entityType, level);
        setNoGravity(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Ghast.createAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.FOLLOW_RANGE, 12.0)
                .add(Attributes.FLYING_SPEED, 0.035)
                .add(Attributes.MOVEMENT_SPEED, 0.035);
    }

    @Override
    protected void registerGoals() {
        // Cloud wisps are passive ambient creatures. Dedicated AI comes after the entity pipeline is stable.
    }

    @Override
    public void aiStep() {
        super.aiStep();
        setNoGravity(true);

        if (!level().isClientSide()) {
            Vec3 movement = getDeltaMovement().scale(DRIFT_DAMPING);
            if (tickCount % DRIFT_INTERVAL_TICKS == 0) {
                movement = movement.add(
                        (getRandom().nextDouble() - 0.5) * DRIFT_SPEED,
                        (getRandom().nextDouble() - 0.45) * DRIFT_SPEED * 0.6,
                        (getRandom().nextDouble() - 0.5) * DRIFT_SPEED);
            }
            setDeltaMovement(movement);
        }
    }

    @Override
    public boolean isCharging() {
        return false;
    }

    @Override
    public void setCharging(boolean charging) {
    }

    @Override
    public int getExplosionPower() {
        return 0;
    }
}
