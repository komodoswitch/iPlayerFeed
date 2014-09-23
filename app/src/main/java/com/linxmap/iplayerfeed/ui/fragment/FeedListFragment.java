package com.linxmap.iplayerfeed.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.linxmap.iplayerfeed.R;
import com.linxmap.iplayerfeed.common.L;
import com.linxmap.iplayerfeed.feed.manager.FeedManager;
import com.linxmap.iplayerfeed.list.FeedEntry;
import com.linxmap.iplayerfeed.list.FeedListAdapter;
import com.linxmap.iplayerfeed.listener.FeedManagerListener;
import com.linxmap.iplayerfeed.listener.FragmentListener;

import java.util.ArrayList;


public class FeedListFragment extends Fragment implements Button.OnClickListener, FeedManagerListener {

    private final String LOG_TAG = "FeedListFragment";
    private final boolean LOG_ENABLED = true;
    private ArrayList<FeedEntry> feedList = new ArrayList<FeedEntry>();
    private ListView feedListView = null;
    private Button refreshFeedButton = null;
    private FeedListAdapter feedListAdapter = null;
    private FragmentListener fragmentListener = null;
    private FeedManager feedManager = null;
    private ProgressBar feedLoadProgressBar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.feed_list_fragment_layout, container, false);
        rootView.setSaveEnabled(false);
        feedListView = (ListView) rootView.findViewById(R.id.feed_list_view);
        refreshFeedButton = (Button) rootView.findViewById(R.id.refresh_feed_button);
        feedLoadProgressBar = (ProgressBar) rootView.findViewById(R.id.feed_load_progress_bar);
        refreshFeedButton.setOnClickListener(this);
        feedListAdapter = new FeedListAdapter(this.getActivity(), feedList);
        feedListView.setAdapter(feedListAdapter);
        if (feedList.isEmpty())
            feedManager.loadFeed();
        if (feedManager.isLoadFeedRunning()) {
            this.startFeedLoadProgress();
        }
        return rootView;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        feedManager = new FeedManager(this.getActivity());
        feedManager.setFeedManagerListener(this);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentListener = (FragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == refreshFeedButton.getId())
            feedManager.loadFeed();
    }

    private void updateFeedList(ArrayList<FeedEntry> list) {
        if (feedList != null) {
            feedList.clear();
            feedList.addAll(list);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void startFeedLoadProgress() {
        if (this.feedLoadProgressBar != null) {
            feedLoadProgressBar.setVisibility(View.VISIBLE);
            feedLoadProgressBar.setIndeterminate(true);
        }
    }

    private void stopFeedLoadProgress() {
        if (this.feedLoadProgressBar != null) {
            feedLoadProgressBar.setIndeterminate(false);
            feedLoadProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUpdateLatestList(ArrayList<FeedEntry> latestList) {
        updateFeedList(latestList);
    }

    @Override
    public void onDownloadTaskCompleted() {
        stopFeedLoadProgress();
        if (feedListAdapter != null)
            feedListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNetworkStatusNotAvailable() {
        if (fragmentListener != null)
            fragmentListener.onLaunchNoNetworkConnectionDialog();
    }

    @Override
    public void onDownloadTaskStarted() {
        startFeedLoadProgress();
        L.log(LOG_TAG, LOG_ENABLED, "ON DOWNLOAD TASK COMPLETED");
    }


}
