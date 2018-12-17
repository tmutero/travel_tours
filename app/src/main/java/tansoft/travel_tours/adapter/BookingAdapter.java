package tansoft.travel_tours.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

import tansoft.travel_tours.R;
import tansoft.travel_tours.domain.Resort;
import tansoft.travel_tours.fragment.BookingFragment;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder>{


private List<Resort> resortList;
        Context context;

    public BookingAdapter(List<Resort> resortList, Context context) {
        this.resortList = resortList;
        this.context = context;

    }

    @NonNull
    @Override
    public BookingAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bookindVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_booking_card, parent, false);
        BookingAdapter.BookingViewHolder gvh = new BookingAdapter.BookingViewHolder(bookindVIew);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.BookingViewHolder holder, int position) {

        holder.resortName.setText(resortList.get(position).getName());
        holder.serviceType.setText(resortList.get(position).getServiceType());
        holder.dateCreated.setText(resortList.get(position).getDateCreated());


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {


        TextView resortName;
        TextView serviceType, dateCreated;
        public BookingViewHolder(View view) {
            super(view);

            resortName=view.findViewById(R.id.resortName);
            serviceType = view.findViewById(R.id.serviceType);
         //dateCreated=view.findViewById(R.id.dateCreated);
        }
    }
}
