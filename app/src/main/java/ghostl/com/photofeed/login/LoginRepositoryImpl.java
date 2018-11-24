package ghostl.com.photofeed.login;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import ghostl.com.photofeed.domain.FireBaseAPI;
import ghostl.com.photofeed.domain.FirebaseActionListenerCallback;
import ghostl.com.photofeed.libs.base.EventBus;
import ghostl.com.photofeed.login.events.LoginEvent;

public class LoginRepositoryImpl implements LoginRepository {
    private EventBus eventBus;
    private FireBaseAPI firebase;
    private DatabaseReference databaseReference;
    private DatabaseReference myUserReference;

    public LoginRepositoryImpl(EventBus eventBus, FireBaseAPI firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;
    }

    @Override
    public void signUp(final String email, final String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        post(LoginEvent.onSignUpSuccess);
                        signIn(email, password);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        post(LoginEvent.onSignUpSuccess, e.getMessage());

                    }
                });
    }

    @Override
    public void signIn(final String email,final String password) {
        if(email != null && password != null ){
            try {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String emial = firebase.getAuthEmail();
                                post(LoginEvent.onSignInSuccess, null, email);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                post(LoginEvent.onSignInError, e.getMessage());
                            }
                        });
            }catch (Exception ex){
                post(LoginEvent.onSignUpSuccess, ex.getMessage());
            }
        }else{
            firebase.checkForSession(new FirebaseActionListenerCallback(){

                @Override
                public void onSuccess() {
                    String email = firebase.getAuthEmail();
                    post(LoginEvent.onSignInSuccess, null, email);
                }

                @Override
                public void onError(FirebaseError error) {
                    post(LoginEvent.onFailedToRecoverSession);

                }
            });
        }
    }

    private void post(int type){
        post(type, null);
    }

    private void post(int type, String errorMessage) {
        post(type, errorMessage, null);
    }

    private void post(int type, String errorMessage, String loggerUserEmail) {
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setEventType(type);
        if(errorMessage != null){
            loginEvent.setErrorMessage(errorMessage);
        }
        loginEvent.setLoggedUserEmail(loggerUserEmail);
        eventBus.post(loginEvent);
    }

}
