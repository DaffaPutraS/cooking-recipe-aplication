package AjaClassGetSet;

import android.os.Parcel;
import android.os.Parcelable;
import AjaAdapter.PostAdapter;

import com.google.firebase.database.PropertyName;

public class Post implements Parcelable {
    private String admin;
    private String idCreator;
    private String idPost;
    private String idSubcategory;
    private String image;
    private String ingredients;
    private String name;
    private String steps;

    public Post() {
        // Diperlukan konstruktor kosong untuk Firebase Realtime Database
    }

    protected Post(Parcel in) {
        admin = in.readString();
        idCreator = in.readString();
        idPost = in.readString();
        idSubcategory = in.readString();
        image = in.readString();
        ingredients = in.readString();
        name = in.readString();
        steps = in.readString();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @PropertyName("id_creator")
    public String getIdCreator() {
        return idCreator;
    }

    @PropertyName("id_creator")
    public void setIdCreator(String idCreator) {
        this.idCreator = idCreator;
    }

    @PropertyName("id_post")
    public String getIdPost() {
        return idPost;
    }

    @PropertyName("id_post")
    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    @PropertyName("id_subcategory")
    public String getIdSubcategory() {
        return idSubcategory;
    }

    @PropertyName("id_subcategory")
    public void setIdSubcategory(String idSubcategory) {
        this.idSubcategory = idSubcategory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getImageUrl() {
        return image;
    }

    // Parcelable implementation

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(admin);
        dest.writeString(idCreator);
        dest.writeString(idPost);
        dest.writeString(idSubcategory);
        dest.writeString(image);
        dest.writeString(ingredients);
        dest.writeString(name);
        dest.writeString(steps);
    }
}


