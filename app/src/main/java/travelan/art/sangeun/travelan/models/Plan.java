package travelan.art.sangeun.travelan.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import travelan.art.sangeun.travelan.R;

public class Plan {
    // Plan
    public int id;
    public double order;
    public int travelId;
    public String attributeType;

    // accommodate, attraction
    public String title;
    public String address;
    public LatLng coordinates;

    // transport
    public String time;
    public String way;
    public String origin;
    public LatLng originCoordinates;
    public String route;
    public List<LatLng> polyline;
    public String destination;
    public LatLng destinationCoordinates;

    public String toString(){
        return "id:" + id + ", title : "+title+", address : "+address ;
    }

    public static final Plan getAddInstance(int travelId, double order) {
        Plan add = new Plan();
        add.id = -1;
        add.travelId = travelId;
        add.order = order;
        return add;
    }

    public void setAttributeTypeById(int id) {
        String attributeType = "";

        switch (id) {
            case R.id.transport:
                attributeType = "transportation";
                break;
            case R.id.accommodate:
                attributeType = "accomodate";
                break;
            case R.id.attraction:
                attributeType = "attraction";
                break;
        }

        this.attributeType = attributeType;
    }
}
