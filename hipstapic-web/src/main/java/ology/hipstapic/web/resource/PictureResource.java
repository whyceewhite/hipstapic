package ology.hipstapic.web.resource;

import ology.hipstapic.service.domain.Picture;
import ology.hipstapic.service.db.SearchParameters;
import ology.hipstapic.service.db.PictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * The restful resource corresponding to the picture service.
 */
@Path("/picture")
@Produces("application/json")
public class PictureResource {

    private final static Logger logger = LoggerFactory.getLogger(PictureResource.class);

    /**
     * <p>
     * Searches for entries that contain all of the given tags.
     * </p>
     *
     * @param   tags The tags or key words that must be applied to the entries
     *          in order for them to be returned. An entry must have all given
     *          tags.
     * @param   page The page of results to return. If null then the first page
     *          of results is returned.
     * @param   pageSize The number of results to return within a page. If null
     *          then a default number of results is returned.
     * @return  A collection of entries that contain all of the tags provided.
     *          If no tags were given then all results are returned within the
     *          paging limits.
     */
    @GET
    @Path("/search")
    public Response search(
            @QueryParam("tags") List<String> tags,
            @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize) {

        logger.debug("search | tags param: {}", tags);
        logger.debug("search | page param: {}", page);
        logger.debug("search | pageSize param: {}", pageSize);

        SearchParameters parameters = new SearchParameters();
        parameters.setTags(tags);
        parameters.setPage(page);
        parameters.setPageSize(pageSize);

        try {
            List<Picture> results = PictureService.getInstance().search(parameters);
            return Response.status(Response.Status.OK).entity(results).build();
        } catch (Exception e) {
            logger.error("An error occurred while searching for pictures.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * <p>
     * Searches for entries that contain all of the given tags and returns the
     * number of results in the result set.
     * </p>
     *
     * @param   tags The tags or key words that must be applied to the entries
     *          in order for them to be returned. An entry must have all given
     *          tags.
     * @return  A count that indicates how many matching results exist for the
     *          set of tags provided.
     */
    @GET
    @Path("/search/count")
    public Response count(@QueryParam("tags") List<String> tags) {

        logger.debug("count | tags param: {}", tags);

        SearchParameters parameters = new SearchParameters();
        parameters.setTags(tags);

        try {
            Long resultCount = PictureService.getInstance().count(parameters);
            return Response.status(Response.Status.OK).entity(resultCount).build();
        } catch (Exception e) {
            logger.error("An error occurred while searching for pictures.", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
