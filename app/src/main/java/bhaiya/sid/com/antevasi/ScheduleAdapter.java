package bhaiya.sid.com.antevasi;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private Context context;
    private ArrayList<ScheduleResponse> scheduleResponsesList;
    public ScheduleAdapter(Context context,ArrayList<ScheduleResponse> scheduleResponsesList){
        this.context = context;
        this.scheduleResponsesList = scheduleResponsesList;
    }
    View view;
    @Override
    public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_row,parent,false);
        ScheduleViewHolder scheduleViewHolder = new ScheduleViewHolder(view);
        return scheduleViewHolder;
    }
    @Override
    public void onBindViewHolder(ScheduleAdapter.ScheduleViewHolder holder, int position) {
        final ScheduleResponse  scheduleData = scheduleResponsesList.get(position);
        holder.date.setText(scheduleData.getDate());
        holder.place.setText(scheduleData.getPlace());
        holder.reason.setText(scheduleData.getReason());
    }
    @Override
    public int getItemCount() {
        return scheduleResponsesList.size();
    }
    class ScheduleViewHolder extends RecyclerView.ViewHolder{
        TextView place,date,reason;
        public ScheduleViewHolder(View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            place=itemView.findViewById(R.id.place);
            reason=itemView.findViewById(R.id.reason);


        }
    }
}
