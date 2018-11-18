package ghostl.com.photofeed.domain;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    listener.onSuccess();
                }else{
                    listener.onError();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("FIREBASE API", firebaseError.getMessage());
            }
        };

        mPhotoDatabaseReference.addValueEventListener(postListener);

    }

    public void subscribe(final FirebaseEventListenerCallback listener){
        if(photosEventListener == null){
            photosEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listener.onChildAdded(dataSnapShot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    listener.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    listener.onCancelled(firebaseError);
                }
            };
        }

        mPhotoDatabaseReference.addChildEventListener(photosEventListener);
    }

    public void unsubscribe(final Firebas){
        mPhotoDatabaseReference.removeEventListener(photosEventListener);
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

}
