package ghostl.com.photofeed.login;

public interface LoginRepository {
    void signUp(String email, String password);
    void signIn(String email, String password);
}
