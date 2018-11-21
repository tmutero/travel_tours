package tansoft.travel_tours.fragment;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tansoft.travel_tours.R;
import tansoft.travel_tours.Utils.LocationTrack;
import tansoft.travel_tours.Utils.GpsTracker;
import tansoft.travel_tours.adapter.ResortAdapter;
import tansoft.travel_tours.config.AppConfig;
import tansoft.travel_tours.config.AppController;
import tansoft.travel_tours.domain.Resort;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends FragmentBase {
    private GpsTracker gpsTracker;
    private Button btnSearch;
    private EditText textSearchResort;
    private ProgressDialog pDialog;

    Context mContext;
    RecyclerView.LayoutManager mLayoutManager;
    List<Resort> resortList;
    RecyclerView rv;


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;



    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  View view = inflater.inflate(R.layout.fragment_search, container, false);

        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = getContext();


        try {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        textSearchResort = v.findViewById(R.id.search_resort);

        btnSearch = v.findViewById(R.id.btnsearch_resort);

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);


        rv = v.findViewById(R.id.idRecyclerView);
        if (rv != null) {
            rv.setHasFixedSize(true);
        }
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mLayoutManager);
        rv.setLayoutManager(mLayoutManager);

        resortList = new ArrayList<>();



        try {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }




        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (textSearchResort.getText() == null) {
                    displayNotification("Enter resort Name");
                } else {
                    String name = textSearchResort.getText().toString().toLowerCase();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textSearchResort.getWindowToken(), 0);

                    gpsTracker = new GpsTracker(getContext());
                    if(gpsTracker.canGetLocation()){
                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();
                       System.out.println("----------------------------------------"+longitude);
                         getResorts(name,latitude,longitude);


                    }else{


                         getResorts(name,0.0,0.0);


                        gpsTracker.showSettingsAlert();
                    }




                }
            }
        });




        return v;

    }


    private void getResorts ( final String resortName, final Double lat, final Double lon){

        String tag_string_req = "req_login";
        System.out.println("---------------------------------------"+String.valueOf(lat));
        Double d= Double.valueOf(String.valueOf(lat));
        pDialog.setMessage("Search  ..." + resortName);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RESORT_SEARCH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Search Response: " + response);
                hideDialog();

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject resorts = jsonArray.getJSONObject(i);

                        resortList.add(new Resort(
                                resorts.getString("name"),
                                resorts.getString("contact"),
                                resorts.getString( "serviceType" ),
                                resorts.getString( "resortID" ),
                                resorts.getDouble("latitude"),
                                resorts.getDouble("longitude"),
                                resorts.getString("imageString"),
                                resorts.getString("distance"),
                                resorts.getString("amount")



                        ));

                    }
                 //  sort(resortList);
                    rv.setAdapter(new ResortAdapter(resortList, getContext()));

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

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();


                params.put("resortName", resortName);
                params.put("latitude", String.valueOf(lat));
                params.put("longitude", String.valueOf(lon));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void showDialog () {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }



    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }




}
