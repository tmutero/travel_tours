package tansoft.travel_tours.adapter;

import android.app.Activity;
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
import tansoft.travel_tours.fragment.RecomendedFragment;
import tansoft.travel_tours.fragment.ResortViewFragment;

public class RecomendedAdapter   extends RecyclerView.Adapter<RecomendedAdapter.ResortProductViewHolder>{

    private List<Resort> resortList;
    RecomendedFragment activity;

    public RecomendedAdapter(List<Resort> resortList, RecomendedFragment activity) {
        this.resortList = resortList;
        this.activity = activity;
    }

    @Override
    public RecomendedAdapter.ResortProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View ResortProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recomended_card, parent, false);
        RecomendedAdapter.ResortProductViewHolder gvh = new RecomendedAdapter.ResortProductViewHolder(ResortProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(RecomendedAdapter.ResortProductViewHolder holder, final int position) {

        final Resort resort = resortList.get(position);
        holder.textViewTitle.setText(resortList.get(position).getName());
        holder.textViewShortDesc.setText(resortList.get(position).getServiceType());
        holder.textDistance.setText(resortList.get(position).getDistance()+" :"+"KM from here.");
        holder.textPrice.setText("$"+" "+resortList.get(position).getAmount());

        Glide.with(activity)
                .load(AppConfig.URL_IMAGE+resort.getImageString())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AppCompatActivity activity = (AppCompatActivity) v.getContexttext();
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
                activity.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frag_container, addProductFragment).commit();


            }
        });
    }

    @Override
    public int getItemCount() {
        return resortList.size();
    }

    public class ResortProductViewHolder extends RecyclerView.ViewHolder {
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
}
