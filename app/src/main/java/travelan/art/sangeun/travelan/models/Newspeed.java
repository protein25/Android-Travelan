package travelan.art.sangeun.travelan.models;

import java.util.List;

public class Newspeed {
    public String location;
    public boolean isFav;
    public List<String> images;
    public User user;
    public String contents;
    public int planId;

    public String toString() {
        return "location: " + location + ", isFav: " + (isFav ? "true" : "false") + ", contents: " + contents;
    }
}
