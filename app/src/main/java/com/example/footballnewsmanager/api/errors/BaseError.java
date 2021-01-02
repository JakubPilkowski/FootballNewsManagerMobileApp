package com.example.footballnewsmanager.api.errors;

public class BaseError {
    private String timestamp;
    private int status;
    private String error;
    private String path;


    public void setStatus(int status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getPath() {
        return path;
    }
}
