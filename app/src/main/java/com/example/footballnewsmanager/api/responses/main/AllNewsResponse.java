package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.UserNews;

import java.util.List;

public class AllNewsResponse extends NewsResponse {

    private BaseNewsAdjustment additionalContent;

    public BaseNewsAdjustment getAdditionalContent() {
        return additionalContent;
    }
}
