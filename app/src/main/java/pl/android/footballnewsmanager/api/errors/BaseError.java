package pl.android.footballnewsmanager.api.errors;

public class BaseError {
    private int status;
    private String error;


    public void setStatus(int status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

}
