package tansoft.travel_tours.fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import tansoft.travel_tours.adapter.RecomendedAdapter;
import tansoft.travel_tours.adapter.ResortAdapter;
import tansoft.travel_tours.config.AppConfig;
import tansoft.travel_tours.config.AppController;
import tansoft.travel_tours.domain.Resort;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.android.volley.VolleyLog.TAG;
import static java.util.Collections.sort;


public class RecomendedFragment extends Fragment {

    List<Resort> resortList;
    RecyclerView rv;
    private ProgressDialog pDialog;

    Context mContext;
    RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;


    public RecomendedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_recomended, container, false);

//Preference Engine Search Results
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String preferedService=sharedPrefs.getString("service_type", "Hotel and Conferences");
        final String preferedCity= sharedPrefs.getString("city_list", "Harare");


        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        rv = root.findViewById(R.id.idRecyclerViewRecomended);
        if (rv != null) {
            rv.setHasFixedSize(true);
        }
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //mRecyclerView.setLayoutManager(mLayoutManager);
        rv.setLayoutManager(mLayoutManager);

        resortList = new ArrayList<>();



        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        locationTrack = new LocationTrack(getContext());




        if (locationTrack.canGetLocation()) {


            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
            recomendResorts("",latitude,longitude);

        } else {

            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getContext());
            alertDialog.setTitle("Check Location Settings for recomendation list");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            alertDialog.setPositiveButton("Change",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            permissionsToRequest = findUnAskedPermissions(permissions);
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




        return root;
    }
    private void recomendResorts ( final String resortName, final Double lat, final Double lon){


        String tag_string_req = "recomend";

        pDialog.setMessage("Loading  ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RESORT_SEARCH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Recomended Response: " + response);
                hideDialog();

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject resorts = jsonArray.getJSONObject(i);

                        resortList.add(new Resort(
                                resorts.getString("name"),
                                resorts.getString("contact"),
                                resorts.getString( "serviceType" ),
                                resorts.getInt( "resortID" ),
                                resorts.getDouble("latitude"),
                                resorts.getDouble("longitude"),
                                resorts.getString("imageString"),
                                resorts.getString("distance")

                        ));

                    }
                    sort(resortList);
                    rv.setAdapter(new RecomendedAdapter(resortList, getContext()));

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
                HashMap<String, Double> params2 = new HashMap<>();
                params.put("resortName", resortName);
                params2.put("latitude", lat);
                params2.put("longitude", lon);


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
