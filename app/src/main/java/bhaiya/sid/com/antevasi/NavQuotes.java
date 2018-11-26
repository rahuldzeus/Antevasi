package bhaiya.sid.com.antevasi;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class NavQuotes extends Fragment {
    View view;
    String QUOTE_URL = "https://storyclick.000webhostapp.com/biography.php?req=quote";
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton refresh;
    MultipleRippleLoader multipleRippleLoader;
    ConnectionDetector connectionDetector;
    String CONSUMER_KEY = "";
    String CONSUMER_SECRET = "";
    TextView quote;
    private LinearLayout mainLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.nav_quotes, container, false);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        mainLayout = view.findViewById(R.id.main_layout);
        quote=view.findViewById(R.id.quote);
        refresh = view.findViewById(R.id.refresh);
        multipleRippleLoader = view.findViewById(R.id.progress_ripple);
        serverRequest();    //load the quote for first time
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quote.setVisibility(View.GONE);
                serverRequest();
                quote.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    public void serverRequest() {
        connectionDetector = new ConnectionDetector();
        if (connectionDetector.isConnectingToInternet(getActivity().getApplicationContext())) {
            multipleRippleLoader.setVisibility(View.VISIBLE);
            //make a string request

            StringRequest stringRequest=new StringRequest(Request.Method.GET, QUOTE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    multipleRippleLoader.setVisibility(View.GONE);
                    if (!isEmpty(response)){
                        quote.setText(response.trim());
                    }
                    else{
                        String a="You do not need to learn a thing to become wise,you need to unlearn all those bad habits that keep you far from meaningful things!";
                        quote.setText(a);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    multipleRippleLoader.setVisibility(View.GONE);
                    Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue=Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(stringRequest);
        } else {
            Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    }


}
