package bhaiya.sid.com.antevasi;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class VolunteerHomepage extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_attendance:
                    // execute the 'Attendance' fragment
                    VolunteerNavAttendance volunteerNavAttendance=new VolunteerNavAttendance();
                    ft.replace(R.id.volunteer_frame,volunteerNavAttendance);
                    ft.commit();
                    return true;
                case R.id.navigation_meeting:
                    // execute the 'Meeting' fragment
                    VolunteerNavMeeting volunteerNavMeeting=new VolunteerNavMeeting();
                    ft.replace(R.id.volunteer_frame,volunteerNavMeeting);
                    ft.commit();
                    return true;
                case R.id.navigation_profile:
                    // execute the 'Profile' fragment
                    VolunteerNavProfile volunteerNavProfile=new VolunteerNavProfile();
                    ft.replace(R.id.volunteer_frame,volunteerNavProfile);
                    ft.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_homepage);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        VolunteerNavAttendance volunteerNavAttendance=new VolunteerNavAttendance();
        ft.addToBackStack("volunteerNavAttendance");
        ft.replace(R.id.volunteer_frame,volunteerNavAttendance);
        ft.commit();


    }

}
