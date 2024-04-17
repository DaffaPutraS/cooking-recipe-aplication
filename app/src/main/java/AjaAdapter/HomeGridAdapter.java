package AjaAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import AjaClassGetSet.Home;

import com.example.cookingrecipe.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeGridAdapter extends BaseAdapter {
    private Context context;
    private List<Home> homeList;

    public HomeGridAdapter(Context context, List<Home> homeList) {
        this.context = context;
        this.homeList = homeList;
    }

    @Override
    public int getCount() {
        return homeList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_post, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.Gridviewimg = convertView.findViewById(R.id.Gridviewimg);
            viewHolder.Gridviewtitle = convertView.findViewById(R.id.Gridviewtitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Home home = homeList.get(position);

        // Set data to views
        viewHolder.Gridviewtitle.setText(home.getName());
        Picasso.get().load(home.getImageUrl()).into(viewHolder.Gridviewimg);

        return convertView;
    }

    private static class ViewHolder {
        ImageView Gridviewimg;
        TextView Gridviewtitle;
    }
}


