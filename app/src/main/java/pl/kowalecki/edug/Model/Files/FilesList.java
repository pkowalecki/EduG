package pl.kowalecki.edug.Model.Files;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class FilesList {

    @SerializedName("list_files")
    @Expose
    private List<ListFile> listFiles = null;

    public List<ListFile> getListFiles() {
        return listFiles;
    }

    public void setListFiles(List<ListFile> listFiles) {
        this.listFiles = listFiles;
    }


    @Override
    public String toString() {
        return "FilesList{" +
                "listFiles=" + listFiles +
                '}';
    }
}
