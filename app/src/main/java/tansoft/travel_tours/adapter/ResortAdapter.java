package tansoft.travel_tours.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tansoft.travel_tours.R;
import tansoft.travel_tours.config.AppConfig;
import tansoft.travel_tours.domain.Resort;
import tansoft.travel_tours.fragment.ResortViewFragment;

public class ResortAdapter extends RecyclerView.Adapter<ResortAdapter.ResortProductViewHolder>{
    private List<Resort> resortList;
    Context context;

    public ResortAdapter(List<Resort> resortList, Context context) {
        this.resortList = resortList;
        this.context = context;

    }

    @Override
    public ResortProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View ResortProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_resort_card, parent, false);
        ResortProductViewHolder gvh = new ResortProductViewHolder(ResortProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(ResortProductViewHolder holder, final int position) {
        final Resort resort = resortList.get(position);
        holder.textViewTitle.setText(resortList.get(position).getName());
        holder.textViewShortDesc.setText(resortList.get(position).getServiceType());
        holder.textDistance.setText(resortList.get(position).getDistance()+" :"+"KM from here.");
        holder.textPrice.setText("$"+" "+resortList.get(position).getAmount());

        Glide.with(context)
                .load(AppConfig.URL_IMAGE+resort.getImageString())
                .into(holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Bundle bundle = new Bundle();
                bundle.putString("resortName", resort.getName());
                bundle.putDouble("latitude", resort.getLatitude());
                bundle.putDouble("longitude", resort.getLongitude());
                bundle.putString("contact", resort.getContact());
                bundle.putString("city", resort.getCity());
                bundle.putString("serviceType", resort.getServiceType());
                bundle.putString("imageString",resort.getImageString());
                bundle.putString("resortID",resort.getId());

                ResortViewFragment addProductFragment = new ResortViewFragment();
                addProductFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frag_container, addProductFragment).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return resortList.size();
    }

    public class ResortProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageResortImage;
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewShortDesc, textDistance,textPrice;
        public ResortProductViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.imageView);
            textViewTitle=view.findViewById(R.id.resort_name);
            textViewShortDesc = view.findViewById(R.id.textViewShortDesc);
            textDistance=view.findViewById(R.id.textDistance);
            textPrice=view.findViewById(R.id.textPrice);
        }
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = (dist * Math.PI / 180.0);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344 * 1000;

        return (dist);
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

}