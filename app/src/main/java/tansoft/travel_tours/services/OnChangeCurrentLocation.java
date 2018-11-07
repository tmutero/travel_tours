package tansoft.travel_tours.services;

import android.location.Location;

/**
 * Created by scott on 26/04/2017.
 */

public interface OnChangeCurrentLocation {

    void handleNewLocationAvailable(Location location);


}
