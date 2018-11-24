package ghostl.com.photofeed.login.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ghostl.com.photofeed.PhotoFeedActivity;
import ghostl.com.photofeed.R;
import ghostl.com.photofeed.login.LoginPresenter;
import ghostl.com.photofeed.PhotoFeedApp;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.bSignIn)
    Button bSignIn;
    @Bind(R.id.bSignUp)
    Button bSignUp;
    @Bind(R.id.pbLogin)
    ProgressBar pbLogin;
    @Bind(R.id.rlMainContainer)
    RelativeLayout rlMainContainer;

    @Inject
    LoginPresenter loginPresenter;

    @Inject
    SharedPreferences sharedPreferences;

    private PhotoFeedApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        app = (PhotoFeedApp) this.getApplication();

        setupInjection();
        loginPresenter.onCreate();
        loginPresenter.login(null, null);

    }

    private void setupInjection() {
        app.getLoginComponent(this).inject(this);
    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();

    }

    @Override
    @OnClick(R.id.bSignIn)
    public void handleSignIn(){
        loginPresenter.registerNewUser(etEmail.getText().toString(), etPassword.getText().toString());
    }


    @Override
    @OnClick(R.id.bSignUp)
    public void handleSignUp(){
        loginPresenter.login(etEmail.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void enableInputs() {
        setInputs(false);
    }

    @Override
    public void disableInputs() {
        setInputs(true);
    }

    private void setInputs(boolean b) {
    }

    @Override
    public void showProgress() {
        pbLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLogin.setVisibility(View.GONE);
    }



    @Override
    public void newUserSuccess() {

    }

    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, PhotoFeedActivity.class));
    }

    @Override
    public void setUserEmail(String email) {
        if(email != null){
            String key = app.getEmailKey();
            sharedPreferences.edit().putString(key, email).commit();
        }
    }

    @Override
    public void loginError(String error) {
        etPassword.setText("");
        @SuppressLint("StringFormatInvalid") String msgError = String.format(getString(R.string.login_error_message_sigin), error);
        etPassword.setText(msgError);
    }

    @Override
    public void newUserError(String error) {
        etPassword.setText("");
        @SuppressLint("StringFormatInvalid") String msgError = String.format(getString(R.string.login_error_message_sigup), error);
        etPassword.setText(msgError);
    }
}
