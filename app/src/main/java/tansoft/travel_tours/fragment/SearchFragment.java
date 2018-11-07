package tansoft.travel_tours.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import tansoft.travel_tours.adapter.ResortAdapter;
import tansoft.travel_tours.config.AppConfig;
import tansoft.travel_tours.config.AppController;
import tansoft.travel_tours.domain.Resort;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends  FragmentBase {

    private Button btnSearch;
    private EditText textSearchResort;
    private ProgressDialog pDialog;

    Context mContext;
    RecyclerView.LayoutManager mLayoutManager;
    List<Resort> resortList;
    RecyclerView rv;





    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  View view = inflater.inflate(R.layout.fragment_search, container, false);

        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = getContext();

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


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (textSearchResort.getText() == null) {
                    displayNotification("Enter resort Name");
                } else {
                    String name = textSearchResort.getText().toString().toLowerCase();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textSearchResort.getWindowToken(), 0);

                    if (checkInternetConnectivity()) {

                        getResorts(name, 0.0, 0.0);


                    }


                }
            }
        });




        return v;

    }


    private void getResorts ( final String resortName, final Double lat, final Double lon){


        String tag_string_req = "req_login";

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
                                resorts.getString("name"),
                                resorts.getString( "name" ),
                                resorts.getInt( "longitude" ),
                                resorts.getDouble("latitude"),
                                resorts.getDouble("longitude")

                        ));

                    }
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("resortName", resortName);


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

    private void hideDialog () {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




}
