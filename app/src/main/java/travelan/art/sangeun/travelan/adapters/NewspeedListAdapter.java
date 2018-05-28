package travelan.art.sangeun.travelan.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.models.Newspeed;

public class NewspeedListAdapter extends RecyclerView.Adapter{
    private List<Newspeed> items;
    private Context context;

    public NewspeedListAdapter (Context context, List<Newspeed> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_newspeed, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mViewHolder = (ViewHolder)holder;
        final Newspeed item = items.get(position);
        Log.i("onBindViewHolder", item.toString());
        mViewHolder.location.setText(item.location);
        mViewHolder.userId.setText(item.user.userId);
        mViewHolder.contents.setText(item.contents);
        if (item.isFav) {
            mViewHolder.btnAddFavs.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            mViewHolder.btnAddFavs.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        mViewHolder.imageCarousel.setPageCount(item.images.size());
        mViewHolder.imageCarousel.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Log.i("image load", item.images.get(position));

                Picasso.get().load(item.images.get(position)).into(imageView);
            }
        });
        Picasso.get().load(item.user.thumbnail).into(mViewHolder.userThumb);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView location, userId, contents, commentButton;
        public ImageButton btnAddFavs;
        public CarouselView imageCarousel;
        public ImageView userThumb;
        public Button planButton;

        public ViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location);
            userId = itemView.findViewById(R.id.userId);
            contents = itemView.findViewById(R.id.contents);
            commentButton = itemView.findViewById(R.id.commentButton);

            btnAddFavs = itemView.findViewById(R.id.btnAddFavs);
            imageCarousel = itemView.findViewById(R.id.photo);
            userThumb = itemView.findViewById(R.id.userThumb);
            planButton = itemView.findViewById(R.id.planButton);
        }
    }
}