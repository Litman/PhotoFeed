package ghostl.com.photofeed.domain;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface FirebaseEventListenerCallback {

    void onChildAdded(DataSnapshot dataSnapshot);
    void onChildRemove(DataSnapshot dataSnapshot);
    void onCancelled(DatabaseError error);
}
