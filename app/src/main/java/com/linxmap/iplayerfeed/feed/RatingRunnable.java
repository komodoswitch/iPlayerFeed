package com.linxmap.iplayerfeed.feed;

import com.linxmap.iplayerfeed.feed.manager.FeedManager;
import com.linxmap.iplayerfeed.list.FeedEntry;

import java.io.UnsupportedEncodingException;

/**
 * Created by borg on 23/09/14.
 */
public class RatingRunnable implements  Runnable{

    private static final String LOG_TAG = "RatingRunnable";
    private static final boolean LOG_ENABLED = true;
    private FeedManager feedManager;
    private FeedEntry feedEntry ;

    public RatingRunnable (FeedEntry feedEntry,FeedManager feedManager )
    {
        this.feedManager = feedManager ;
        this.feedEntry = feedEntry;
    }

    @Override
    public void run() {
        try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            int rating = feedManager.getRating(feedEntry.getTitle());
            feedManager.updateRatingInFeedList(new FeedEntry(feedEntry.getTitle(), feedEntry.getImageUrl(), rating));
            //L.log(LOG_TAG,LOG_ENABLED,"THREAD COMPLETED :"+Thread.currentThread().getName());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



    }
}
