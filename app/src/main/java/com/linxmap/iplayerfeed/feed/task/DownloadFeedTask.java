package com.linxmap.iplayerfeed.feed.task;

import android.os.AsyncTask;

import com.linxmap.iplayerfeed.common.L;
import com.linxmap.iplayerfeed.feed.manager.FeedManager;
import com.linxmap.iplayerfeed.list.FeedEntry;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class DownloadFeedTask extends AsyncTask<FeedManager, Void, FeedManager> {

    private static final String LOG_TAG = "DownloadFeedTask";
    private static final boolean LOG_ENABLED = true;

    @Override
    protected FeedManager doInBackground(FeedManager... feedManagers) {
        FeedManager feedManager = feedManagers[0];
        synchronized (this) {
            ArrayList<FeedEntry> latestFeedList = null;
            try {
                latestFeedList = feedManager.getLatestFeedList();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (latestFeedList != null)
                feedManager.onUpdateLatestList(latestFeedList);
        }
        return feedManager;
    }

    @Override
    protected void onCancelled() {
        L.log(LOG_TAG, LOG_ENABLED, "ASYNC CANCELLED");
    }

    @Override
    protected void onPostExecute(FeedManager feedManager) {
        super.onPostExecute(feedManager);
        feedManager.onDownloadTaskCompleted();
    }


}
