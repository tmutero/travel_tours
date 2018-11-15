package tansoft.travel_tours.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import tansoft.travel_tours.activity.LoginActivity;
import tansoft.travel_tours.activity.MainActivity;
import tansoft.travel_tours.activity.preference.SettingsActivity;
import tansoft.travel_tours.adapter.ResortAdapter;
import tansoft.travel_tours.config.AppConfig;
import tansoft.travel_tours.config.AppController;
import tansoft.travel_tours.domain.Resort;
import tansoft.travel_tours.services.SQLiteHandler;
import tansoft.travel_tours.services.SessionManager;

import static com.android.volley.VolleyLog.TAG;
import static java.util.Collections.sort;


public class ResortViewFragment extends Fragment {

    private  String resortName, contactDetails,serviceType, city,imageString;
    private Double longitude, latitude;
    ImageView productImage;
    TextView  companyName, phonenumber, emailaddress, physicaladdress,  payment_method;
    Button locateOnMap, booking;
    private ProgressDialog pDialog;
    String resortID;
    private SQLiteHandler db;
    private SessionManager session;

    public ResortViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View  root= inflater.inflate(R.layout.fragment_resort_view, container, false);
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);


        Bundle bundle = getArguments();
        resortName = bundle.getString("resortName");
        contactDetails=bundle.getString("phonenumber");
        serviceType=bundle.getString("serviceType");
        latitude=bundle.getDouble("latitude");
        longitude=bundle.getDouble("longitude");
        city=bundle.getString("city");
        imageString=bundle.getString("imageString");

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
        db = new SQLiteHandler(getContext());

        // session manager
        session = new SessionManager(getContext());


        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

         final String clientEmail = user.get("email");


        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Booking  Resort");

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);


                alertDialog.setPositiveButton("Book",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                booking(resortName,clientEmail);


                                System.out.println("------------------------------"+resortName+"======="+clientEmail);
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


    private void booking(final String resortName, final String clientEmail) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Book in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BOOKING, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Booking Response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        Toast.makeText(getContext(), "User successfully booked.", Toast.LENGTH_LONG).show();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Booking Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("resortName", resortName);
                params.put("clientEmail", clientEmail);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
