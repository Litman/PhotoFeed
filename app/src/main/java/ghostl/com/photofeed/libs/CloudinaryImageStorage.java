package ghostl.com.photofeed.libs;

import android.content.Context;
import android.os.AsyncTask;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.util.Map;

import ghostl.com.photofeed.libs.base.EventBus;
import ghostl.com.photofeed.libs.base.ImageStorage;
import ghostl.com.photofeed.libs.base.ImageStorageFinishedListener;

public class CloudinaryImageStorage implements ImageStorage {

    private EventBus eventBus;
    private Cloudinary cloudinary;

    public CloudinaryImageStorage(Context context, EventBus eventBus) {
        this.eventBus = eventBus;
        this.cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(context));
    }

    @Override
    public String getImageUrl(String id) {
        return cloudinary.url().generate(id);
    }

    @Override
    public void upload(final File file, final String id, final ImageStorageFinishedListener listener) {
        new AsyncTask<Void, Void, Void>(){

            boolean success = false;
            @Override
            protected Void doInBackground(Void... voids) {
                Map params = ObjectUtils.asMap("public_id", id);

                try{
                    cloudinary.uploader().upload(file, params);
                    success = true;
                }catch (Exception ex){
                    listener.onError(ex.getMessage());
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(success){
                    listener.onSuccess();
                }
            }
        }.execute();
    }
}
