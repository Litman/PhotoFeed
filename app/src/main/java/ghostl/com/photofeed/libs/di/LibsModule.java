package ghostl.com.photofeed.libs.di;

import android.support.v4.app.Fragment;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ghostl.com.photofeed.libs.CloudinaryImageStorage;
import ghostl.com.photofeed.libs.GlideImageLoader;
import ghostl.com.photofeed.libs.GreenRobotEventBus;
import ghostl.com.photofeed.libs.base.EventBus;
import ghostl.com.photofeed.libs.base.ImageLoader;
import ghostl.com.photofeed.libs.base.ImageStorage;

@Module
public class LibsModule {

    private Fragment fragment;

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @Singleton
    EventBus providesEventBus(){
        return new GreenRobotEventBus();
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader(Fragment fragment){
        GlideImageLoader imageLoader = new GlideImageLoader();
        if(fragment != null){
            imageLoader.setLoaderContext(fragment);
        }
        return imageLoader;
    }

    @Provides
    @Singleton
    ImageStorage providesImageStorage(Context context, EventBus eventBus){
        ImageStorage imageStorage = new CloudinaryImageStorage(context, eventBus);
        return imageStorage;
    }


    @Provides
    @Singleton
    Fragment providesFragment(){
        return this.fragment;
    }

}
