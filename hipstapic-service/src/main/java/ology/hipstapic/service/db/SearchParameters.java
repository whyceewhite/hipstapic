package ology.hipstapic.service.db;

import com.google.gson.Gson;

import java.util.List;

/**
 * Parameters used for searching.
 */
public class SearchParameters {

    private List<String> tags;
    private Integer pageSize;
    private Integer page;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
