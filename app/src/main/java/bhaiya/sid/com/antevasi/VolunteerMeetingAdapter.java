package bhaiya.sid.com.antevasi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class VolunteerMeetingAdapter extends RecyclerView.Adapter<VolunteerMeetingAdapter.VolunteerMeetingViewHolder> {
    private Context context;
    private ArrayList<VolunteerMeetingResponse> meetingResponseArrayList;

    public VolunteerMeetingAdapter(Context context, ArrayList<VolunteerMeetingResponse> meetingResponseArrayList) {
        this.context = context;
        this.meetingResponseArrayList = meetingResponseArrayList;
    }

    View view;

    @Override
    public VolunteerMeetingAdapter.VolunteerMeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.volunteer_meeting_single_item, parent, false);
        VolunteerMeetingViewHolder volunteerMeetingViewHolder = new VolunteerMeetingViewHolder(view);
        return volunteerMeetingViewHolder;
    }

    @Override
    public void onBindViewHolder(VolunteerMeetingAdapter.VolunteerMeetingViewHolder holder, int position) {
        final VolunteerMeetingResponse meetingData = meetingResponseArrayList.get(position);
        String time="Time: "+meetingData.getTime();
        String meetingWith="With: "+meetingData.getMeeting_with();
        String meetingFor="For: "+meetingData.getMeeting_for();
        if (!isEmpty(meetingData.getDate())){
            holder.date.setText(meetingData.getDate());
        }
       if (!isEmpty(meetingData.getTime())) {
           holder.time.setText(time);
       }
       if (!isEmpty(meetingData.getMeeting_with())) {
            holder.meeting_with.setText(meetingWith);
        }
        if (!isEmpty(meetingData.getMeeting_for())) {
            holder.meeting_for.setText(meetingFor);
        }
    }
    @Override
    public int getItemCount() {
        return meetingResponseArrayList.size();
    }

    class VolunteerMeetingViewHolder extends RecyclerView.ViewHolder {
        TextView time, date, meeting_with, meeting_for;     //here 'meeting_with' is the faculty and 'meeting_for' is the individuals

        VolunteerMeetingViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.meeting_date);
            time = itemView.findViewById(R.id.meeting_time);
            meeting_with = itemView.findViewById(R.id.meeting_with);
            meeting_for = itemView.findViewById(R.id.meeting_for);
        }
    }
}