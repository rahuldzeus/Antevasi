package bhaiya.sid.com.antevasi;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class TermsActivity extends AppCompatActivity {
    TextView term;
    String url="https://storyclick.000webhostapp.com/biography.php?req=terms";
    MultipleRippleLoader multipleRippleLoader;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);


        term=findViewById(R.id.term);
        coordinatorLayout=findViewById(R.id.coordinator_layout);
        multipleRippleLoader=findViewById(R.id.progress_ripple);
        if (new ConnectionDetector().isConnectingToInternet(TermsActivity.this)) {
            multipleRippleLoader.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    term.setText(response);
                    multipleRippleLoader.setVisibility(View.GONE);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String termMessage = "The names and the websites are added for the ease of the user only. The Application does not charges any amount from the user. The hacking or testing on WebBook is not allowed. The websites listed on WebBook is only for the access. The Developer has no intention to downgrade any website which is not present in the list.";
                    term.setText(termMessage);
                    multipleRippleLoader.setVisibility(View.GONE);

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }
        else
        {
            Snackbar.make(coordinatorLayout,"No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }
}
