package ghostl.com.photofeed.main.di;

import javax.inject.Singleton;

import dagger.Component;
import ghostl.com.photofeed.PhotoFeedAppModule;
import ghostl.com.photofeed.domain.di.DomainModule;
import ghostl.com.photofeed.libs.di.LibsModule;
import ghostl.com.photofeed.main.ui.MainActivity;

@Singleton
@Component(modules = {MainModule.class, DomainModule.class, LibsModule.class, PhotoFeedAppModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
