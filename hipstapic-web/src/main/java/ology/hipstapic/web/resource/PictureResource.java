package ology.hipstapic.web.resource;

import ology.hipstapic.domain.Picture;
import ology.hipstapic.domain.SearchParameters;
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

    @GET
    @Path("/search")
    public Response search(
            @QueryParam("tag") List<String> tags,
            @QueryParam("page") Integer page,
            @QueryParam("pageSize") Integer pageSize) {

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

    @GET
    @Path("/search/count")
    public Response count(@QueryParam("tag") List<String> tags) {

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
