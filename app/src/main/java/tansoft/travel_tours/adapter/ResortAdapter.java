package tansoft.travel_tours.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tansoft.travel_tours.R;
import tansoft.travel_tours.domain.Resort;

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
        holder.imageProductImage.setImageResource(R.drawable.cover);
        holder.txtProductName.setText(resortList.get(position).getName());
        holder.txtProductPrice.setText(resortList.get(position).getAmount());
        holder.txtProductWeight.setText(resortList.get(position).getContact());
       // holder.txtProductQty.setText(resortList.get(position).getProductQty());

        holder.imageProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Resort album = resortList.get(position);
                Toast.makeText(context, album.getLatitude() + " is selected", Toast.LENGTH_SHORT).show();

                Uri gmmIntentUri;
                if (album.getLatitude() != null && album.getLongitude() != null) {
                    gmmIntentUri = Uri.parse("geo:" + album.getLatitude()
                            + "," + album.getLongitude()
                            + "?q=" + album.getLatitude()
                            + "," + album.getLongitude()
                            + "(" + album.getName() + " - " + album.getServiceType() + ")");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    context.startActivity(mapIntent);



                } else {
                    gmmIntentUri = Uri.parse("geo:0,0?q=" + album.getName());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    //  startActivity(mapIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return resortList.size();
    }

    public class ResortProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProductImage;
        TextView txtProductName;
        TextView txtProductPrice;
        TextView txtProductWeight;
        TextView txtProductQty;
        public ResortProductViewHolder(View view) {
            super(view);
            imageProductImage=view.findViewById(R.id.idProductImage);
            txtProductName=view.findViewById(R.id.idProductName);
            txtProductPrice = view.findViewById(R.id.idProductPrice);
            txtProductWeight = view.findViewById(R.id.idProductWeight);
            txtProductQty = view.findViewById(R.id.idProductQty);
        }
    }
}