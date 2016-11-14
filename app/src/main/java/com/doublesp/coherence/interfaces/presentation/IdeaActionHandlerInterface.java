package com.doublesp.coherence.interfaces.presentation;

import android.text.Editable;

/**
 * Created by pinyaoting on 11/10/16.
 */

public interface IdeaActionHandlerInterface {

    void afterTextChanged(Editable s);
    void onSuggestionClick(int pos);

}
