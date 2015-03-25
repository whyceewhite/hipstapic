package ology.hipstapic.service.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import ology.hipstapic.domain.Picture;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 */
public class PictureService {

    private final static Logger logger = LoggerFactory.getLogger(PictureService.class);
    private final static PictureService service;
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
}
