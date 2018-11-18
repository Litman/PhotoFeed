package ghostl.com.photofeed.libs.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LibsModule.class, PhotoFeedAppModule.class})
public interface LibsComponent {

}
