package com.example.footballnewsmanager.api.responses.proposed;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.User;

public class ProposedUserResponse extends BaseResponse {

    private User user;

    public User getUser() {
        return user;
    }
}
