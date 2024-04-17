package AjaClassGetSet;

public class Favorite {
    private String postId;
    private boolean liked;

    public Favorite() {
        // Diperlukan konstruktor kosong untuk Firebase
    }

    public Favorite(String postId, boolean liked) {
        this.postId = postId;
        this.liked = liked;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}


