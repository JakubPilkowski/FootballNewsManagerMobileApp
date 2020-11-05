package com.example.footballnewsmanager.api.responses.proposed;

import com.example.footballnewsmanager.api.responses.BaseResponse;
import com.example.footballnewsmanager.models.FavouriteTeam;
import com.example.footballnewsmanager.models.Language;
import com.example.footballnewsmanager.models.Site;
import com.example.footballnewsmanager.models.Team;
import com.example.footballnewsmanager.models.User;
import com.example.footballnewsmanager.models.UserSite;

import java.util.ArrayList;
import java.util.List;

public class ProposedUserResponse extends BaseResponse {

    private User user;

    public User getUser() {
        return user;
    }
}
