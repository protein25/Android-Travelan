package travelan.art.sangeun.travelan.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.PlanFragment;
import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.dialog.SelectTravelDialog;
import travelan.art.sangeun.travelan.models.MapLocation;
import travelan.art.sangeun.travelan.dialog.MapLocationDialog;
import travelan.art.sangeun.travelan.utils.Alert;
import travelan.art.sangeun.travelan.utils.ApiClient;
import travelan.art.sangeun.travelan.utils.OnMapSelectListener;
import travelan.art.sangeun.travelan.models.Plan;

public class PlanAdapter extends RecyclerView.Adapter {
    private static final int ATTRIBUTE_ADD = 0;
    private static final int ATTRIBUTE_COMMON = 1;
    private static final int ATTRIBUTE_TRANSPORT = 2;

    public List<Plan> items;
    private PlanFragment planFragment;

    public PlanAdapter(PlanFragment planFragment, List<Plan> items) {
        this.planFragment = planFragment;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case ATTRIBUTE_ADD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plan_add, parent, false);
                return new ViewHolderAdd(view);
            case ATTRIBUTE_TRANSPORT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plan_transportation, parent, false);
                return new ViewHolderTransport(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_plan_common, parent, false);
                return new ViewHolderCommon(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Plan item = items.get(position);
        int viewType = getItemViewType(position);

        if (viewType == ATTRIBUTE_ADD) {
            ViewHolderAdd viewHolder = (ViewHolderAdd) holder;
            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.travelId < 0) {
                        selectTravel(item);
                    } else {
                        addPlan(item);
                    }
                }
            });
        } else if (viewType == ATTRIBUTE_TRANSPORT) {
            ViewHolderTransport viewHolder = (ViewHolderTransport) holder;
            viewHolder.origin.setText(item.origin);
            viewHolder.route.setText(item.route);
            viewHolder.destination.setText(item.destination);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MapLocationDialog mapDialog = new MapLocationDialog();
                    mapDialog.setPolyline(item.polyline);
                    mapDialog.show(planFragment.getFragmentManager(), "mapDialog");
                }
            });
        } else {
            ViewHolderCommon viewHolder = (ViewHolderCommon) holder;
            viewHolder.title.setText(item.title);
            viewHolder.address.setText(item.address);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.coordinates == null) {
                        Alert.show(planFragment.getContext(), "위치정보가 없습니다.");
                        return;
                    }

                    MapLocationDialog mapDialog = new MapLocationDialog();
                    mapDialog.setPoint(item.coordinates);
                    mapDialog.show(planFragment.getFragmentManager(), "mapDialog");
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Plan item = items.get(position);

        if (item.id < 0) {
            return ATTRIBUTE_ADD;
        }

        if (item.attributeType.equals("transportation")) {
            return ATTRIBUTE_TRANSPORT;
        }

        return ATTRIBUTE_COMMON;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void selectTravel(final Plan item) {
        final SelectTravelDialog selectTravelDialog = new SelectTravelDialog();
        selectTravelDialog.init(planFragment.getNearByTravel(), new SelectTravelAdapter.OnTravelSelectListener() {
            @Override
            public void onSelect(Map<String, String> travel) {
                selectTravelDialog.dismiss();
                item.travelId = Integer.parseInt(travel.get("id"));
                addPlan(item);
            }
        });

        selectTravelDialog.show(planFragment.getFragmentManager(), "selectTravelDialog");
    }

    public void addPlan(final Plan item) {
        planFragment.openAttributeSelect(new BottomSheetListener() {
            @Override
            public void onSelect(final int id) {
                final MapLocationDialog mapLocationDialog = new MapLocationDialog();

                if (id == R.id.transport) {
                    mapLocationDialog.setMode(MapLocationDialog.SEARCH_ORIGIN);
                    mapLocationDialog.setOnRouteSelect(new MapLocationDialog.OnRouteSelect() {
                        @Override
                        public void onSelect(String startAddress, LatLng startLocation, String endAddress, LatLng endLocation, String summary, List<LatLng> points) {
                            item.setAttributeTypeById(id);
                            item.origin = startAddress;
                            item.originCoordinates = startLocation;
                            item.destination = endAddress;
                            item.destinationCoordinates = endLocation;
                            item.route = summary;
                            item.polyline = points;

                            ApiClient.addPlan(item, planFragment.selectedDate, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    mapLocationDialog.dismiss();
                                    planFragment.getMonthTravel(planFragment.selectedDate);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    Toast.makeText(planFragment.getContext(), "FAIL TO WRITE PLAN", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    mapLocationDialog.setOnMapSelectedListener(new OnMapSelectListener() {
                        @Override
                        public void onSelect(MapLocation mapLocation) {
                            item.setAttributeTypeById(id);
                            item.title = mapLocation.poi;
                            item.address = mapLocation.address;
                            item.coordinates = mapLocation.latlng;

                            ApiClient.addPlan(item, planFragment.selectedDate, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    mapLocationDialog.dismiss();
                                    planFragment.getMonthTravel(planFragment.selectedDate);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    Toast.makeText(planFragment.getContext(), "FAIL TO WRITE PLAN", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }

                mapLocationDialog.show(planFragment.getFragmentManager(), "mapLocationDialog");
            }
        });
    }

    class ViewHolderAdd extends RecyclerView.ViewHolder {
        TextView add;

        public ViewHolderAdd(View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.add);
        }
    }

    class ViewHolderCommon extends RecyclerView.ViewHolder {
        TextView title, address;

        public ViewHolderCommon(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.planTitle);
            address = itemView.findViewById(R.id.planAddress);
        }
    }

    class ViewHolderTransport extends RecyclerView.ViewHolder {
        TextView time, way, origin, route, destination;

        public ViewHolderTransport(View itemView) {
            super(itemView);
            origin = itemView.findViewById(R.id.origin);
            route = itemView.findViewById(R.id.route);
            destination = itemView.findViewById(R.id.destination);
        }
    }
}
