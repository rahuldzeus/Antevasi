package bhaiya.sid.com.antevasi;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FilterQueryProvider;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NavFaq extends Fragment {
    View view;
    public RecyclerView recyclerView;
    String FAQ_URL="https://storyclick.000webhostapp.com/biography.php?req=faq";
    ArrayList<FaqResponse> faqAdaptersList = new ArrayList<FaqResponse>();
    public FaqAdapter adapter;
    CoordinatorLayout coordinatorLayout;
    MultipleRippleLoader multipleRippleLoader;
    JsonArrayRequest jsonArrayRequest;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.nav_faq,container,false);
        final Button postButton;
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        postButton=view.findViewById(R.id.post_button);
        recyclerView = view.findViewById(R.id.mRecyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        if (new ConnectionDetector().isConnectingToInternet(getActivity().getApplicationContext())) {
            postButton.setEnabled(false);
            multipleRippleLoader=view.findViewById(R.id.progress_ripple);
            multipleRippleLoader.setVisibility(View.VISIBLE);
            jsonArrayRequest = new JsonArrayRequest(FAQ_URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                faqAdaptersList.clear();
                                Gson gson = new Gson();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    FaqResponse faqResponse = gson.fromJson(String.valueOf(jsonObject), FaqResponse.class);
                                    faqAdaptersList.add(faqResponse);
                                }
                                adapter = new FaqAdapter(getActivity().getApplicationContext(), faqAdaptersList);
                                recyclerView.setAdapter(adapter);
                                multipleRippleLoader.setVisibility(View.GONE);
                                postButton.setEnabled(true);

                            } catch (JSONException e) {
                                multipleRippleLoader.setVisibility(View.GONE);
                                Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
                                postButton.setEnabled(true);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            multipleRippleLoader.setVisibility(View.GONE);
                            Snackbar.make(coordinatorLayout,"Try again later", Snackbar.LENGTH_LONG).show();
                            postButton.setEnabled(true);
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(jsonArrayRequest);
        }
        else{
            Snackbar.make(coordinatorLayout,"No Internet Connection", Snackbar.LENGTH_LONG).show();
            postButton.setEnabled(true);
        }

        /*POST BUTTON*/
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toTokenCheckActivity=new Intent(getActivity().getApplicationContext(),TokenActivity.class);
                startActivity(toTokenCheckActivity);
                //Call the screen which will validate and will take the name and question as input
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        jsonArrayRequest.cancel();
        super.onDestroy();

    }
}
