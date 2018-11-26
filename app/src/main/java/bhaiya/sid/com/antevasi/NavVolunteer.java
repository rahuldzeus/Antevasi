package bhaiya.sid.com.antevasi;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static android.content.Context.MODE_PRIVATE;
import static android.text.TextUtils.isEmpty;

public class NavVolunteer extends Fragment {
    View view;
    String usernameString,passwordString;
    EditText username,password;
    Button signInButton,signUpButton;
    CoordinatorLayout coordinatorLayout;
    StringRequest stringRequest;
    MultipleRippleLoader multipleRippleLoader;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.nav_volunteer, container, false);
        coordinatorLayout=view.findViewById(R.id.coordinator_layout);
        username=view.findViewById(R.id.username);
        password=view.findViewById(R.id.password);
        signUpButton=view.findViewById(R.id.sign_up);
        signInButton=view.findViewById(R.id.sign_in);

        /*Sign In button starts here*/
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!isEmpty(username.getText().toString())&& (!isEmpty(password.getText().toString())))){
                usernameString=username.getText().toString();
                passwordString=password.getText().toString();
                    multipleRippleLoader=view.findViewById(R.id.progress_ripple);
                    multipleRippleLoader.setVisibility(View.VISIBLE);
                    stringRequest=new StringRequest(Request.Method.GET, "https://storyclick.000webhostapp.com/biography.php?req=volunteer&username="+usernameString+"&password="+passwordString, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String res=response.trim();
                            switch (res){
                                case "TRUE":
                                    multipleRippleLoader.setVisibility(View.GONE);
                                    SharedPreferences usernameSharedPreferences=getActivity().getSharedPreferences("USERNAME",MODE_PRIVATE);//'USERNAME' is the name of SharedPreferences        //Shared Preferences contain USERNAME to track the user
                                    SharedPreferences.Editor editor=usernameSharedPreferences.edit();
                                    editor.putString("username",usernameString); //'username' is the name of the value
                                    editor.commit();
                                    Intent toVolunteerHomePage=new Intent(getActivity().getApplicationContext(),VolunteerHomepage.class);
                                    /*Put the username in sharedpreferences*/
                                    startActivity(toVolunteerHomePage);
                                    break;
                                case "FALSE":
                                    multipleRippleLoader.setVisibility(View.GONE);
                                    Snackbar.make(coordinatorLayout,"Incorrect username or password", Snackbar.LENGTH_LONG).show();
                                    break;
                                default:
                                    multipleRippleLoader.setVisibility(View.GONE);
                                    Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }, new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            multipleRippleLoader.setVisibility(View.GONE);
                            Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
                        }
                    });
                    RequestQueue requestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
                    requestQueue.add(stringRequest);


                }
                else{
                    Snackbar.make(coordinatorLayout,"Fill Both Fields", Snackbar.LENGTH_LONG).show();
                }


            }
        });
        /*Sign Up button starts here*/
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Implementation of registration logic*/
                Intent toRegistrationActivity=new Intent(getActivity().getApplicationContext(),VolunteerRegistration.class);
                startActivity(toRegistrationActivity);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        Intent toHomePage=new Intent(getActivity().getApplicationContext(),HomeActivity.class);
        startActivity(toHomePage);
        super.onDestroy();
    }
}
