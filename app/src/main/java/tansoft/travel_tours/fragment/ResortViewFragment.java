package tansoft.travel_tours.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import tansoft.travel_tours.R;


public class ResortViewFragment extends Fragment {

    private  String resortName, contactDetails,serviceType, city;
    private Double longitude, latitude;
    ImageView productImage;
    TextView  companyName, phonenumber, emailaddress, physicaladdress,  payment_method;
    Button locateOnMap, booking;

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
                .load("http://192.168.20.184/travelTours/uploads/6.jpeg")
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
                final EditText input = new EditText(getContext());
                input.setText(city);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Book",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                           //     city = input.getText().toString();
                         //       taskLoadUp(city);
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


}
