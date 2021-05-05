package pl.kowalecki.edug.Model.Files;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class FileData {

    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("filemane")
    @Expose
    private String filemane;
    @SerializedName("location")
    @Expose
    private String location;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilemane() {
        return filemane;
    }

    public void setFilemane(String filemane) {
        this.filemane = filemane;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "category='" + category + '\'' +
                ", filemane='" + filemane + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}