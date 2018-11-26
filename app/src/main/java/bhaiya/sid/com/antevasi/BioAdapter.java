package bhaiya.sid.com.antevasi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BioAdapter extends RecyclerView.Adapter<BioAdapter.BioViewHolder> {
        Context context;
        View view;
        ArrayList<BioResponse> bioList;
public BioAdapter(Context context,ArrayList<BioResponse> bioList){
        this.context = context;
        this.bioList = bioList;
        }

@Override
public BioAdapter.BioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.single_item,parent,false);
        BioViewHolder bioViewHolder= new BioViewHolder(view);
        return bioViewHolder;
        }

    @Override
    public void onBindViewHolder(@NonNull BioAdapter.BioViewHolder holder, int position) {
        final BioResponse  bioResponse = bioList.get(position);
        holder.name.setText(bioResponse.getName());
        Picasso.get()
                .load(bioResponse.getImage())
                .into(holder.image);
        holder.bio.setText(bioResponse.getBiography());
    }


@Override
public int getItemCount() {
        return bioList.size();
        }

class BioViewHolder extends RecyclerView.ViewHolder{
    TextView name,bio;
    ImageView image;
    public BioViewHolder(View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.name);
        bio=itemView.findViewById(R.id.bio);
        image=itemView.findViewById(R.id.pic);

    }
}
}
