package net.media.spamserver.services;

import net.media.spamserver.config.RedisConfig;
import net.media.spamserver.model.ClickDetails;
import org.springframework.stereotype.Service;

/**
 * Created by vivek on 7/31/15.
 * Because naming stuff is hard!
 */
@Service
public class CustomKeyNamingService {
    public enum KeyLevel {
        PARTNER, CUSTOMER, PORTFOLIO, CREATIVE;
        private static KeyLevel[] allValues = values();
        public static KeyLevel fromOrdinal(int n) {return allValues[n];}
    }

    public String getCustomKey(ClickDetails clickDetails, KeyLevel keyLevel, String prefix) {
        if (keyLevel.ordinal() == 3) {
            return prefix + clickDetails.getCreativeId();
        }
        if (keyLevel.ordinal() == 2) {
            return prefix + clickDetails.getPortfolioId();
        }
        if (keyLevel.ordinal() == 1) {
            return prefix + clickDetails.getCustomerId();
        }
        return prefix + clickDetails.getPartnerId();
    }

    public String getCustomSettingsKey(ClickDetails clickDetails, KeyLevel keyLevel) {
        return getCustomKey(clickDetails, keyLevel, RedisConfig.KEY_CUSTOM_VERIFIER_SETTING);
    }

    public String getCustomKeyFromEntityId(String entityId, String prefix) {
        return prefix + entityId;
    }

    public String getCustomSettingsKeyFromEntityId(String entityId) {
        return getCustomKeyFromEntityId(entityId, RedisConfig.KEY_CUSTOM_VERIFIER_SETTING);
    }
}
