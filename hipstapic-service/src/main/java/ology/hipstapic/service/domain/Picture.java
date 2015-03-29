package ology.hipstapic.service.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

/**
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("_id")
    private String id;
    private String title;
    private String filename;
    private List<String> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean addTag(String tag) {

        if (tag == null || tag.isEmpty()) {
            return false;
        }
        if (tags == null) {
            tags = new ArrayList<String>();
        }
        return tags.add(tag);
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Picture toObject(String json) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(json, Picture.class);
    }

    public String toString() {
        return toJson();
    }

}
