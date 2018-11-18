package ghostl.com.photofeed.login;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import ghostl.com.photofeed.libs.base.EventBus;

public class LoginRepositoryImpl implements LoginRepository {
    private EventBus eventBus;
    private FirebaseAPI firebase;
    private DatabaseReference databaseReference;
    private DatabaseReference myUserReference;

    public LoginRepositoryImpl(EventBus eventBus, FirebaseAPI firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;
    }

    @Override
    public void signUp(final String email, final String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        post(LoginEvent.onSigUpSuccess);
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
                                post(LoginEvent.onSignInSucces, null, email);
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
        loginEvent.setEvenType(type);
        if(errorMessage != null){
            loginEvent.setErroroMessage(errorMessage);
        }
        loginEvent.setLoggedUserEmail(loggerUserEmail);
        eventBus.post(loginEvent);
    }

}
