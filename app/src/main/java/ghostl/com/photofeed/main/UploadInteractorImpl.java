package ghostl.com.photofeed.main;

import android.location.Location;

public class UploadInteractorImpl implements UploadInteractor{
    private MainRepository mainRepository;

    public UploadInteractorImpl(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Override
    public void execute(Location location, String path) {
        mainRepository.uploadPhoto(location, path);
    }
}
