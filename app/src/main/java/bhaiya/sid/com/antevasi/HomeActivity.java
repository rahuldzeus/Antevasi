package bhaiya.sid.com.antevasi;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.text.TextUtils.isEmpty;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*Push Notification implementation starts*/

     /*   FirebaseMessaging.getInstance().subscribeToTopic("text");
        FirebaseInstanceId.getInstance().getToken();
*/
        /*Push Notification implementation ends*/


        /*Opens the 'HomeFragment'*/
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        // execute the home fragment
        NavHome navHome=new NavHome();
        ft.replace(R.id.frame,navHome);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_terms) {
            Intent toTerms=new Intent(HomeActivity.this,TermsActivity.class);
            startActivity(toTerms);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            // execute the home fragment
            NavHome navHome=new NavHome();
            ft.replace(R.id.frame,navHome);
            ft.commit();

        }else if (id == R.id.nav_blessings)
        {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            // execute the quotation fragment
            SeekBlessings seekBlessings=new SeekBlessings();
            ft.replace(R.id.frame,seekBlessings);
            ft.commit();

        }
        else if (id == R.id.nav_quotes)
        {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            // execute the quotation fragment
            NavQuotes navQuotes=new NavQuotes();
            ft.replace(R.id.frame,navQuotes);
            ft.commit();

        } else if (id == R.id.nav_schedule) {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            // execute the schedule fragment
            NavSchedule navSchedule=new NavSchedule();
            ft.replace(R.id.frame,navSchedule);
            ft.commit();

        }else if (id == R.id.faq) {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            // execute the faq fragment
            NavFaq navFaq=new NavFaq();
            ft.replace(R.id.frame,navFaq);
            ft.commit();


        }else if (id == R.id.nav_biography) {
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            // execute the biography fragment
            NavBio navBio=new NavBio();
            ft.replace(R.id.frame,navBio);
            ft.commit();

        }
        else if (id == R.id.nav_volunteer) {
            /*if the user is logged in then VOLUNTEER HOMEPAGE is executed otherwise the VOLUNTEER LOGIN PAGE is executed*/
            SharedPreferences usernameCheck=getSharedPreferences("USERNAME",MODE_PRIVATE);
            if (isEmpty(usernameCheck.getString("username",null))){
                FragmentTransaction ft=getFragmentManager().beginTransaction();
                // execute the volunteer fragment
                NavVolunteer navVolunteer=new NavVolunteer();
                ft.replace(R.id.frame,navVolunteer);
                ft.commit();
            }else{
                Intent toHomeVolunteerPage=new Intent(HomeActivity.this,VolunteerHomepage.class);
                startActivity(toHomeVolunteerPage);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
