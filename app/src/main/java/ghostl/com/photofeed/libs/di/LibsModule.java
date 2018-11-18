package ghostl.com.photofeed.libs.di;

import android.app.Fragment;

import dagger.Module;

@Module
public class LibsModule {

    private Fragment fragment;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }


}
