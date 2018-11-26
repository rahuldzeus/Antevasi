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
import android.widget.ImageView;
import android.widget.TextView;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.southernbox.parallaxrecyclerview.ParallaxRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NavHome extends Fragment {
    View view;
    CoordinatorLayout coordinatorLayout;
    MultipleRippleLoader multipleRippleLoader;
    RecyclerView recyclerView;
    HomeAdapter adapter;
    TextView about,headOfDept,names;
    String hardcodedAbout,hardCodedNames,hardCodedHead;
    String homeURL="https://storyclick.000webhostapp.com/biography.php?req=home";
    ArrayList<HomeResponse> homeAdapterList = new ArrayList<HomeResponse>();
    JsonArrayRequest jsonArrayRequest;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.nav_home,container,false);
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        headOfDept=view.findViewById(R.id.head_of_dep);
        names=view.findViewById(R.id.names);
        about=view.findViewById(R.id.about);
        /*Hardcoded about*/
        hardCodedHead="\nHEAD OF THE DEPARTMENTS\n";
        hardcodedAbout="The application is handled and maintained by Art of Living, Meerut. The Information of departments are given as follows :";
        hardCodedNames="Sri Sri Anukampa : Pankhuri Agarwal\n" +
                "Mentor of Mentors : Siddharth Kar\n" +
                "Aol Programs : Ruby Sharma\n" +
                "Promotions : Isha Patel and Manushree Verma\n" +
                "Aol Venue : Aarti Jolly\n" +
                "Events : Anshul Karhana\n" +
                "Reception : Neha Tiwari\n" +
                "Poojas and Satsangs : Yesheswini Kaushik\n" +
                "Security : Mohit Setiya\n" +
                "IT department : Rahul Sharma";
        about.setText(hardcodedAbout);
        headOfDept.setText(hardCodedHead);
        names.setText(hardCodedNames);
        recyclerView = view.findViewById(R.id.mRecyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        if (new ConnectionDetector().isConnectingToInternet(getActivity().getApplicationContext())) {
            multipleRippleLoader=view.findViewById(R.id.progress_ripple);
            multipleRippleLoader.setVisibility(View.VISIBLE);
            jsonArrayRequest= new JsonArrayRequest(homeURL,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                homeAdapterList.clear();
                                Gson gson = new Gson();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    HomeResponse homeResponse = gson.fromJson(String.valueOf(jsonObject), HomeResponse.class);
                                    homeAdapterList.add(homeResponse);
                                }
                                adapter = new HomeAdapter(getActivity().getApplicationContext(), homeAdapterList);
                                recyclerView.setAdapter(adapter);
                                multipleRippleLoader.setVisibility(View.GONE);

                            } catch (JSONException e) {
                                multipleRippleLoader.setVisibility(View.GONE);
                                Snackbar.make(coordinatorLayout,"Something went wrong", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            multipleRippleLoader.setVisibility(View.GONE);
                            Snackbar.make(coordinatorLayout,"Try again later", Snackbar.LENGTH_LONG).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
            requestQueue.add(jsonArrayRequest);
        }
        else{
            Snackbar.make(coordinatorLayout,"No Internet Connection!!", Snackbar.LENGTH_LONG).show();
        }
        return view;

    }
    @Override
    public void onDestroy() {
        jsonArrayRequest.cancel();
        super.onDestroy();

    }

}
