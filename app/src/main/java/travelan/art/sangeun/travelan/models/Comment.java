package travelan.art.sangeun.travelan.models;

public class Comment {
    public User user;
    public String content;

    public String toString(){
        return user.userId+" : "+content;
    }
}
