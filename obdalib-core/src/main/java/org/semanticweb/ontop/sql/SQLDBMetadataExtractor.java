package org.semanticweb.ontop.sql;


import com.google.inject.assistedinject.AssistedInject;
import net.sf.jsqlparser.JSQLParserException;
import org.semanticweb.ontop.injection.OBDAProperties;
import org.semanticweb.ontop.mapping.sql.SQLTableNameExtractor;
import org.semanticweb.ontop.model.OBDADataSource;
import org.semanticweb.ontop.model.OBDAModel;
import org.semanticweb.ontop.nativeql.DBConnectionWrapper;
import org.semanticweb.ontop.nativeql.DBMetadataException;
import org.semanticweb.ontop.nativeql.DBMetadataExtractor;
import org.semanticweb.ontop.sql.api.RelationJSQL;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SQLDBMetadataExtractor implements DBMetadataExtractor {

    /**
     * If we have to parse the full metadata or just the table list in the mappings.
     */
    private final Boolean obtainFullMetadata;

    @AssistedInject
    private SQLDBMetadataExtractor(OBDAProperties preferences) {
        this.obtainFullMetadata = preferences.getBoolean(OBDAProperties.OBTAIN_FULL_METADATA);
    }

    /**
     * Expects the DBConnection to be a JDBCConnection
     */
    @Override
    public DBMetadata extract(OBDADataSource dataSource, OBDAModel obdaModel, DBConnectionWrapper dbConnection,
                              @Nullable ImplicitDBConstraints userConstraints) throws DBMetadataException {
        boolean applyUserConstraints = (userConstraints != null);

        if (dbConnection == null) {
            throw new IllegalArgumentException("dbConnection is required by " + getClass().getCanonicalName());
        }

        Object abstractConnection = dbConnection.getConnection();

        if (!(abstractConnection instanceof Connection)) {
            throw new IllegalArgumentException("The connection must correspond to a java.sql.connection (" + getClass().getCanonicalName() + ")");
        }
        Connection connection = (Connection) abstractConnection;

        try {
            DBMetadata metadata;
            if (obtainFullMetadata) {
                 metadata = JDBCConnectionManager.getMetaData(connection);
            } else {
                // This is the NEW way of obtaining part of the metadata
                // (the schema.table names) by parsing the mappings

                // Parse mappings. Just to get the table names in use
                SQLTableNameExtractor mParser = new SQLTableNameExtractor(connection, obdaModel.getMappings(dataSource.getSourceID()));

                List<RelationJSQL> realTables = mParser.getRealTables();

                if (applyUserConstraints) {
                    // Add the tables referred to by user-supplied foreign keys
                    userConstraints.addReferredTables(realTables);
                }

                metadata = JDBCConnectionManager.getMetaData(connection, realTables);
            }

            //Adds keys from the text file
            if (userConstraints != null) {
                userConstraints.addConstraints(metadata);
            }

            return metadata;

        } catch (JSQLParserException e) {
            throw new DBMetadataException("Error obtaining the tables" + e);
        } catch (SQLException e) {
            throw new DBMetadataException("Error obtaining the Metadata" + e);
        }
    }
}
