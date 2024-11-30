package me.t3sl4.hydraulic.utils.database.Model.File;

public class FileDescription {
    private final String description;
    private final String path;

    public FileDescription(String description, String path) {
        this.description = description;
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }
}

