package travelan.art.sangeun.travelan.models;

public class Plan {
    public int id;
    public double order;
    public String title;
    public String address;
    public String tel;
    public String time;
    public String way;
    public String origin;
    public String route;
    public String destination;
    public String label;
    public String attributeType;

    public String toString(){
        return "id:" + id + ", title : "+title+", address : "+address ;
    }

    public static final Plan getAddInstance(double order) {
        Plan add = new Plan();
        add.id = -1;
        add.order = order;
        return add;
    }
}
