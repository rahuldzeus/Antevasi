package bhaiya.sid.com.antevasi;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static android.text.TextUtils.isEmpty;

public class TokenValidToQuestion extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    EditText name,question;
    Button submitQuestionButton;
    ImageView checkImage;
    MultipleRippleLoader multipleRippleLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_valid_to_question);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        name=findViewById(R.id.name_of_student);
        multipleRippleLoader=findViewById(R.id.progress_ripple);
        question=findViewById(R.id.layout_question);
        submitQuestionButton=findViewById(R.id.submit_question);
        checkImage=findViewById(R.id.check_image);
         submitQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkImage.getVisibility()==View.VISIBLE){
                    checkImage.setVisibility(View.GONE);
                }
                if ((isEmpty(name.getText().toString()))||(isEmpty(question.getText().toString()))){  //  if any of the field is empty
                    Snackbar.make(coordinatorLayout,"Enter both fields", Snackbar.LENGTH_LONG).show();
                    }
                else{
                    if (new ConnectionDetector().isConnectingToInternet(TokenValidToQuestion.this)){
                        //if Connection is present
                        //Checking Internet connection
                        multipleRippleLoader.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://storyclick.000webhostapp.com/biography.php?req=raw_faq&name=" + name.getText().toString().toLowerCase()+"&batch="+getIntent().getStringExtra("TOKEN")+"&question="+question.getText().toString(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String res = response.trim();    // To remove the newline trim() is used
                               // Switch case to check the validity of token
                                switch (res) {
                                    case "TRUE":
                                        multipleRippleLoader.setVisibility(View.GONE);
                                        checkImage.setVisibility(View.VISIBLE);
                                        submitQuestionButton.setEnabled(false);
                                        break;
                                    case "FALSE":
                                        multipleRippleLoader.setVisibility(View.GONE);
                                        Snackbar.make(coordinatorLayout, "Try Later", Snackbar.LENGTH_LONG).show();
                                        checkImage.setImageResource(R.drawable.error);
                                        break;
                                    default:
                                        multipleRippleLoader.setVisibility(View.GONE);
                                        checkImage.setImageResource(R.drawable.error);
                                        Snackbar.make(coordinatorLayout, "Server Error", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                multipleRippleLoader.setVisibility(View.GONE);
                                Snackbar.make(coordinatorLayout, "Server Error", Snackbar.LENGTH_LONG).show();
                            }
                        });

                        RequestQueue requestQueue = Volley.newRequestQueue(TokenValidToQuestion.this);
                        requestQueue.add(stringRequest);
                    }else{
                        Snackbar.make(coordinatorLayout,"No Internet Connection", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
