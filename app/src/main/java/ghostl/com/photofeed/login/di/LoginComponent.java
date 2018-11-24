package ghostl.com.photofeed.login.di;

import javax.inject.Singleton;

import dagger.Component;
import ghostl.com.photofeed.PhotoFeedAppModule;
import ghostl.com.photofeed.domain.di.DomainModule;
import ghostl.com.photofeed.libs.di.LibsModule;
import ghostl.com.photofeed.login.ui.LoginActivity;

@Singleton
@Component(modules = {LoginModule.class, DomainModule.class, LibsModule.class, PhotoFeedAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
