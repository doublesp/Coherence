package com.doublesp.coherence.interfaces.presentation;

import android.text.Editable;

/**
 * Created by pinyaoting on 11/13/16.
 */

public interface ListFragmentActionHandlerInterface {

    void onFloatingAcitonButtonClick();
    void afterTextChanged(Editable s);
    void onSuggestionClick(int pos);

}
