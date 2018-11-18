package ghostl.com.photofeed.libs.base;

import java.io.File;

public interface ImageStorage {
    String getImageUrl(String id);
    void upload(File file, String id, ImageStorageFinishedListener listener);
}
