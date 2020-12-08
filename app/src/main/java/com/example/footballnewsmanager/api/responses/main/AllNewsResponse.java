package com.example.footballnewsmanager.api.responses.main;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.UserNews;

import java.util.List;

public class AllNewsResponse<T extends BaseNewsAdjustment> extends NewsResponse {

    private T additionalContent;

    public T getAdditionalContent() {
        return additionalContent;
    }
}
