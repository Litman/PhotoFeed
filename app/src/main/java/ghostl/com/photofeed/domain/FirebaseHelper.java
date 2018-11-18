package ghostl.com.photofeed.domain;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {
    private DatabaseReference databaseReference;
    private final static String SEPARATOR = "____";
    private final static String CHATS_PATH = "chats";
    private final static String USERS_PATH = "users";
    private final static String CONTACTS_PATH = "contacts";

    private static FirebaseHelper getInstance(){
        return SingleHolder.INSTANCE;
    }

    public FirebaseHelper(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public String getAuthUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = null;
        if(email != null){
            email = user.getEmail();
        }
        return email;
    }

    public DatabaseReference getUserReference(String email){
        DatabaseReference userReference = null;
        if(email != null){
            String emailKey = email.replace(".", "_");
            userReference = databaseReference.getRoot().child(USERS_PATH).child(emailKey);
        }
        return userReference;
    }

    public DatabaseReference getMyUserReference(){
        return getUserReference(getAuthUserEmail());
    }

    public DatabaseReference getContactsReference(String email){
        return getUserReference(email).child(CONTACTS_PATH);
    }

    public DatabaseReference getMyContactsReference(){
        return getContactsReference(getAuthUserEmail());
    }

    public DatabaseReference getOneContactReference(String mainEmail, String childEmail){
        String childKey = childEmail.replace(".", "_");
        return getUserReference(mainEmail).child(CONTACTS_PATH).child(childKey);
    }

    public void changeUserConnectionStatus(boolean online){
        if(getMyUserReference() != null){
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("online", online);
            getMyUserReference().updateChildren(updates);
            notifyContactsOfConnectionChange(online);
        }
    }

    private void notifyContactsOfConnectionChange(boolean online) {
        notifyContactsOfConnectionChange(online, false);
    }

    private void notifyContactsOfConnectionChange(final boolean online, final boolean signOff) {
        final String myEmail = getAuthUserEmail();
        getMyContactsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    String email = child.getKey();
                    DatabaseReference reference = getOneContactReference(email, myEmail);
                    reference.setValue(online);
                }
                if(signOff){
                    FirebaseAuth.getInstance().signOut();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void signOff(){
        FirebaseAuth.getInstance().signOut();
    }

}
