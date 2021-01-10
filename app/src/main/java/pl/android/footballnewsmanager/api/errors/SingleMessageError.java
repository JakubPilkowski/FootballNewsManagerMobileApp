package pl.android.footballnewsmanager.api.errors;

public class SingleMessageError extends BaseError{

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
