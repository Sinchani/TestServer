package net.media.spamserver.data;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.media.spamserver.config.CMStoredProceduresAndParameters;
import net.media.spamserver.exception.StoredProcedureCreationException;
import net.media.spamserver.model.StoredProcedure;
import net.media.spamserver.model.VerifierSetting;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

/**
 * Created by vivek on 3/17/15.
 */
@Repository
public class DbDAL implements CMStoredProceduresAndParameters {
    @Autowired private MSSqlRepository msSqlRepository;
    @Autowired private Logger logger;
    private static final String SETTING_ATTRIBUTE_NAME = "spam_server_custom_settings";

    private List<VerifierSetting> executeSPAndHandleExceptions(StoredProcedure storedProcedure, int index) {
        try {
            return convertResponseToList(msSqlRepository.executeStatement(storedProcedure, index));
        } catch (StoredProcedureCreationException e) {
            logger.info("Can't get settings from settings from PartnerId as SP creation failed because: " + e.getMessage());
        } catch (SQLException e) {
            logger.info("Can't get settings from settings from PartnerId as SQLException was thrown because: " + e.getMessage());
        }
        return null;
    }

    private List<VerifierSetting> convertResponseToList(String response) {
        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();
        JsonArray jsonElements = jsonParser.parse(response).getAsJsonArray();
        for(JsonElement jsonElement : jsonElements) {
            if (jsonElement.getAsJsonObject().get("attribute_name").getAsString().equals(SETTING_ATTRIBUTE_NAME)) {
                return gson.fromJson(jsonElement.getAsJsonObject().get("attribute_value").getAsString(), new TypeToken<List<VerifierSetting>>(){}.getType());
            }
        }
        return null;
    }

    public List<VerifierSetting> getVerifiersFromCustomerId(String customerId) {
        StoredProcedure storedProcedure = new StoredProcedure().setName(CM_GET_ENTITY_ATTRIBUTE_DETAILS)
                .addStringParameter(PARAM_SEARCH_BY_CUSTOMER, customerId);
       return executeSPAndHandleExceptions(storedProcedure, 0);
    }

    public List<VerifierSetting> getVerifiersFromEntityId(String entityId) {
        StoredProcedure storedProcedure = new StoredProcedure().setName(CM_GET_ENTITY_ATTRIBUTE_DETAILS)
                .addStringParameter(PARAM_ENTITY_ID, entityId);
        return executeSPAndHandleExceptions(storedProcedure, 0);
    }
}
