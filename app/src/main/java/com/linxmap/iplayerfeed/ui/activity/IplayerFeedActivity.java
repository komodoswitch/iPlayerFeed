package com.linxmap.iplayerfeed.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.linxmap.iplayerfeed.R;
import com.linxmap.iplayerfeed.dialog.DialogManager;
import com.linxmap.iplayerfeed.listener.FragmentListener;
import com.linxmap.iplayerfeed.ui.fragment.FeedListFragment;


public class IplayerFeedActivity extends FragmentActivity implements FragmentListener {

    private static final String LOG_TAG = "ItunezFeedActivity";
    private static final boolean LOG_ENABLED = true;
    private final String NETWORK_STATUS_DIALOG_STATE = "NETWORK_STATUS_DIALOG_STATE";
    private DialogManager dialogManager;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();;
        if(dialogManager != null)
            dialogManager.hideNetworkConnectionDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iplayer_feed_activity_layout);
        dialogManager = new DialogManager(this);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                restoreStateFromBundle(savedInstanceState);
                return;
            }
            FeedListFragment feedListFragment = new FeedListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, feedListFragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateToBundle(outState);
    }

    private void saveStateToBundle(Bundle outState)
    {
        outState.putBoolean(NETWORK_STATUS_DIALOG_STATE,this.dialogManager.isNetworkStatusDialogShowing());
    }

    private void restoreStateFromBundle(Bundle bundle)
    {
        boolean wasNetworkDialogShowing = bundle.getBoolean(NETWORK_STATUS_DIALOG_STATE);
        if(dialogManager != null)
        {
            if (wasNetworkDialogShowing)
                dialogManager.launchNoNetworkConnectionDialog();
        }
    }

    @Override
    public void onLaunchNoNetworkConnectionDialog()
    {
        if (dialogManager != null)
            dialogManager.launchNoNetworkConnectionDialog();
    }

}

