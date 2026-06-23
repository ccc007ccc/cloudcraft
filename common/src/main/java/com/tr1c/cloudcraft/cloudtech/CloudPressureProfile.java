package com.tr1c.cloudcraft.cloudtech;

import com.tr1c.cloudcraft.progression.CloudTier;
import com.tr1c.cloudcraft.registry.ModIds;

public enum CloudPressureProfile {
    CUMULUS(ModIds.CUMULUS_CLOUD_BLOCK, ModIds.CUMULUS_CLOUD_BLOCK_GAS, CloudTier.CUMULUS, CompressedAirRules.CUMULUS_RECHARGE),
    STRATUS(ModIds.STRATUS_CLOUD_BLOCK, ModIds.STRATUS_CLOUD_BLOCK_GAS, CloudTier.STRATUS, CompressedAirRules.STRATUS_RECHARGE),
    CIRRUS(ModIds.CIRRUS_CLOUD_BLOCK, ModIds.CIRRUS_CLOUD_BLOCK_GAS, CloudTier.CIRRUS, CompressedAirRules.CIRRUS_RECHARGE),
    ALTOSTRATUS(ModIds.ALTOSTRATUS_CLOUD_BLOCK, ModIds.ALTOSTRATUS_CLOUD_BLOCK_GAS, CloudTier.ALTOSTRATUS, CompressedAirRules.ALTOSTRATUS_RECHARGE),
    NIMBOSTRATUS(ModIds.NIMBOSTRATUS_CLOUD_BLOCK, ModIds.NIMBOSTRATUS_CLOUD_BLOCK_GAS, CloudTier.NIMBOSTRATUS, CompressedAirRules.NIMBOSTRATUS_RECHARGE),
    CUMULONIMBUS(ModIds.CUMULONIMBUS_CLOUD_BLOCK, ModIds.CUMULONIMBUS_CLOUD_BLOCK_GAS, CloudTier.CUMULONIMBUS, CompressedAirRules.CUMULONIMBUS_RECHARGE);

    private final String solidCloudId;
    private final String gasCloudId;
    private final CloudTier tier;
    private final int rechargeRate;

    CloudPressureProfile(String solidCloudId, String gasCloudId, CloudTier tier, int rechargeRate) {
        this.solidCloudId = solidCloudId;
        this.gasCloudId = gasCloudId;
        this.tier = tier;
        this.rechargeRate = rechargeRate;
    }

    public String cloudId() {
        return gasCloudId;
    }

    public String solidCloudId() {
        return solidCloudId;
    }

    public String gasCloudId() {
        return gasCloudId;
    }

    public CloudTier tier() {
        return tier;
    }

    public int rechargeRate() {
        return rechargeRate;
    }

    public static CloudPressureProfile find(String cloudId) {
        for (CloudPressureProfile profile : values()) {
            if (profile.matches(cloudId)) {
                return profile;
            }
        }
        return null;
    }

    private boolean matches(String cloudId) {
        return solidCloudId.equals(cloudId) || gasCloudId.equals(cloudId);
    }
}
