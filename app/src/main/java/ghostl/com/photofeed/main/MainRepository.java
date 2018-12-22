package ghostl.com.photofeed.main;

import android.location.Location;

public interface MainRepository {
    void logout();
    void uploadPhoto(Location location, String path);
}
