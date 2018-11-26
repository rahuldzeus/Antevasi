package bhaiya.sid.com.antevasi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.lang.reflect.Method;

import static android.text.TextUtils.isEmpty;

public class VolunteerRegistration extends AppCompatActivity {
    Button signUp;
    EditText usernameView,passwordView,confirmPasswordView;
    StringRequest stringRequest;
    String usernameValue,passwordValue,confirmPasswordValue;
    MultipleRippleLoader multipleRippleLoader;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_registration);
        multipleRippleLoader=findViewById(R.id.progress_ripple);
        coordinatorLayout=findViewById(R.id.coordinator_layout);
        usernameView=findViewById(R.id.username);
        passwordView=findViewById(R.id.password);
        confirmPasswordView=findViewById(R.id.confirm_password);
        signUp=findViewById(R.id.sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!isEmpty(usernameView.getText()))&&(!isEmpty(passwordView.getText()))&&(!isEmpty(confirmPasswordView.getText()))){ //if all the fields are filled then only execute, otherwise move to the else part
                    //fetching the values
                    usernameValue=usernameView.getText().toString();
                    passwordValue=passwordView.getText().toString();
                    confirmPasswordValue=confirmPasswordView.getText().toString();
                    if (passwordValue.compareTo(confirmPasswordValue)==0){  //Both password matched, now send the username and password to server
                        multipleRippleLoader.setVisibility(View.VISIBLE);
                        //Start Volley
                        stringRequest=new StringRequest(Request.Method.GET, "https://storyclick.000webhostapp.com/biography.php?req=volunteer_registration&username=" + usernameValue + "&password=" + passwordValue, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String res=response.trim();
                                switch (res){
                                    case "TRUE":
                                        multipleRippleLoader.setVisibility(View.GONE);
                                        SharedPreferences usernameSharedPreferences=getSharedPreferences("USERNAME",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=usernameSharedPreferences.edit();
                                        editor.putString("username",usernameValue);
                                        editor.commit();
                                        Intent toVolunteerHomeActivity=new Intent(VolunteerRegistration.this, VolunteerHomepage.class);
                                        startActivity(toVolunteerHomeActivity);
                                        break;
                                    case "FALSE":
                                        multipleRippleLoader.setVisibility(View.GONE);
                                        Snackbar.make(coordinatorLayout,"Try again later", Snackbar.LENGTH_LONG).show();
                                        break;
                                    default:
                                        multipleRippleLoader.setVisibility(View.GONE);
                                        Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
                                        break;
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                multipleRippleLoader.setVisibility(View.GONE);
                                Snackbar.make(coordinatorLayout,"Incorrect values", Snackbar.LENGTH_LONG).show();
                            }
                        });

                        RequestQueue requestQueue= Volley.newRequestQueue(VolunteerRegistration.this);
                        requestQueue.add(stringRequest);
                        //End Volley

                    }else{
                        Snackbar.make(coordinatorLayout,"Password did not match", Snackbar.LENGTH_LONG).show();
                    }


                }
                else{
                    Snackbar.make(coordinatorLayout,"Enter all the fields", Snackbar.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        Intent toHomePage=new Intent(VolunteerRegistration.this,HomeActivity.class);
        startActivity(toHomePage);
        super.onBackPressed();
    }
}
