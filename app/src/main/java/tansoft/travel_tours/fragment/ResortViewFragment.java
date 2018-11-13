package tansoft.travel_tours.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tansoft.travel_tours.R;
import tansoft.travel_tours.adapter.ResortAdapter;
import tansoft.travel_tours.config.AppConfig;
import tansoft.travel_tours.config.AppController;
import tansoft.travel_tours.domain.Resort;
import tansoft.travel_tours.services.SQLiteHandler;

import static com.android.volley.VolleyLog.TAG;
import static java.util.Collections.sort;


public class ResortViewFragment extends Fragment {

    private  String resortName, contactDetails,serviceType, city,imageString;
    private Double longitude, latitude;
    ImageView productImage;
    TextView  companyName, phonenumber, emailaddress, physicaladdress,  payment_method;
    Button locateOnMap, booking;
    private ProgressDialog pDialog;
    Integer resortID;
    private SQLiteHandler db;

    public ResortViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View  root= inflater.inflate(R.layout.fragment_resort_view, container, false);


        Bundle bundle = getArguments();
        resortName = bundle.getString("resortName");
        contactDetails=bundle.getString("phonenumber");
        serviceType=bundle.getString("serviceType");
        latitude=bundle.getDouble("latitude");
        longitude=bundle.getDouble("longitude");
        city=bundle.getString("city");
        imageString=bundle.getString("imageString");
        resortID=bundle.getInt("resortID");

        productImage = root.findViewById(R.id.resort_image);
        companyName = root.findViewById(R.id.companyName);
        phonenumber = root.findViewById(R.id.phonenumber);
        emailaddress = root.findViewById(R.id.emailaddress);
        physicaladdress = root.findViewById(R.id.physicaladdress);
        locateOnMap = root.findViewById(R.id.locateOnMap);
        booking=root.findViewById(R.id.booking);


        companyName.setText("Resort Name: " + resortName);
        phonenumber.setText(contactDetails);
        physicaladdress.setText(serviceType);

        Glide.with(getActivity().getApplicationContext())
                .load(AppConfig.URL_IMAGE+imageString)
                .thumbnail(0.1f)
                .into(productImage);


        locateOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = null;
                if(longitude !=null && longitude !=null) {
                    geoUri = "http://maps.google.com/maps?q=" + latitude + "," + longitude + " (" + resortName + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);

                }
                  else {

                    Toast.makeText(getContext(), "Location of this resort is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Booking  Resort");

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
               // input.setLayoutParams(lp);
                //alertDialog.setView(input);

                alertDialog.setPositiveButton("Book",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                           //     city = input.getText().toString();
                         //    taskLoadUp(city);
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }

        });

       return root;
    }
    private void bookResort ( final String resortName, final Double lat, final Double lon){


        String tag_string_req = "Book Resort";

        pDialog.setMessage("Book resort  ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BOOKING, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Book Response: " + response);
                hideDialog();

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject resorts = jsonArray.getJSONObject(i);


                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Search Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {



            HashMap<String, String> user = db.getUserDetails();

            String email = user.get("email");


            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                HashMap<String, Integer> params2 = new HashMap<>();
                params.put("email", email);
                params2.put("resortID", resortID);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    private void showDialog () {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog () {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}
