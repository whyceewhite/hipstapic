package ology.hipstapic.service.db;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import ology.hipstapic.service.domain.Picture;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public class PictureService {

    private final static Logger logger = LoggerFactory.getLogger(PictureService.class);
    private final static PictureService service;
    private final static int DEFAULT_SKIP = 0;
    private final static int DEFAULT_LIMIT = 20;

    private DBCollection collection;

    static {
        service = new PictureService();
    }

    private PictureService() {
        collection = DatabaseClient.getDB().getCollection("picture");
    }

    public static PictureService getInstance() {
        return service;
    }

    /**
     * <p>
     * If the given picture entry is new then it is created and saved to the
     * database. Otherwise, the entry is updated.
     * </p>
     *
     * @param   picture Creates or updates the given picture entry.
     */
    public void save(Picture picture) {

        boolean isNew = false;
        WriteResult result;

        if (picture.getId() == null) {
            picture.setId(new ObjectId().toString());
            isNew = true;
        }
        DBObject dbObj = (DBObject) JSON.parse(picture.toJson());

        result = isNew ?
                collection.insert(dbObj, WriteConcern.ACKNOWLEDGED) :
                collection.update(new BasicDBObject("_id", picture.getId()), dbObj);

        logger.debug("The save result for picture id #{}: {}", picture.getId(), result);
    }

    /**
     * <p>
     * Searches the items that match the given search parameters. The number
     * and page of items returned is dictated by the page and pageSize values
     * in the parameters.
     * </p>
     *
     * @param   parameters The search parameters for finding the desired result
     *          set. Required.
     * @return  The items matching the search criteria. A maximum number of
     *          results returned is determined by the pageSize value.
     */
    public List<Picture> search(SearchParameters parameters) {

        logger.debug("Search parameters: {}", parameters);

        ArrayList<Picture> results = new ArrayList<>();

        int limit = (parameters.getPageSize() != null && parameters.getPage() > 0) ?
                parameters.getPageSize() :
                DEFAULT_LIMIT;

        int skip = (parameters.getPage() != null && parameters.getPage() > 0) ?
                (parameters.getPage() - 1) * limit :
                DEFAULT_SKIP;

        DBCursor cursor = collection.find(createSearchObject(parameters))
                .sort(new BasicDBObject("createTimestamp", 1))
                .skip(skip).limit(limit);
        Iterator<DBObject> iterator = cursor.iterator();
        int resultCount = 0;

        while (iterator.hasNext()) {
            String picJson = iterator.next().toString();
            logger.trace("Search result {}: {}", ++resultCount, picJson);
            results.add(Picture.toObject(picJson));
        }
        cursor.close();

        logger.debug("Search results count: {}", resultCount);

        return results;
    }

    /**
     * <p>
     * Count the number of items that would be returned by the given search
     * parameters.
     * </p>
     *
     * @param   parameters The search parameters for finding the desired result
     *          set. The page and pageSize values are ignored.
     * @return  The number of items matching the search parameters.
     */
    public long count(SearchParameters parameters) {

        return collection.count(createSearchObject(parameters));
    }

    /**
     * <p>
     * Given the search parameters, returns a DBObject that will establish the
     * search critieria.
     * </p>
     *
     * @param   parameters The search criteria.
     * @return  The DBObject that constructs the criteria in the proper format
     *          for querying.
     */
    private DBObject createSearchObject(SearchParameters parameters) {

        DBObject query = new BasicDBObject();

        if (parameters != null && parameters.getTags() != null && !parameters.getTags().isEmpty()) {
            BasicDBList tagQueryList = new BasicDBList();
            for (String tag : parameters.getTags()) {
                if (tag != null && !tag.isEmpty()) tagQueryList.add(tag);
            }
            if (!tagQueryList.isEmpty()) {
                query.put("tags", new BasicDBObject("$all", tagQueryList));
            }
        }

        logger.debug("Query object: {}", query);

        return query;
    }
}
