package bhaiya.sid.com.antevasi;

import android.content.Intent;
import android.media.session.MediaSession;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.text.TextUtils.isEmpty;

public class TokenActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    EditText tokenValue;
    Button tokenValidationDone;
    MultipleRippleLoader multipleRippleLoader;
    final String key="TOKEN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        final ConnectionDetector connection=new ConnectionDetector();
        /*Finding Starts*/
        multipleRippleLoader=findViewById(R.id.progress_ripple);
        tokenValidationDone=findViewById(R.id.token_validation_done);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        tokenValue=findViewById(R.id.tokenValue);

            /*Finding Ends*/

            tokenValidationDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   //Button is clicked, now validate and request
                    /*Making Network request*/
                    if (!isEmpty(tokenValue.getText().toString())) {
                    if (connection.isConnectingToInternet(TokenActivity.this)) { //Checking Internet connection
                        multipleRippleLoader.setVisibility(View.VISIBLE);
                        final String batchToken=tokenValue.getText().toString().toLowerCase();
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://storyclick.000webhostapp.com/biography.php?req=token_check&token_data=" + batchToken, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String res = response.trim();     /*To remove the newline trim() is used*/
                                    /*Switch case to check the validity of token*/
                                    switch (res) {
                                        case "TRUE":
                                            multipleRippleLoader.setVisibility(View.GONE);
                                            Intent toQuestion=new Intent(getApplicationContext(),TokenValidToQuestion.class);
                                            toQuestion.putExtra("TOKEN",batchToken);
                                            startActivity(toQuestion);
                                            break;
                                        case "FALSE":
                                            multipleRippleLoader.setVisibility(View.GONE);
                                            Snackbar.make(coordinatorLayout, "Invalid Token", Snackbar.LENGTH_LONG).show();
                                            tokenValue.setBackgroundResource(R.drawable.error_edittext);
                                            break;
                                        default:
                                            multipleRippleLoader.setVisibility(View.GONE);
                                            Snackbar.make(coordinatorLayout, "Server Error", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    multipleRippleLoader.setVisibility(View.GONE);
                                    Snackbar.make(coordinatorLayout, "Try again later", Snackbar.LENGTH_LONG).show();
                                }
                            });

                            RequestQueue requestQueue = Volley.newRequestQueue(TokenActivity.this);
                            requestQueue.add(stringRequest);
                        } else {
                            Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_LONG).show();
                        }

                    }
                    else{
                        Snackbar.make(coordinatorLayout,"Enter batch token",Snackbar.LENGTH_LONG).show();
                    }
                }

            });


    }
}
