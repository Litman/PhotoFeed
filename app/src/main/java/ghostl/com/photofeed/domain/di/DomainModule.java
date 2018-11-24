package ghostl.com.photofeed.domain.di;

import android.content.Context;
import android.location.Geocoder;

import com.firebase.client.Firebase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ghostl.com.photofeed.domain.FireBaseAPI;
import ghostl.com.photofeed.domain.Util;

@Module
public class DomainModule {
    private final static String FIREBASE_URL = "https://galileo-android-photo.firebaseio.com/";

    @Provides
    @Singleton
    FireBaseAPI providesFireBaseAPI(Firebase firebase){
        return new FireBaseAPI(firebase);
    }

    @Provides
    @Singleton
    Firebase providesFirebase(){
        return new Firebase(FIREBASE_URL);
    }

    @Provides
    @Singleton
    Util providesUtil(Geocoder geocoder){
        return new Util(geocoder);
    }

    @Provides
    @Singleton
    Geocoder providesGeocoder(Context context){
        return new Geocoder(context);
    }

}
