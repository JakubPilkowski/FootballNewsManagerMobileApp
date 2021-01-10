package pl.android.footballnewsmanager.api.errors;

import java.util.HashMap;

public class MultipleMessageError extends BaseError{
    private HashMap<String, String> messages;

    public HashMap<String, String> getMessages() {
        return messages;
    }
}
