package bhaiya.sid.com.antevasi;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private Context context;
    private ArrayList<HomeResponse> homeResponseArrayList;
    HomeAdapter(Context context, ArrayList<HomeResponse> homeResponseArrayList){
        this.context = context;
        this.homeResponseArrayList = homeResponseArrayList;
    }
    View view;
    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.single_home_item,parent,false);
        HomeAdapter.HomeViewHolder homeViewHolder = new HomeAdapter.HomeViewHolder(view);
        return homeViewHolder;
    }
    @Override
    public void onBindViewHolder(HomeAdapter.HomeViewHolder holder, int position) {
        final HomeResponse  homeData = homeResponseArrayList.get(position);
        /*Picasso to show the posters*/
        String imgUrl = "https://storyclick.000webhostapp.com/antevasi_upcoming_batches/"+homeData.getPicName();
        Picasso.get()
                .load(imgUrl)
                .into(holder.poster);

    }
    @Override
    public int getItemCount() {
        return homeResponseArrayList.size();
    }
    class HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView poster;
        HomeViewHolder(View itemView) {
            super(itemView);
            poster=itemView.findViewById(R.id.poster);

        }
    }
}
