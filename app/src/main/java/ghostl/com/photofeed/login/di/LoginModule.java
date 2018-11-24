package ghostl.com.photofeed.login.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ghostl.com.photofeed.domain.FireBaseAPI;
import ghostl.com.photofeed.libs.base.EventBus;
import ghostl.com.photofeed.login.LoginInteractor;
import ghostl.com.photofeed.login.LoginInteractorImpl;
import ghostl.com.photofeed.login.LoginPresenter;
import ghostl.com.photofeed.login.LoginPresenterImpl;
import ghostl.com.photofeed.login.LoginRepository;
import ghostl.com.photofeed.login.LoginRepositoryImpl;
import ghostl.com.photofeed.login.SignupInteractor;
import ghostl.com.photofeed.login.SignupInteractorImpl;
import ghostl.com.photofeed.login.ui.LoginView;

@Module
public class LoginModule {
    LoginView loginView;

    public LoginModule(LoginView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    LoginView providesLoginView(){
        return this.loginView;
    }

    @Provides
    @Singleton
    LoginPresenter providesLoginPresenter(EventBus eventBus, LoginView loginView, LoginInteractor loginInteractor, SignupInteractor signupInteractor){
        return new LoginPresenterImpl(eventBus, loginView, loginInteractor, signupInteractor);
    }

    @Provides
    @Singleton
    LoginInteractor providesLoginInteractor(LoginRepository loginRepository){
        return new LoginInteractorImpl(loginRepository);
    }

    @Provides
    @Singleton
    SignupInteractor providesSignupInteractor(LoginRepository loginRepository){
        return new SignupInteractorImpl(loginRepository);
    }

    @Provides
    @Singleton
    LoginRepository providesLoginRepository(EventBus eventBus, FireBaseAPI firebase){
        return  new LoginRepositoryImpl(eventBus, firebase);
    }

}
