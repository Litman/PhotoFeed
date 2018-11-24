package ghostl.com.photofeed;

import android.app.Application;

import com.firebase.client.Firebase;

import ghostl.com.photofeed.domain.di.DomainModule;
import ghostl.com.photofeed.libs.di.LibsModule;
import ghostl.com.photofeed.login.di.DaggerLoginComponent;
import ghostl.com.photofeed.login.di.LoginComponent;
import ghostl.com.photofeed.login.di.LoginModule;
import ghostl.com.photofeed.login.ui.LoginView;

public class PhotoFeedApp extends Application{
    private final static String EMAIL_KEY = "email";

    private LibsModule libsModule;
    private DomainModule domainModule;
    private PhotoFeedAppModule photoFeedAppModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initFirebase();
        initModules();
    }

    private void initFirebase() {
        Firebase.setAndroidContext(this);
    }

    private void initModules() {
        libsModule = new LibsModule();
        domainModule = new DomainModule();
        photoFeedAppModule = new PhotoFeedAppModule(this);
    }

    public static String getEmailKey() {
        return EMAIL_KEY;
    }


    public LoginComponent getLoginComponent(LoginView loginView) {
        return DaggerLoginComponent.builder()
                .photoFeedAppModule(photoFeedAppModule)
                .domainModule(domainModule)
                .loginModule(new LoginModule(loginView))
                .build();

    }
}
