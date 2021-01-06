package com.example.footballnewsmanager.adapters.news;

import android.app.Activity;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballnewsmanager.R;
import com.example.footballnewsmanager.adapters.news.additionalInfo.teams.AdditionalTeamsAdapter;
import com.example.footballnewsmanager.databinding.AdditionalInfoTeamsBinding;
import com.example.footballnewsmanager.models.UserTeam;

import java.util.List;

public class NewsAdditionalInfoViewModel {


    public ObservableField<String> viewTitle = new ObservableField<>();
    private Activity activity;


    public ObservableField<RecyclerView.LayoutManager> layoutManager = new ObservableField<>();
    public ObservableField<RecyclerView.Adapter> adapterObservable = new ObservableField<>();
    public List<UserTeam> teams;
    private float xDistance, yDistance, lastX, lastY;

    public void initForTeams(List<UserTeam> teams, Activity activity, AdditionalInfoTeamsBinding binding) {
        this.activity = activity;
        this.teams = teams;
        viewTitle.set(activity.getResources().getString(R.string.proposed_teams));
        LinearLayoutManager teamsLayoutManager = new LinearLayoutManager(binding.additionalInfoTeamsRecyclerview.getContext());
        teamsLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.set(teamsLayoutManager);
        binding.additionalInfoTeamsRecyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xDistance = yDistance = 0f;
                        lastX = ev.getX();
                        lastY = ev.getY();
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final float curX = ev.getX();
                        final float curY = ev.getY();
                        xDistance += Math.abs(curX - lastX);
                        yDistance += Math.abs(curY - lastY);
                        lastX = curX;
                        lastY = curY;
                        if (yDistance > xDistance)
                            rv.getParent().requestDisallowInterceptTouchEvent(false);

                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        AdditionalTeamsAdapter additionalTeamsAdapter = new AdditionalTeamsAdapter(activity);
        additionalTeamsAdapter.setItems(teams);
        adapterObservable.set(additionalTeamsAdapter);
    }
}
