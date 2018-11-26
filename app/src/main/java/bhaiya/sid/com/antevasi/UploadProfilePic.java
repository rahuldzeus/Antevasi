package bhaiya.sid.com.antevasi;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agrawalsuneet.loaderspack.loaders.MultipleRippleLoader;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadProfilePic extends AppCompatActivity {

    String sharedPreferenceUsername;
    CoordinatorLayout coordinatorLayout;
    MultipleRippleLoader multipleRippleLoader;
    StringRequest stringRequest;
    public static String URL;
    Button btnChoose,btnUpload;
    ImageView imageUpload,backButton;
    Bitmap bitmap;
    final int CODE_GALLERY_REQUEST=999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);
        coordinatorLayout=findViewById(R.id.coordinator_layout);
        backButton=findViewById(R.id.back_button);
        multipleRippleLoader=findViewById(R.id.progress_ripple);
        SharedPreferences sh = getSharedPreferences("USERNAME", MODE_PRIVATE);
        sharedPreferenceUsername = sh.getString("username", null);
        URL = "https://storyclick.000webhostapp.com/profile_upload.php";

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPreviousActivity=new Intent(UploadProfilePic.this,VolunteerHomepage.class);
                startActivity(toPreviousActivity);
            }
        });
        btnChoose=findViewById(R.id.btnChoose);
        btnUpload=findViewById(R.id.btnUpload);
        imageUpload=findViewById(R.id.imageUpload);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(UploadProfilePic.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
                btnUpload.setVisibility(View.VISIBLE);

            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleRippleLoader.setVisibility(View.VISIBLE);
                //post image to server
                stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String res=response.trim();
                        switch (res){
                            case "SUCCESS":/*on success show the message and disable 'upload' button*/
                                Snackbar.make(coordinatorLayout,"Congratulations! Profile Picture Changed", Snackbar.LENGTH_LONG).show();
                                btnUpload.setVisibility(View.GONE);
                                multipleRippleLoader.setVisibility(View.GONE);
                                break;
                            case "ERROR":/*on error show the error message and disable 'upload' button*/
                                Snackbar.make(coordinatorLayout,"Upload Failed", Snackbar.LENGTH_LONG).show();
                                btnUpload.setVisibility(View.GONE);
                                multipleRippleLoader.setVisibility(View.GONE);
                                break;
                                default:
                                    Snackbar.make(coordinatorLayout,"Internal Error", Snackbar.LENGTH_LONG).show();
                                    btnUpload.setVisibility(View.GONE);
                                    multipleRippleLoader.setVisibility(View.GONE);
                                    break;

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        multipleRippleLoader.setVisibility(View.GONE);
                        btnUpload.setVisibility(View.GONE);
                        Snackbar.make(coordinatorLayout,"Upload Failed", Snackbar.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params=new HashMap<>();
                        String imageData=imageToString(bitmap);
                        params.put("image",imageData);
                        params.put("username",sharedPreferenceUsername);
                        return params;
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(UploadProfilePic.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         if (requestCode==CODE_GALLERY_REQUEST) {
             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 Intent intent = new Intent(Intent.ACTION_PICK);
                 intent.setType("image/*");
                 startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
             } else {
                 Toast.makeText(getApplicationContext(), "You don't have permission", Toast.LENGTH_LONG).show();
             }
             return;
         }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==CODE_GALLERY_REQUEST && resultCode==RESULT_OK && data != null){
            Uri filePath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filePath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                imageUpload.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes=outputStream.toByteArray();
        String encodedImage= Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onDestroy() {
        stringRequest.cancel();
        super.onDestroy();
    }
}
