package ghostl.com.photofeed.main;

import android.location.Location;

import ghostl.com.photofeed.libs.base.EventBus;
import ghostl.com.photofeed.main.events.MainEvent;
import ghostl.com.photofeed.main.ui.MainView;

public class MainPresenterImpl implements MainPresenter {
    MainView mainView;
    EventBus eventBus;
    UploadInteractor uploadInteractor;
    SessionInteractor sessionInteractor;

    public MainPresenterImpl(MainView mainView, EventBus eventBus, UploadInteractor uploadInteractor, SessionInteractor sessionInteractor) {
        this.mainView = mainView;
        this.eventBus = eventBus;
        this.uploadInteractor = uploadInteractor;
        this.sessionInteractor = sessionInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        mainView = null;
        eventBus.unregister(this);
    }



    @Override
    public void logout() {
        sessionInteractor.logout();
    }

    @Override
    public void uploadPhoto(Location location, String path) {
        uploadInteractor.execute(location, path);
    }

    @Override
    public void onEventMainThread(MainEvent event) {
        String error = event.getError();
        if(this.mainView != null){
            switch (event.getType()){
                case MainEvent.UPLOAD_INIT:
                    mainView.onUploadInit();
                    break;
                case MainEvent.UPLOAD_COMPLETE:
                    mainView.onUploadComplete();
                    break;
                case MainEvent.UPLOAD_ERROR:
                    mainView.onUploadError(error);
                    break;
            }
        }
    }
}
