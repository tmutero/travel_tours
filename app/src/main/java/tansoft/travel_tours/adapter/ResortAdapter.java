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
        holder.resortName.setText(resortList.get(position).getName());
        holder.resortService.setText(resortList.get(position).getServiceType());

        Glide.with(context)
                .load("http://192.168.20.184/travelTours/uploads/"+resort.getId()+"png")
                .into(holder.imageResortImage);

        holder.imageResortImage.setOnClickListener(new View.OnClickListener() {
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

                ResortViewFragment addProductFragment = new ResortViewFragment();
                addProductFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frag_container, addProductFragment).commit();

//                Intent myIntent = new Intent(ResortAdapter.this, ResortViewFragment.class);
//                myIntent.putExtra("resortName", resort.getAmount());
//                myIntent.putExtra("latitude", resort.getLatitude());
//                context.startActivity(myIntent);

//                Integer intent = new Intent(this, ResortViewActivity.this);
//                intent.putExtra("YourValueIdentifier", YourAdapter[e.Position]);
//
//                context.startActivity(intent);



//                Uri gmmIntentUri;
//                if (resort.getLatitude() != null && resort.getLongitude() != null) {
//                    gmmIntentUri = Uri.parse("geo:" + resort.getLatitude()
//                            + "," + resort.getLongitude()
//                            + "?q=" + resort.getLatitude()
//                            + "," + resort.getLongitude()
//                            + "(" + resort.getName() + " - " + resort.getServiceType() + ")");
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//                    context.startActivity(mapIntent);
//
//
//
//                } else {
//                    gmmIntentUri = Uri.parse("geo:0,0?q=" + resort.getName());
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//                    //  startActivity(mapIntent);
//                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return resortList.size();
    }

    public class ResortProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageResortImage;
        TextView resortName;
        TextView resortService;
        public ResortProductViewHolder(View view) {
            super(view);
            imageResortImage=view.findViewById(R.id.thumbnail);
            resortName=view.findViewById(R.id.title);
            resortService = view.findViewById(R.id.desc);

        }
    }
}