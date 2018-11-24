package ghostl.com.photofeed.libs;



import android.util.Log;

import ghostl.com.photofeed.libs.base.EventBus;


public class GreenRobotEventBus implements EventBus{
    private String TAG = GreenRobotEventBus.class.getName();
    org.greenrobot.eventbus.EventBus eventBus;


    public GreenRobotEventBus() {
        Log.d(TAG, "enter 1");
        this.eventBus = org.greenrobot.eventbus.EventBus.getDefault();
        Log.d(TAG, eventBus.toString());
    }

    @Override
    public void register(Object subscriber) {
        Log.d(TAG, "enter Register");
        eventBus.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }
}
