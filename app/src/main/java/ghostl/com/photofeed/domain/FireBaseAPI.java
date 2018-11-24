package ghostl.com.photofeed.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.client.AuthData;


import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ghostl.com.photofeed.entities.Photo;

public class FireBaseAPI {
    private Firebase firebase;
    private DatabaseReference mPhotoDatabaseReference;
    private ChildEventListener photosEventListener;

    public FireBaseAPI(Firebase firebase) {
        this.firebase = firebase;
        mPhotoDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void checkForData(final FirebaseActionListenerCallback listener){
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    listener.onSuccess();
                }else{
                    listener.onError(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FIREBASE API", databaseError.getMessage());
            }
        };

        mPhotoDatabaseReference.addValueEventListener(postListener);

    }

    public void subscribe(final FirebaseEventListenerCallback listener){
        if(photosEventListener == null){
            photosEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    listener.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    listener.onChildRemove(dataSnapshot);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    listener.onCancelled(databaseError);
                }
            };
        }

        mPhotoDatabaseReference.addChildEventListener(photosEventListener);
    }

    public void unsubscribe(){
        mPhotoDatabaseReference.removeEventListener(photosEventListener);
    }

    public String create(){
        return firebase.push().getKey();
    }


    public void update(Photo photo){
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child(photo.getId()).setValue(photo);
    }

    public void remove(Photo photo, FirebaseActionListenerCallback listener){
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child(photo.getId()).removeValue();

        listener.onSuccess();
    }

    public String getAuthEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return user.getEmail();
        }
        return null;
    }

    public void signUp(String email, String password, final FirebaseActionListenerCallback listener){
        firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                listener.onSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.onError(firebaseError);
            }
        });
    }

    public void login(String email, String password, final FirebaseActionListenerCallback listener){
        firebase.authWithPassword(email, password, new Firebase.AuthResultHandler(){

            @Override
            public void onAuthenticated(AuthData authData) {
                listener.onSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.onError(firebaseError);
            }
        });
    }

    public void checkForSession(FirebaseActionListenerCallback listener){
        if(firebase.getAuth() != null){
            listener.onSuccess();
        }else{
            listener.onError(null);
        }

    }

    public void logout(){
        firebase.unauth();
    }

}
