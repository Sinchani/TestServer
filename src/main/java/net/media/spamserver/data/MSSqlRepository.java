package net.media.spamserver.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.media.spamserver.exception.StoredProcedureCreationException;
import net.media.spamserver.model.StoredProcedure;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vivek on 7/29/15.
 */
@Repository
public class MSSqlRepository {
    @Autowired private Logger logger;
    @Autowired private DataSource mssqlDataSource;

    public String executeStatement(StoredProcedure storedProcedure, int index) throws StoredProcedureCreationException, SQLException {
        String spString = storedProcedure.buildStoredProcedure();
        CallableStatement callableStatement = null;
        SQLException sqlException = null;
        Connection connection = null;
        List<String> resultSetStrings = new LinkedList<String>();
        try {
            connection = mssqlDataSource.getConnection();
            callableStatement = connection.prepareCall(spString);
            boolean results = callableStatement.execute();
            while (results) {
                ResultSet resultSet = callableStatement.getResultSet();
                resultSetStrings.add(stringify(resultSet));
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Could not find close ResultSet");
                    sqlException = sqlException == null ? e : sqlException;
                }
                results = callableStatement.getMoreResults();
            }
        } catch (SQLException e) {
            logger.error("Could not execute query " + e.getMessage());
            sqlException = sqlException == null ? e : sqlException;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (callableStatement != null) {
                    callableStatement.close();
                }
            } catch (SQLException e) {
                logger.error("Could not find close SQL connection");
                sqlException = sqlException == null ? e : sqlException;
            }
        }
        if (sqlException != null) {
            throw sqlException;
        }
        if (resultSetStrings.size() > index) {
            return resultSetStrings.get(index);
        }
        return null;
    }

    public String stringify(ResultSet resultSet) throws SQLException {
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnCount = rsmd.getColumnCount();
        JsonArray resultJson = new JsonArray();
        while (resultSet.next()) {
            JsonObject rowObject = new JsonObject();
            for (int i = 1; i <= columnCount; i++) {
                rowObject.addProperty(rsmd.getColumnLabel(i), resultSet.getString(i));
            }
            resultJson.add(rowObject);
        }
        return (new Gson()).toJson(resultJson);
    }
}
