package ghostl.com.photofeed.login;

public interface LoginPresenter {
    void onCreate();
    void onDestroy();

    void login(String email, String password);

    void registerNewUser(String email, String password);
}
