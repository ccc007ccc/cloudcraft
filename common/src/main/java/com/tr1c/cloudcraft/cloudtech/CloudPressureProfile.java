package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.progression.CloudTier;
import com.tr1c.cloudcraft.registry.ModIds;

public enum CloudPressureProfile {
    CUMULUS(ModIds.CUMULUS_CLOUD_BLOCK_GAS, CloudTier.CUMULUS, CompressedAirRules.IDLE_RECHARGE),
    STRATUS(ModIds.STRATUS_CLOUD_BLOCK_GAS, CloudTier.STRATUS, CompressedAirRules.IDLE_RECHARGE),
    CIRRUS(ModIds.CIRRUS_CLOUD_BLOCK_GAS, CloudTier.CIRRUS, CompressedAirRules.CIRRUS_RECHARGE),
    ALTOSTRATUS(ModIds.ALTOSTRATUS_CLOUD_BLOCK_GAS, CloudTier.ALTOSTRATUS, CompressedAirRules.ALTOSTRATUS_RECHARGE),
    NIMBOSTRATUS(ModIds.NIMBOSTRATUS_CLOUD_BLOCK_GAS, CloudTier.NIMBOSTRATUS, CompressedAirRules.NIMBOSTRATUS_RECHARGE),
    CUMULONIMBUS(ModIds.CUMULONIMBUS_CLOUD_BLOCK_GAS, CloudTier.CUMULONIMBUS, CompressedAirRules.CUMULONIMBUS_RECHARGE);

    private final String cloudId;
    private final CloudTier tier;
    private final int rechargeRate;

    CloudPressureProfile(String cloudId, CloudTier tier, int rechargeRate) {
        this.cloudId = cloudId;
        this.tier = tier;
        this.rechargeRate = rechargeRate;
    }

    public String cloudId() {
        return cloudId;
    }

    public CloudTier tier() {
        return tier;
    }

    public int rechargeRate() {
        return rechargeRate;
    }

    public static CloudPressureProfile find(String cloudId) {
        for (CloudPressureProfile profile : values()) {
            if (profile.cloudId.equals(cloudId)) {
                return profile;
            }
        }
        return null;
    }
}
