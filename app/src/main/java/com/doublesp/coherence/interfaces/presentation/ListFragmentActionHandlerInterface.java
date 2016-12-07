package com.doublesp.coherence.interfaces.presentation;

public interface ListFragmentActionHandlerInterface {

    void onShareButtonClick();

    void onSearchButtonClick();

    void onNearbyStoreButtonClick();

    void onCrossoutButtonClick(int pos);

    void onRemoveButtonClick(int pos);

}
