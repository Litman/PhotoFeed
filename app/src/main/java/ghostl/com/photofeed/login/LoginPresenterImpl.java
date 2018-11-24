package ghostl.com.photofeed.login;


import org.greenrobot.eventbus.Subscribe;

import ghostl.com.photofeed.libs.base.EventBus;
import ghostl.com.photofeed.login.events.LoginEvent;
import ghostl.com.photofeed.login.ui.LoginView;

public class LoginPresenterImpl implements LoginPresenter{

    EventBus eventBus;
    LoginView loginView;
    LoginInteractor loginInteractor;
    SignupInteractor signupInteractor;

    public LoginPresenterImpl(EventBus eventBus, LoginView loginView, LoginInteractor loginInteractor, SignupInteractor signupInteractor) {
        this.eventBus = eventBus;
        this.loginView = loginView;
        this.loginInteractor = loginInteractor;
        this.signupInteractor = signupInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
        eventBus.unregister(this);
    }

    @Override
    @Subscribe
    public void onEventMainThread(LoginEvent event) {
        switch (event.getEventType()){
            case LoginEvent.onSignInError:
                onSignError(event.getErrorMessage());
                break;
            case LoginEvent.onSignInSuccess:
                onSignInSuccess(event.getLoggedUserEmail());
                break;
            case LoginEvent.onSignUpError:
                onSignUpError(event.getErrorMessage());
                break;
            case LoginEvent.onSignUpSuccess:
                onSignUpSuccess();
                break;
            case LoginEvent.onFailedToRecoverSession:
                onFailedToRecoverSession();
                break;
        }
    }

    private void onFailedToRecoverSession() {
        if(loginView != null){
            loginView.hideProgress();
            loginView.enableInputs();
        }
    }

    private void onSignUpSuccess() {
        if(loginView != null){
            loginView.newUserSuccess();
        }

    }

    private void onSignUpError(String errorMessage) {
        if (loginView != null) {
            loginView.hideProgress();
            loginView.enableInputs();
            loginView.newUserError(errorMessage);
        }
    }

    private void onSignInSuccess(String loggedUserEmail) {
        if(loginView != null){
            loginView.setUserEmail(loggedUserEmail);
            loginView.navigateToMainScreen();
        }
    }

    private void onSignError(String errorMessage) {
        if(loginView != null){
            loginView.hideProgress();
            loginView.enableInputs();
            loginView.loginError(errorMessage);
        }
    }

    @Override
    public void login(String email, String password) {
        if(loginView != null){
            loginView.disableInputs();
            loginView.showProgress();
        }
        loginInteractor.execute(email, password);
    }

    @Override
    public void registerNewUser(String email, String password) {
        if(loginView != null){
            loginView.disableInputs();
            loginView.showProgress();
        }
        signupInteractor.execute(email, password);
    }
}
