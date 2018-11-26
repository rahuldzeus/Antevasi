package bhaiya.sid.com.antevasi;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class NavBio extends Fragment {
    View view;
    String BIO_URL="https://storyclick.000webhostapp.com/biography.php?req=bio";
    CoordinatorLayout coordinatorLayout;
    public RecyclerView recyclerView;
    public BioAdapter adapter;
    MultipleRippleLoader multipleRippleLoader;
    ArrayList<BioResponse> bioList = new ArrayList<BioResponse>();
    JsonArrayRequest jsonArrayRequest;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.nav_bio,container,false);
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        multipleRippleLoader=view.findViewById(R.id.progress_ripple);
        if (new ConnectionDetector().isConnectingToInternet(getActivity().getApplicationContext())){
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
            multipleRippleLoader.setVisibility(View.VISIBLE);
            jsonArrayRequest= new JsonArrayRequest(BIO_URL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                bioList.clear();
                                Gson gson = new Gson();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    BioResponse bioResponse = gson.fromJson(String.valueOf(jsonObject), BioResponse.class);
                                    bioList.add(bioResponse);
                                }
                                adapter = new BioAdapter(getActivity().getApplicationContext(), bioList);
                                recyclerView.setAdapter(adapter);
                                multipleRippleLoader.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                multipleRippleLoader.setVisibility(View.GONE);
                                Snackbar.make(coordinatorLayout, "something went wrong", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    multipleRippleLoader.setVisibility(View.GONE);
                    Snackbar.make(coordinatorLayout,"something went wrong",Snackbar.LENGTH_LONG).show();

            }
        });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(jsonArrayRequest);

        }
        else{
            /*Show SnackBar*/
            Snackbar.make(coordinatorLayout,"No Internet Connection",Snackbar.LENGTH_LONG).show();
        }
        return view;
    }
    @Override
    public void onDestroy() {
        jsonArrayRequest.cancel();
        super.onDestroy();

    }
}
