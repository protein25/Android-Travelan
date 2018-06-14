package travelan.art.sangeun.travelan.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import travelan.art.sangeun.travelan.R;
import travelan.art.sangeun.travelan.models.Comment;
import travelan.art.sangeun.travelan.models.Newspeed;
import travelan.art.sangeun.travelan.utils.ApiClient;

public class CommentAdapter extends RecyclerView.Adapter{
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_COMMENT = 1;

    private Newspeed newspeed;
    public List<Comment> items;

    public CommentAdapter(Newspeed newspeed, List<Comment> items) {
        this.newspeed = newspeed;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_HEADER;

        return VIEW_TYPE_COMMENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_comment,  parent, false);
            return new NewspeedViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_member_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            final NewspeedViewHolder newspeedViewHolder = (NewspeedViewHolder) holder;
            newspeedViewHolder.location.setText(newspeed.location);
            newspeedViewHolder.imageSlide.setPageCount(newspeed.images.size());
            newspeedViewHolder.imageSlide.setImageListener(new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    Picasso.get().load(newspeed.images.get(position)).into(imageView);
                }
            });

            if (newspeed.isFav) {
                newspeedViewHolder.favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                newspeedViewHolder.favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }

            newspeedViewHolder.favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ApiClient.toggleFav(!newspeed.isFav, newspeed.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });

                    if (newspeed.isFav) {
                        newspeedViewHolder.favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    } else {
                        newspeedViewHolder.favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }

                    newspeed.isFav = !newspeed.isFav;
                }
            });
            return;
        }
        Comment item = items.get(position);

        CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
        Picasso.get().load(item.user.thumbnail).into(commentViewHolder.thumbnail);
        commentViewHolder.userId.setText(item.user.userId);
        commentViewHolder.content.setText(item.content);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class CommentViewHolder extends RecyclerView.ViewHolder {
    public ImageView thumbnail;
    public TextView userId, content;

    public CommentViewHolder(View itemView) {
        super(itemView);

        thumbnail = itemView.findViewById(R.id.userThumb);
        userId = itemView.findViewById(R.id.userId);
        content = itemView.findViewById(R.id.contents);
    }
}

class NewspeedViewHolder extends RecyclerView.ViewHolder {
    public TextView location;
    public ImageView favBtn;
    public CarouselView imageSlide;

    public NewspeedViewHolder(View itemView) {
        super(itemView);
        location = itemView.findViewById(R.id.location);
        favBtn = itemView.findViewById(R.id.btnAddFavs);
        imageSlide = itemView.findViewById(R.id.photo);
    }
}
