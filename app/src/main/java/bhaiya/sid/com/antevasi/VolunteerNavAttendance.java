package bhaiya.sid.com.antevasi;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class VolunteerNavAttendance extends Fragment {
    View view;
    String username;
    JsonArrayRequest jsonArrayRequest;
    CoordinatorLayout coordinatorLayout;
    MultipleRippleLoader multipleRippleLoader;
    CircularProgressIndicator progressChart;
    int totalDays=0,attendanceCount=0;
    String lastAttendance=" ";
    TextView lastAttendanceDateView,usernameView,totalDaysView,attendanceCountView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.volunteer_nav_attendance,container,false);
        coordinatorLayout=view.findViewById(R.id.coordinator_layout);
        lastAttendanceDateView=view.findViewById(R.id.last_attendance_date);
        usernameView=view.findViewById(R.id.username);
        totalDaysView=view.findViewById(R.id.total_days);
        attendanceCountView=view.findViewById(R.id.attendance_count);
        progressChart=view.findViewById(R.id.progress_chart);
        multipleRippleLoader=view.findViewById(R.id.progress_ripple);
        SharedPreferences usernameSharedPreferences=getActivity().getSharedPreferences("USERNAME", Context.MODE_PRIVATE);
        username=usernameSharedPreferences.getString("username",null);  //This username will be sent to the server and the attendance and total days will be fetched
        if (new ConnectionDetector().isConnectingToInternet(getActivity().getApplicationContext())) {
            multipleRippleLoader.setVisibility(View.VISIBLE);
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://storyclick.000webhostapp.com/biography.php?req=attendance&username="+username, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            if (response.length()>0) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                if (jsonObject.getInt("total_days") != 0){
                                    totalDays = jsonObject.getInt("total_days");
                                }
                                if(jsonObject.getInt("attendance_count")!=0){
                                    attendanceCount = jsonObject.getInt("attendance_count");
                                }
                                if (jsonObject.getString("last_attendance")!=null) {
                                    lastAttendance = "Last Attendance -" + jsonObject.getString("last_attendance");
                                }
                                break;
                            }
                            else{
                                notAVolunteer();
                            }
                        } catch (JSONException e) {
                            Snackbar.make(coordinatorLayout,"something went wrong",Snackbar.LENGTH_LONG).show();
                            multipleRippleLoader.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                    multipleRippleLoader.setVisibility(View.GONE);
                    progressChart.setMaxProgress(totalDays);
                    progressChart.setCurrentProgress(attendanceCount);
                    lastAttendanceDateView.setText(lastAttendance);
                    /*The data is concatenated with the title*/
                    String usernameWithMessage="Username - "+username;
                    String totalDaysWithMessage="Total Days - "+totalDays;
                    String attendanceWithMessage="Attendance Count - "+attendanceCount;

                    usernameView.setText(usernameWithMessage);
                    totalDaysView.setText(totalDaysWithMessage);
                    attendanceCountView.setText(attendanceWithMessage);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    multipleRippleLoader.setVisibility(View.GONE);
                    Snackbar.make(coordinatorLayout,"something went wrong",Snackbar.LENGTH_LONG).show();

                }
            });
            RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(jsonArrayRequest);

        }
        else{
            Snackbar.make(coordinatorLayout,"No Internet connection", Snackbar.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        jsonArrayRequest.cancel();
        super.onDestroy();
    }
    private void notAVolunteer(){
        SharedPreferences preferences = getActivity().getSharedPreferences("USERNAME", Context.MODE_PRIVATE);
        preferences.edit().remove("username").commit();
        progressChart.setVisibility(View.GONE);
        lastAttendanceDateView.setVisibility(View.GONE);
        usernameView.setVisibility(View.GONE);
        totalDaysView.setVisibility(View.GONE);
        attendanceCountView.setText(R.string.not_a_volunteer);
        attendanceCountView.setGravity(Gravity.CENTER);
    }

    @Override
    public void onDetach() {
        getActivity().finish();
        super.onDetach();
    }
}
