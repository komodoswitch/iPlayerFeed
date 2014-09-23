package com.linxmap.iplayerfeed.feed.manager;

import android.content.Context;

import com.linxmap.iplayerfeed.feed.RatingRunnable;
import com.linxmap.iplayerfeed.feed.http.HttpConnectionManager;
import com.linxmap.iplayerfeed.feed.http.HttpConnectionResponse;
import com.linxmap.iplayerfeed.feed.http.HttpRequestParams;
import com.linxmap.iplayerfeed.feed.parser.JsonParser;
import com.linxmap.iplayerfeed.feed.parser.XmlParser;
import com.linxmap.iplayerfeed.feed.task.DownloadFeedTask;
import com.linxmap.iplayerfeed.list.FeedEntry;
import com.linxmap.iplayerfeed.listener.FeedManagerListener;
import com.linxmap.iplayerfeed.network.NetworkStatusManager;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by borg on 22/09/14.
 */
public class FeedManager  {

    private final String LOG_TAG = "FeedManager";
    private static final boolean LOG_ENABLED = true;
    private Context context;
    private FeedManagerListener feedManagerListener;
    private final String BBC_FEED_URL = "http://feeds.bbc.co.uk/iplayer/films/tv/list/";
    private final String ROTTEN_TOMATOES_URL = "http://api.rottentomatoes.com/api/public/v1.0/movies.json?apikey=nnnyxe6sqtdnvcszmn9y8pnm&q=";
    private HttpConnectionManager httpConnectionManager;
    private JsonParser jsonParser;
    private XmlParser xmlParser;
    private ArrayList<FeedEntry> feedList = new ArrayList<FeedEntry>();
    private boolean isLoadFeedRunning = false;

    public FeedManager(Context context) {
        this.context = context;
        httpConnectionManager = new HttpConnectionManager();
        jsonParser = new JsonParser();
        xmlParser = new XmlParser();
    }

    public void setFeedManagerListener(FeedManagerListener feedManagerListener) {
        this.feedManagerListener = feedManagerListener;
    }

    public void loadFeed() {
        if (NetworkStatusManager.getInstance(context).isConnectionAvailable()) {
            try {
                setLoadFeedRunning(true);
                if (feedManagerListener != null)
                    feedManagerListener.onDownloadTaskStarted();
                DownloadFeedTask downloadFeedTask = new DownloadFeedTask();
                downloadFeedTask.execute(this);
            } catch (RejectedExecutionException e) {
                e.printStackTrace();
            }
        } else {
            if (feedManagerListener != null)
                feedManagerListener.onNetworkStatusNotAvailable();
        }

    }

    public void onUpdateLatestList(ArrayList<FeedEntry> latestList) {
        if (feedManagerListener != null)
            feedManagerListener.onUpdateLatestList(latestList);
    }

    public void onDownloadTaskCompleted() {
        setLoadFeedRunning(false);
        if (feedManagerListener != null)
            feedManagerListener.onDownloadTaskCompleted();
    }

    public int getRating(String title) throws UnsupportedEncodingException {

        int rating = -1;
        String url = ROTTEN_TOMATOES_URL + URLEncoder.encode(title.toLowerCase()).toString();

        HttpConnectionResponse httpConnectionResponse = httpConnectionManager.getHttpConnectionResponse(new HttpRequestParams(url, HttpConnectionManager.HttpMethod.HTTP_METHOD_GET, null, null, null));
         if (httpConnectionResponse.getHttpConnectionResponseResult() == HttpConnectionManager.HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_OK)
            rating = jsonParser.getAudienceRating(httpConnectionResponse.getHttpResponseBody());
         return rating;
    }

    public ArrayList<FeedEntry> getLatestFeedList() throws XmlPullParserException, IOException {

        ArrayList<FeedEntry> tempList = new ArrayList<FeedEntry>();

        HttpConnectionResponse httpConnectionResponse = httpConnectionManager.getHttpConnectionResponse(new HttpRequestParams(BBC_FEED_URL, HttpConnectionManager.HttpMethod.HTTP_METHOD_GET, null, null, null));
        if (httpConnectionResponse.getHttpConnectionResponseResult() == HttpConnectionManager.HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_OK)
            tempList = xmlParser.parse(httpConnectionResponse.getHttpResponseBody());

        feedList.clear();
        feedList.addAll(tempList);

        for (FeedEntry feedEntry : tempList) {
            Thread thread = new Thread(new RatingRunnable(feedEntry, this));
            try {
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(feedList, new Comparator<FeedEntry>() {
            @Override
            public int compare(FeedEntry feedEntry1, FeedEntry feedEntry2) {
                return feedEntry2.getRating() - feedEntry1.getRating();
            }
        });


        return feedList;
    }

    public boolean isLoadFeedRunning() {
        return isLoadFeedRunning;
    }

    public void setLoadFeedRunning(boolean isLoadFeedRunning) {
        this.isLoadFeedRunning = isLoadFeedRunning;
    }

    public synchronized void updateRatingInFeedList(FeedEntry feedEntry) {
        int index = feedList.indexOf(feedEntry);
        if (feedList.contains(feedEntry) && index > -1) {
            feedList.set(index, feedEntry);
        }
    }
}
