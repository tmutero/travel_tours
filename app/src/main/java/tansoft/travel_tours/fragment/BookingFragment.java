package tansoft.travel_tours.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import tansoft.travel_tours.Utils.GpsTracker;
import tansoft.travel_tours.adapter.BookingAdapter;
import tansoft.travel_tours.adapter.RecomendedAdapter;
import tansoft.travel_tours.config.AppConfig;
import tansoft.travel_tours.config.AppController;
import tansoft.travel_tours.domain.Resort;
import tansoft.travel_tours.services.SQLiteHandler;
import tansoft.travel_tours.services.SessionManager;

import static com.android.volley.VolleyLog.TAG;
import static java.util.Collections.sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment {

    private GpsTracker gpsTracker;
    List<Resort> resortList;
    RecyclerView rv;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    Context mContext;
    RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    public BookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View root= inflater.inflate(R.layout.fragment_booking, container, false);


        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        rv = root.findViewById(R.id.idBookedView);
        if (rv != null) {
            rv.setHasFixedSize(true);
        }
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.GAP_HANDLING_LAZY);
        //mRecyclerView.setLayoutManager(mLayoutManager);
        rv.setLayoutManager(mLayoutManager);

        resortList = new ArrayList<>();
        getBooked();
        return root;
    }


    private void getBooked (){
        db = new SQLiteHandler(getContext());



        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        final String clientID = user.get("uid");

        String tag_string_req = "booked";

        pDialog.setMessage("Loading  ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BOOKING, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Booked Response: " + response);
                hideDialog();

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject resorts = jsonArray.getJSONObject(i);


                        resortList.add(new Resort(
                                resorts.getString("resortID"),
                                resorts.getString( "name" ),
                                resorts.getString( "serviceType" ),
                                resorts.getString("dateCreated")

                        ));


                    }

                    rv.setAdapter(new BookingAdapter(resortList, getContext()));

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
                params.put("clientID", clientID);

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
