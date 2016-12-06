package com.doublesp.coherence.interfaces.presentation;

/**
 * Created by pinyaoting on 11/13/16.
 */

public interface ListFragmentActionHandlerInterface {

    void onShareButtonClick();

    void onSearchButtonClick();

    void onNearbyStoreButtonClick();

    void onCrossoutButtonClick(int pos);

    void onRemoveButtonClick(int pos);

}
