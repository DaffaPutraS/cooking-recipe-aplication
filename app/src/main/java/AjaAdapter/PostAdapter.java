package AjaAdapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import AjaClassGetSet.Post;

import com.example.cookingrecipe.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;
    private OnItemClickListener listener;
    private List<Post> searchList;
    private List<Post> filteredList;
    private Drawable likeIconDefault;
    private Drawable likeIconFavorite;


    public void setSearchList(List<Post> searchList) {
        postList.clear();
        postList.addAll(searchList);
        notifyDataSetChanged();
    }

    public void setFilteredList(List<Post> filteredList) {
        postList.clear();
        postList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Post post);

        void onFavoriteClick(Post post);

        void onRefreshRequested();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView2, likeButton;
        public TextView nameTextView;
        public CardView recyclercardview;

        public PostViewHolder(View itemView) {
            super(itemView);

            imageView2 = itemView.findViewById(R.id.recyclercardimg);
            nameTextView = itemView.findViewById(R.id.recyclercardtitle);
            recyclercardview = itemView.findViewById(R.id.recyclerviewtemplate);
            likeButton = itemView.findViewById(R.id.likeButton);

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onFavoriteClick(postList.get(position));
                    }
                }
            });
        }
    }

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        this.filteredList = new ArrayList<>(postList);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final PostViewHolder viewHolder = new PostViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(postList.get(position));
                }
            }
        });

        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onFavoriteClick(postList.get(position));
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post currentPost = postList.get(position);
        if (currentPost != null && currentPost.getName() != null) {
            holder.nameTextView.setText(currentPost.getName());
        } else {
            holder.nameTextView.setText("N/A"); // Atur teks default jika nilai name null
        }
        if (currentPost != null && currentPost.getImageUrl() != null) {
            Picasso.get().load(currentPost.getImageUrl()).into(holder.imageView2);
        } else {
            Log.d(TAG, "onBindViewHolder: ??????");
        }
    }

    @Override
    public int getItemCount() {
        if (postList != null) {
            return postList.size();
        } else {
            return 0;
        }
    }

}

