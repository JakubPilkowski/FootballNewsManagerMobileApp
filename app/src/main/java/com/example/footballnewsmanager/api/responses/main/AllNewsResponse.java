package com.example.footballnewsmanager.api.responses.main;

public class AllNewsResponse extends NewsResponse {

    private BaseNewsAdjustment additionalContent;

    public BaseNewsAdjustment getAdditionalContent() {
        return additionalContent;
    }
}
