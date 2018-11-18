package ghostl.com.photofeed.domain;

import com.google.firebase.FirebaseError;

public interface FirebaseActionListenerCallback {
    void onSuccess();
    void onError(FirebaseError error);
}
