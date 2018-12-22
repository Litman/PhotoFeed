package ghostl.com.photofeed.main.di;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ghostl.com.photofeed.main.MainPresenter;
import ghostl.com.photofeed.main.ui.MainView;

@Module
public class MainModule {

    private MainView mainView;
    private String [] titles;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    public MainModule(MainView mainView, String[] titles, Fragment[] fragments, FragmentManager fragmentManager) {
        this.mainView = mainView;
        this.titles = titles;
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
    }
    @Provides
    @Singleton
    MainView providesMainView(){
        return this.mainView;
    }

    @Provides
    @Singleton
    MainPresenter providesMainPresenter(){

    }
}
