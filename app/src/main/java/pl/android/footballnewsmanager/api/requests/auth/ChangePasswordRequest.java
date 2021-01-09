package pl.android.footballnewsmanager.api.requests.auth;

public class ChangePasswordRequest {

    private String oldCredential;
    private String newCredential;

    public ChangePasswordRequest(String oldCredential, String newCredential) {
        this.oldCredential = oldCredential;
        this.newCredential = newCredential;
    }
}
