package com.doublesp.coherence.interfaces.presentation;

import android.text.Editable;

public interface GoalActionHandlerInterface {

    void afterTextChanged(Editable s);

    void onBookmarkClick(int pos);

    void onPreviewButtonClick(int pos);

    interface PreviewHandlerInterface {
        void showPreviewDialog(int pos);
    }

}
