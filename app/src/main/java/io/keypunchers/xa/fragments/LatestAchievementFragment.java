package io.keypunchers.xa.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.keypunchers.xa.R;
import io.keypunchers.xa.adapters.GenericAdapter;
import io.keypunchers.xa.loaders.LatestAchievementsLoader;
import io.keypunchers.xa.models.LatestAchievement;

public class LatestAchievementFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<LatestAchievement>> {
    private ArrayList<LatestAchievement> mData = new ArrayList<>();
    private GenericAdapter<String> mAdapter;
    private String BASE_URL;

    public LatestAchievementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_latest_achievement, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        //ListView mLvContent = (ListView) view.findViewById(R.id.v_image_list);
        //mLvContent.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int LOADER_ID = getActivity().getResources().getInteger(R.integer.latest_achievements_loader_id);

        if (getArguments() != null) {
            BASE_URL = getArguments().getString("url");
            String AB_TITLE = getArguments().getString("ab_title");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(AB_TITLE);
        }

        if (mData.isEmpty()) {
            getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<ArrayList<LatestAchievement>> onCreateLoader(int id, Bundle args) {
        return new LatestAchievementsLoader(getActivity(), BASE_URL, mData);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<LatestAchievement>> loader, ArrayList<LatestAchievement> data) {

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<LatestAchievement>> loader) {

    }
}
