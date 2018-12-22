package ghostl.com.photofeed.main;

public class SessionInteractorImpl implements SessionInteractor{
    MainRepository mainRepository;

    public SessionInteractorImpl(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Override
    public void logout() {
        mainRepository.logout();
    }
}
