package AjaAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import AjaClassGetSet.Post;

import com.example.cookingrecipe.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<Post> favoriteList;
    private Context context;
    private boolean isFavorite;
    private OnLikeClickListener likeClickListener;

    public interface OnLikeClickListener {
        void onLikeClick(Post post, boolean isFavorite);
    }

    public void setOnLikeClickListener(OnLikeClickListener listener) {
        likeClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView2;
        private TextView nameTextView;
        private ImageView likeButton;
        public CardView recyclercardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView2 = itemView.findViewById(R.id.recyclercardimg);
            nameTextView = itemView.findViewById(R.id.recyclercardtitle);
            likeButton = itemView.findViewById(R.id.likeButton);
            recyclercardview = itemView.findViewById(R.id.recyclerviewtemplate);
        }
    }

    public FavoriteAdapter(List<Post> favoriteList, Context context, boolean isFavorite) {
        this.favoriteList = favoriteList;
        this.context = context;
        this.isFavorite = isFavorite;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = favoriteList.get(position);
        holder.nameTextView.setText(post.getName());
        // Menggunakan Picasso untuk memuat gambar dari URL ke ImageView
        Picasso.get().load(post.getImageUrl()).into(holder.imageView2);

        // Tampilkan tombol like atau unlike berdasarkan status favorit
        if (isFavorite) {
            holder.likeButton.setImageResource(R.drawable.ic_bookmark_nav);
        } else {
            holder.likeButton.setImageResource(R.drawable.ic_bookmark);
        }

        // Atur listener tombol like sesuai kondisi favorit
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (likeClickListener != null) {
                    likeClickListener.onLikeClick(post, isFavorite);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (favoriteList != null) {
            return favoriteList.size();
        } else {
            return 0;
        }    }
}

