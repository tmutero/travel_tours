package tansoft.travel_tours.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public abstract class FragmentBase extends Fragment {


    public void displayNotification(String message) {
        Toast.makeText(this.getActivity(), message,
                Toast.LENGTH_LONG).show();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public boolean checkInternetConnectivity() {
        if (isOnline() == true)
            return true;
        else
            displayNotification("Cannot connect to the internet please try again later");
        return false;
    }



}
