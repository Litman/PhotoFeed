package ghostl.com.photofeed.main;

import android.location.Location;

import ghostl.com.photofeed.main.events.MainEvent;

public interface MainPresenter {

    void onCreate();
    void onDestroy();

    void logout();
    void uploadPhoto(Location location, String path);
    void onEventMainThread(MainEvent event);
}
