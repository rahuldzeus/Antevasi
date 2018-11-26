package bhaiya.sid.com.antevasi;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SeekBlessings extends Fragment {
    CoordinatorLayout coordinatorLayout;
    Button submitBlessingButton;
    ImageView checkImage;
    MultipleRippleLoader multipleRippleLoader;
    EditText name, message;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.seek_blessings,container,false);
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        name=view.findViewById(R.id.name);
        multipleRippleLoader=view.findViewById(R.id.progress_ripple);
        message=view.findViewById(R.id.message);
        submitBlessingButton=view.findViewById(R.id.submit_for_blessings);
        checkImage=view.findViewById(R.id.check_image);
        submitBlessingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkImage.getVisibility()==View.VISIBLE){
                    checkImage.setVisibility(View.GONE);
                }
                if (isEmpty(name.getText().toString())&& (isEmpty(message.getText().toString()))){  //if name and message is empty
                    Snackbar.make(coordinatorLayout,"Name is mandatory", Snackbar.LENGTH_LONG).show();
                }
                else{
                    if (new ConnectionDetector().isConnectingToInternet(getActivity().getApplicationContext())){
                        //if Connection is present
                        //Checking Internet connection
                        multipleRippleLoader.setVisibility(View.VISIBLE);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://storyclick.000webhostapp.com/biography.php?req=seek_blessings&name=" + name.getText().toString().toLowerCase() + "&message=" + message.getText().toString(), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String res = response.trim();    // To remove the newline trim() is used
                                // Switch case to check the validity of token
                                switch (res) {
                                    case "TRUE":
                                        multipleRippleLoader.setVisibility(View.GONE);
                                        checkImage.setVisibility(View.VISIBLE);
                                        submitBlessingButton.setEnabled(false);
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

                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        requestQueue.add(stringRequest);
                    }else{
                        Snackbar.make(coordinatorLayout,"No Internet Connection", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });


        return view;
    }
}
