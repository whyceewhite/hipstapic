package ology.hipstapic.service.db;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.Properties;

import com.mongodb.DBObject;
import ology.hipstapic.service.domain.Picture;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;


/**
 * Provides access to the database client so that data inserts, updates, reads
 * and deletes may occur for this application. To gain access to the client use
 * the {#getDB} method.
 * </p>
 *
 * This client assumes that a properties file containing the database
 * configuration settings are established. The following properties may be
 * configured:
 *
 * <ul>
 * <li>db.host - The mongodb service host</li>
 * <li>db.port - The mongodb service port</li>
 * <li>db.name - The mongodb database name</li>
 * </ul>
 */
public class DatabaseClient {

    protected final static Logger logger = LoggerFactory.getLogger(DatabaseClient.class);

    private final static DatabaseClient databaseClient;

    private MongoClient client;
    private String databaseHost;
    private String databaseName;
    private Integer databasePort;
    private Morphia mapper;

    static {
        databaseClient = new DatabaseClient();
    }

    private DatabaseClient() {

        loadProperties();
        initializeMongoClient();
        initializeMapper();
    }

    /**
     * The database client associated with this application.
     *
     * @return  The database client associated with this application.
     */
    static DB getDB() {
        return databaseClient.getMongoDB();
    }

    /**
     * Converts the DBObject instance to an instance with type specified by the
     * entityClass parameter.
     *
     * @param   entityClass The class that the returned instance will be.
     *          Required.
     * @param   dbObject The DBObject instance that is to be converted into its
     *          representative entityClass type. Required.
     * @return  An instance of type T that contains the values from dbObject.
     */
    static <T> T fromDBObject(final Class<T> entityClass, final DBObject dbObject) {
        return databaseClient.mapper.fromDBObject(entityClass, dbObject);
    }

    /**
     * Converts a pojo instance into a DBObject instance.
     *
     * @param   entity The instance to convert into a DBObject. Required.
     * @return  The DBObject representation of the entity parameter.
     */
    static DBObject toDBObject(final Object entity) {
        return databaseClient.mapper.toDBObject(entity);
    }

    /**
     * Loads the configuration properties that are needed to initialize the
     * database client.
     */
    private void loadProperties() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = getClass().getResourceAsStream("/config.properties");
            prop.load(input);

            if (logger.isDebugEnabled()) {
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(byteStream);
                prop.list(printStream);
                logger.debug(byteStream.toString());
            }

            databaseHost = prop.getProperty("db.host", "localhost");
            databaseName = prop.getProperty("db.name", "hipstapic");
            databasePort = new Integer(prop.getProperty("db.port", "27017"));

        } catch (Exception e) {
            logger.error("An error occurred when accessing and reading the configuration properties.", e);
            throw new RuntimeException("An error occurred when accessing and reading the configuration properties.");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("An error occurred when closing the input stream for the configuration properties.");
                }
            }
        }
    }

    /**
     * Initializes the mongodb client. This initialization assumes that the database
     * properties are set.
     */
    private void initializeMongoClient() {

        try {
            client = new MongoClient(databaseHost, databasePort);
        } catch (UnknownHostException e) {
            logger.error("", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the MongoDB object mapper and adds project entities that are
     * included in the mapping.
     */
    private void initializeMapper() {

        mapper = new Morphia();
        mapper.map(Picture.class);
    }

    /**
     * Returns the MongoDB client associated with the database of this
     * application.
     *
     * @return  The MongoDB client database for this application.
     */
    private DB getMongoDB() {
        return databaseClient.client.getDB(databaseName);
    }
}