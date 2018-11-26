package bhaiya.sid.com.antevasi;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import uk.me.hardill.volley.multipart.MultipartRequest;

import static android.app.Activity.RESULT_OK;
import static android.text.TextUtils.isEmpty;

public class VolunteerNavProfile extends Fragment {
    View view;
    CoordinatorLayout coordinatorLayout;
    JsonArrayRequest jsonArrayRequest;
    MultipleRippleLoader multipleRippleLoader;
    ImageView verifiedBadge, profilePic;
    TextView name, usernameView, bio, dateOfCourse, volunteerSince, occupation, department;
    TextView changePassword;
    Button logout, nameEdit, bioEdit, occupationEdit;
    String sharedPreferenceUsername;
    int flag=0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.volunteer_nav_profile, container, false);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        multipleRippleLoader = view.findViewById(R.id.progress_ripple);
        profilePic = view.findViewById(R.id.profile_pic);
        nameEdit = view.findViewById(R.id.name_edit);
        bioEdit = view.findViewById(R.id.bio_edit);
        occupationEdit = view.findViewById(R.id.occupation_edit);
        verifiedBadge = view.findViewById(R.id.verified_badge);
        name = view.findViewById(R.id.name);
        usernameView = view.findViewById(R.id.username);
        department = view.findViewById(R.id.department);
        bio = view.findViewById(R.id.bio);
        dateOfCourse = view.findViewById(R.id.date_of_course);
        occupation = view.findViewById(R.id.occupation);
        volunteerSince = view.findViewById(R.id.volunteer_since);
        changePassword = view.findViewById(R.id.change_password);
        logout = view.findViewById(R.id.logout);
        /*Move to previous activity when it is clicked*/
        SharedPreferences userLogInCheck = getActivity().getSharedPreferences("USERNAME", Context.MODE_PRIVATE);
        if (isEmpty(userLogInCheck.getString("username", null))) {    //if the user has pressed back button after logout
            Intent toHomeActivity = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
            startActivity(toHomeActivity);
        } else {

            /*Profile pic uploading*/
            profilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toUploadActivity = new Intent(getActivity(), UploadProfilePic.class);
                    startActivity(toUploadActivity);
                }
            });
            /*Changing password*/
            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateInfo("password");

                }
            });


            logout.setOnClickListener(new View.OnClickListener() {  //on clicking the 'logout' button
                @Override
                public void onClick(View v) {
                    SharedPreferences deletingUsername = getActivity().getSharedPreferences("USERNAME", Context.MODE_PRIVATE);
                    deletingUsername.edit().remove("username").commit();
                    Intent toHomePage = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
                    startActivity(toHomePage);
                }
            });
            if (new ConnectionDetector().isConnectingToInternet(getActivity().getApplicationContext())) {
                SharedPreferences usernameToSend = getActivity().getSharedPreferences("USERNAME", Context.MODE_PRIVATE);
                sharedPreferenceUsername = usernameToSend.getString("username", null);
                getOrUpdateData("profile_data", sharedPreferenceUsername); //get the data and show
            } else {
                Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_LONG).show();
            }

            /*Editing the information starts here*/
            nameEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateInfo("name");
                }
            });
            bioEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateInfo("bio");
                }
            });
            occupationEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateInfo("occupation");
                }
            });

            /*Editing the information ends here*/


        }
        return view;
    }
    private void getOrUpdateData(String key, final String username){    /*gets the data from server and shows in the fields*/
        multipleRippleLoader.setVisibility(View.VISIBLE);
        jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, "https://storyclick.000webhostapp.com/biography.php?req="+key+"&username=" + username, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject=response.getJSONObject(0);

                    /*Start setting the data into the 'EditText's and ImageView*/
                    if(jsonObject.getString("is_verified").equalsIgnoreCase("true")){
                        verifiedBadge.setVisibility(View.VISIBLE);
                    }
                    if (!isEmpty(jsonObject.getString("name"))){
                    name.setText(jsonObject.getString("name"));
                    }

                    usernameView.setText(username);
                    if (!isEmpty(jsonObject.getString("bio"))) {
                        bio.setText(jsonObject.getString("bio"));
                    }
                    if (!isEmpty(jsonObject.getString("department"))) {
                        department.setText(jsonObject.getString("department"));
                    }
                    if (!isEmpty(jsonObject.getString("course_date"))) {
                        dateOfCourse.setText(jsonObject.getString("course_date"));
                    }
                    if (!isEmpty(jsonObject.getString("volunteer_since"))) {
                        volunteerSince.setText(jsonObject.getString("volunteer_since"));
                    }
                    if (!isEmpty(jsonObject.getString("occupation"))) {
                        occupation.setText(jsonObject.getString("occupation"));
                    }
                    if (!isEmpty(jsonObject.getString("pic"))){
                        Picasso.get().load("https://storyclick.000webhostapp.com/"+jsonObject.getString("pic")).into(profilePic);
                        multipleRippleLoader.setVisibility(View.GONE);  //loader vanishes and the profile pic is set and visible
                        flag=1;
                    }
                    if (flag!=1){
                        multipleRippleLoader.setVisibility(View.GONE);  //loader vanishes and the profile pic is set and visible
                    }
                    else{
                        flag=0;
                    }


                } catch (JSONException e) {
                    multipleRippleLoader.setVisibility(View.GONE);
                    Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                multipleRippleLoader.setVisibility(View.GONE);
                Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }




    @Override
    public void onDestroy() {
        jsonArrayRequest.cancel();
        super.onDestroy();
    }
    protected void updateInfo(final String updateType){
        if (updateType.equalsIgnoreCase("name")){
            final EditText taskEditText = new EditText(getActivity().getApplicationContext());
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("New Name")
                    .setView(taskEditText)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String task = String.valueOf(taskEditText.getText());

                            updateGivenData(updateType,task);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        }if (updateType.equalsIgnoreCase("bio")){
            final EditText taskEditText = new EditText(getActivity().getApplicationContext());
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("New Biography")
                    .setView(taskEditText)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String task = String.valueOf(taskEditText.getText());
                            updateGivenData(updateType,task);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();

        }if(updateType.equalsIgnoreCase("occupation")){
            final EditText taskEditText = new EditText(getActivity().getApplicationContext());
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("New Occupation")
                    .setView(taskEditText)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String task = String.valueOf(taskEditText.getText());
                            updateGivenData(updateType,task);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        }

        if(updateType.equalsIgnoreCase("password")){    //change password
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
            final EditText passwordDialog = alertLayout.findViewById(R.id.password_dialog);
            final EditText confirmPasswordDialog = alertLayout.findViewById(R.id.confirm_password_dialog);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Change Password");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Snackbar.make(coordinatorLayout,"Password not changed", Snackbar.LENGTH_LONG).show();//Do Nothing
                }
            });

            alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String passwordValue = passwordDialog.getText().toString();
                    String confirmPasswordValue = confirmPasswordDialog.getText().toString();
                    if (passwordValue.compareTo(confirmPasswordValue)==0){
                        updateGivenData(updateType,passwordValue);
                    }
                    else{
                        Snackbar.make(coordinatorLayout,"Password did'nt matched", Snackbar.LENGTH_LONG).show();
                    }
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();

        }
    }
    protected void updateGivenData(final String updateType, String data){
        if (new ConnectionDetector().isConnectingToInternet(getActivity().getApplicationContext())){
            multipleRippleLoader.setVisibility(View.VISIBLE);

            jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, "https://storyclick.000webhostapp.com/biography.php?req=update_profile&username=" + sharedPreferenceUsername+"&update_type="+updateType+"&data="+data, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        JSONObject jsonObject=response.getJSONObject(0);
                        multipleRippleLoader.setVisibility(View.GONE);  //loader vanishes and the values are then set
                        /*Start setting the data into the 'EditText's*/
                        if (updateType.equals("password")) {
                            if (!isEmpty(jsonObject.getString("PASSWORD_CHANGED"))){
                                String message=jsonObject.getString("PASSWORD_CHANGED").trim();
                                switch (message){
                                    case "TRUE":
                                        Snackbar.make(coordinatorLayout,"Congratulation! Password changed", Snackbar.LENGTH_LONG).show();
                                        break;
                                    case "FALSE":
                                        Snackbar.make(coordinatorLayout,"Password not changed", Snackbar.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Snackbar.make(coordinatorLayout,"something went wrong", Snackbar.LENGTH_LONG).show();
                                        break;
                                }
                            }

                        }
                        else{
                            if (jsonObject.getString("is_verified").equalsIgnoreCase("true")) {
                                verifiedBadge.setVisibility(View.VISIBLE);
                            } else {
                                verifiedBadge.setVisibility(View.GONE);
                            }
                            if (!isEmpty(jsonObject.getString("name"))) {
                                name.setText(jsonObject.getString("name"));
                            }if (!isEmpty(jsonObject.getString("bio"))) {
                                bio.setText(jsonObject.getString("bio"));
                            }if (!isEmpty(jsonObject.getString("occupation"))) {
                                occupation.setText(jsonObject.getString("occupation"));
                            }

                        }



                    } catch (JSONException e) {
                        multipleRippleLoader.setVisibility(View.GONE);
                        Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    multipleRippleLoader.setVisibility(View.GONE);
                    Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
                }
            });
            RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(jsonArrayRequest);

        }
        else{
            Snackbar.make(coordinatorLayout,"No Internet connection", Snackbar.LENGTH_LONG).show();
        }


    }

}
