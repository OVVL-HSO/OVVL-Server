package com.tam.utils;

import com.tam.model.*;

public class CVEConvertUtil {
    public static AttackVectorResource buildAttackVectorEnumFromString(String attackVector) {
        switch (attackVector) {
            case "NETWORK":
                return AttackVectorResource.NETWORK;
            case "ADJACENT":
                return AttackVectorResource.ADJACENT_NETWORK;
            case "LOCAL":
                return AttackVectorResource.LOCAL;
            case "PHYSICAL":
                return AttackVectorResource.PHYSICAL;
        }
        return null;
    }

    public static AttackComplexityResource buildAttackComplexityEnumFromString(String attackComplexity) {
        switch (attackComplexity) {
            case "LOW":
                return AttackComplexityResource.LOW;
            case "HIGH":
                return AttackComplexityResource.HIGH;
        }
        return null;
    }

    public static RequiredPrivilegesResource buildRequriedPrivilegesEnumFromString(String requiredPrivileges) {
        switch (requiredPrivileges) {
            case "NONE":
                return RequiredPrivilegesResource.NONE;
            case "LOW":
                return RequiredPrivilegesResource.LOW;
            case "HIGH":
                return RequiredPrivilegesResource.HIGH;
        }
        return null;
    }

    public static UserInteractionResource buildUserInteractionEnumFromString(String userInteraction) {
        switch (userInteraction) {
            case "NONE":
                return UserInteractionResource.NONE;
            case "REQUIRED":
                return UserInteractionResource.REQUIRED;
        }
        return null;
    }

    public static ScopeResource buildScopeEnumFromString(String scope) {
        switch (scope) {
            case "UNCHANGED":
                return ScopeResource.UNCHANGED;
            case "CHANGED":
                return ScopeResource.CHANGED;
        }
        return null;
    }

    public static ImpactResource buildImpactEnumFromString(String impact) {
        switch (impact) {
            case "NONE":
                return ImpactResource.NONE;
            case "LOW":
                return ImpactResource.LOW;
            case "HIGH":
                return ImpactResource.HIGH;
        }
        return null;
    }
}
