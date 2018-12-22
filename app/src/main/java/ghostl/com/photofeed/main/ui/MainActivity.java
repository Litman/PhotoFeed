package ghostl.com.photofeed.main.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ghostl.com.photofeed.Manifest;
import ghostl.com.photofeed.PhotoFeedApp;
import ghostl.com.photofeed.R;
import ghostl.com.photofeed.login.ui.LoginActivity;
import ghostl.com.photofeed.main.MainPresenter;
import ghostl.com.photofeed.main.ui.adapters.MainSectionsPageAdapter;

public class MainActivity extends AppCompatActivity implements MainView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @Bind(R.id.tbMain)
    Toolbar tbMain;
    @Bind(R.id.tlTabs)
    TabLayout tlTabs;
    @Bind(R.id.vpContent)
    ViewPager vpContent;

    private String photoPath;
    private Location lastLocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_PICTURE = 0;
    private final static int PERMISSIONS_REQUEST_READ_MEDIA = 10;
    private final static int PERMISSIONS_REQUEST_LOCATION = 11;

    SharedPreferences sharedPreferences;


    MainSectionsPageAdapter adapter;
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupInjection();
        setupNavigation();
        setupGoogleAPIClient();

        mainPresenter.onCreate();

    }

    private void setupGoogleAPIClient() {
        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void setupNavigation() {
        PhotoFeedApp app = (PhotoFeedApp) getApplication();
        String email = sharedPreferences.getString(app.getEmailKey(), "");
        tbMain.setTitle(email);
        setSupportActionBar(tbMain);

        vpContent.setAdapter(adapter);
        tlTabs.setupWithViewPager(vpContent);
    }

    private void setupInjection() {

        String [] titles = new String[]{getString(R.string.main_title_list), getString(R.string.main_title_map)};

        //Fragment[] fragments = new Fragment[]{new PhotoListFragmen(), new PhotoMapFragment() };

        PhotoFeedApp app = (PhotoFeedApp) getApplication();

        
    }

    @OnClick(R.id.fabButton)
    public void takePicture(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_MEDIA);
        }else{
            takePhoto();
        }
    }

    private void takePhoto() {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        File photoFile = getFile();

        if(photoFile != null){
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if(takePhotoIntent.resolveActivity(getPackageManager()) != null){
                intentList = addIntentsToList(intentList, pickIntent);
            }
        }

        if(intentList.size() > 0){
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    getString(R.string.main_message_picture_source));

            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));


        }

        startActivityForResult(chooserIntent, REQUEST_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_OK && requestCode == REQUEST_PICTURE){
            boolean isCamera = (data == null || data.getData() == null);

            if(isCamera){
                addPicToGallery();

            }else{
                photoPath = getRealPathFromUri(data.getData());
            }

            mainPresenter.uploadPhoto(lastLocation, photoPath);
        }
    }

    private String getRealPathFromUri(Uri contenUri) {
        String result = null;
        Cursor cursor = getContentResolver().query(contenUri, null, null, null, null);
        if(cursor == null){
            result = contenUri.getPath();
        }else{
            if(contenUri.toString().contains("mediaKey")){
                cursor.close();

                try {
                    File file = File.createTempFile("tempImg", ".jpg", getCacheDir());
                    InputStream input = getContentResolver().openInputStream(contenUri);
                    OutputStream output = new FileOutputStream(file);

                    try{
                        byte[] buffer = new byte[4* 1024];
                        int read;

                        while((read = input.read(buffer)) != -1){
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        result = file.getAbsolutePath();

                    }finally {
                        output.close();
                        input.close();
                    }

                }catch (Exception ex){
                    Log.e(MainActivity.class.getSimpleName(), "Error getting file path", ex);
                }
            }else{
                cursor.moveToFirst();
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(dataColumn);
                cursor.close();
            }

        }

        return result;
    }

    private void addPicToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(photoPath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private List<Intent> addIntentsToList(List<Intent> intentList, Intent pickIntent) {
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(pickIntent, 0);
        for(ResolveInfo resolveInfo: resInfo){
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetIntent = new Intent(pickIntent);
            targetIntent.setPackage(packageName);
            intentList.add(targetIntent);
        }
        return intentList;
    }

    private File getFile() {
        File photoFile = null;
        try{
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String imageFileName = "JPEG_"+ timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);

            photoPath = photoFile.getAbsolutePath();

        }catch (IOException ex){
            Snackbar.make(vpContent, R.string.main_error_dispatch_camera, Snackbar.LENGTH_SHORT).show();
        }
        return  photoFile;
    }

    @Override
    protected void onDestroy() {
        mainPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onUploadInit() {
        Snackbar.make(vpContent, R.string.main_notice_upload_init, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadComplete() {
        Snackbar.make(vpContent, R.string.main_notice_upload_complete, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onUploadError(String error) {
        Snackbar.make(vpContent, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);

        }else{
            getLastKnowLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Snackbar.make(vpContent, connectionResult.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mainPresenter.logout();
        sharedPreferences.edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_READ_MEDIA:
                if((grantResults.length > 0 ) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) )
                    takePhoto();
                break;

            case PERMISSIONS_REQUEST_LOCATION:
                if((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    getLastKnowLocation();
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }


    }

    private void getLastKnowLocation() {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }
}
