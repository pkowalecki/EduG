package pl.kowalecki.edug.Model.Badges;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Badge {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("perc")
    @Expose
    private Integer perc;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("desc")
    @Expose
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPerc() {
        return perc;
    }

    public void setPerc(Integer perc) {
        this.perc = perc;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}