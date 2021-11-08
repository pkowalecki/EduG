package pl.kowalecki.edug.Model.Files;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ListFile {

    @SerializedName("file")
    @Expose
    private FileData fileData;

    public FileData getFileData() {
        return fileData;
    }

    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "ListFile{" +
                "fileData=" + fileData +
                '}';
    }
}