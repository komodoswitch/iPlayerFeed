package com.linxmap.iplayerfeed.listener;

import com.linxmap.iplayerfeed.list.FeedEntry;

import java.util.ArrayList;

/**
 * Created by borg on 22/09/14.
 */
public interface FeedManagerListener {

    public void onUpdateLatestList(ArrayList<FeedEntry> latestList);
    public void onDownloadTaskCompleted();
    public void onNetworkStatusNotAvailable();
    public void onDownloadTaskStarted();
}
