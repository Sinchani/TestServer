package net.media.spamserver.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.media.spamserver.config.VerifierConfig;
import net.media.spamserver.data.CacheDAL;
import net.media.spamserver.data.DbDAL;
import net.media.spamserver.data.RedisRepository;
import net.media.spamserver.model.ClickDetails;
import net.media.spamserver.model.VerifierSetting;
import net.media.spamserver.verifiers.CustomVerifier;
import net.media.spamserver.verifiers.Verifier;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vivek on 3/5/15.
 */
@Service
public class EntityService {
    @Autowired private DbDAL dbDAL;
    @Autowired private RedisRepository redisRepository;
    @Autowired private CacheDAL cacheDAL;
    @Autowired private CustomKeyNamingService customKeyNamingService;
    @Autowired private Logger logger;
    @Autowired private ColoListService coloListService;

    public void refreshEntityVerifiers(boolean isCustomerEntity, String entityId) {
        String verifierSettings;
        if(isCustomerEntity) {
            verifierSettings = verifierSettingsListToString(dbDAL.getVerifiersFromCustomerId(entityId));
        } else {
            verifierSettings = verifierSettingsListToString(dbDAL.getVerifiersFromEntityId(entityId));
        }
        redisRepository.set(customKeyNamingService.getCustomSettingsKeyFromEntityId(entityId), verifierSettings);
    }

    private String verifierSettingsListToString(List<VerifierSetting> verifierSettings) {
        return new Gson().toJson(verifierSettings);
    }

    private List<VerifierSetting> getVerifierSettings(ClickDetails clickDetails, Integer level) {
        List<String> responses = redisRepository.mGet(customKeyNamingService.getCustomSettingsKey(clickDetails, CustomKeyNamingService.KeyLevel.CREATIVE),
                customKeyNamingService.getCustomSettingsKey(clickDetails, CustomKeyNamingService.KeyLevel.PORTFOLIO),
                customKeyNamingService.getCustomSettingsKey(clickDetails, CustomKeyNamingService.KeyLevel.CUSTOMER),
                customKeyNamingService.getCustomSettingsKey(clickDetails, CustomKeyNamingService.KeyLevel.PARTNER));
        List<VerifierSetting> verifierSettings = new ArrayList<>();
        List<VerifierSetting> stickySettings = new ArrayList<>();
        for(String response : responses) {
            if (response != null && !response.equals("")) {
                try {
                    List<VerifierSetting> settings = getVerifierSettingFromResponse(response);
                    if (verifierSettings.size() == 0) {
                        verifierSettings = settings;
                    } else {
                        stickySettings.addAll(getStickyVerifiers(settings));
                    }
                } catch (Exception e) {
                    logger.info("Can't covert to json the given string: " + response);
                }
                level++;
            }
        }
        verifierSettings.addAll(stickySettings);
        return verifierSettings;
    }

    private ArrayList<VerifierSetting> getStickyVerifiers(List<VerifierSetting> settings) {
        ArrayList<VerifierSetting> stickyVerifierSettings = new ArrayList<>();
        for (VerifierSetting setting : settings) {
            if (setting.isSticky()) {
                stickyVerifierSettings.add(setting);
            }
        }
        return stickyVerifierSettings;
    }

    private List<VerifierSetting> getVerifierSettingFromResponse(String response) {
        Gson gson = new Gson();
        return gson.fromJson(response, new TypeToken<ArrayList<VerifierSetting>>(){}.getType());
    }

    public List<Verifier> getVerifierList(ClickDetails clickDetails) {
        Integer level = 0;
        List<VerifierSetting> verifierSettings = getVerifierSettings(clickDetails, level);
        List<Verifier> verifiers = new ArrayList<Verifier>();
        if (verifierSettings == null || verifierSettings.size() == 0) {
            return null;
        }
        CustomKeyNamingService.KeyLevel keyPrefix = CustomKeyNamingService.KeyLevel.fromOrdinal(level);
        for (VerifierSetting verifierSetting : verifierSettings) {
            verifiers.add(getVerifierFromSetting(verifierSetting, clickDetails, keyPrefix));
        }
        return verifiers;
    }

    private Verifier getVerifierFromSetting(VerifierSetting verifierSetting, ClickDetails clickDetails, CustomKeyNamingService.KeyLevel keyLevel) {
        if (!VerifierConfig.shortFormVerifiers.containsKey(verifierSetting.getVerifierName())) {
            CustomVerifier customVerifier = new CustomVerifier();
            List<String> values = new LinkedList<>();
            for (String key : verifierSetting.getKeys()) {
                values.add(clickDetails.getStringParam(key));
            }
            customVerifier.setKeys(verifierSetting.getKeys());
            customVerifier.setValues(values);
            customVerifier.setReturnStatus(verifierSetting.getReturnStatus());
            customVerifier.setExpiry(verifierSetting.getExpiry());
            customVerifier.setThreshold(verifierSetting.getThreshold());
            customVerifier.setCacheDAL(cacheDAL);
            customVerifier.setKeyPrefix(customKeyNamingService.getCustomKey(clickDetails, keyLevel, ""));
            return customVerifier;
        }
        Verifier verifier = null;
        try {
            verifier = (Verifier) VerifierConfig.shortFormVerifiers.get(verifierSetting.getVerifierName()).newInstance();
            verifier.setExpiry(verifierSetting.getExpiry());
            verifier.setCacheDAL(cacheDAL);
            verifier.setThreshold(verifierSetting.getThreshold());
            verifier.setColoListService(coloListService);
        } catch (InstantiationException | IllegalAccessException ignored) {}
        return verifier;
    }

    public void refreshEntitiesVerifier(boolean isCustomer, List<String> entities) {
        for (String entity : entities) {
            try {
                refreshEntityVerifiers(isCustomer, entity);
            } catch (Exception ignored) {}
        }
    }
}
