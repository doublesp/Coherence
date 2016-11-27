package com.doublesp.coherence.datastore;

import com.doublesp.coherence.R;
import com.doublesp.coherence.interfaces.domain.IdeaDataStoreInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;

import org.parceler.Parcels;

import android.content.Context;
import android.os.Parcelable;
import android.util.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

/**
 * Created by pinyaoting on 11/10/16.
 */

public class IdeaDataStore implements IdeaDataStoreInterface {

    IdeaSnapshotStore mIdeaSnapshotStore;
    List<Observer<Integer>> mStateObservers;
    int mIdeaState;
    private Context mContext;

    public IdeaDataStore(Context context) {
        mIdeaSnapshotStore = new IdeaSnapshotStore();
        mStateObservers = new ArrayList<>();
        mIdeaState = R.id.idea_state_idle;
        mContext = context;
    }

    @Override
    public void setIdeaState(int state) {
        mIdeaState = state;
        notifyStateChange();
    }

    @Override
    public void addIdea(Idea idea) {
        getUserIdeas().add(idea);
    }

    @Override
    public void updateIdea(int pos, Idea idea) {
        if (pos == getUserIdeas().size()) {
            getUserIdeas().add(idea);
        } else {
            getUserIdeas().set(pos, idea);
        }
    }

    @Override
    public void removeIdea(int pos) {
        Pair<Integer, List<Idea>> adjustedPair = getAdjustedPositionAndCorrespondingList(pos);
        int adjustedPos = adjustedPair.first;
        List<Idea> targetList = adjustedPair.second;
        targetList.remove(adjustedPos);
    }

    @Override
    public List<Idea> getIdeas() {
        return getUserIdeas();
    }

    @Override
    public List<Idea> getSuggestions() {
        return getSuggestedIdeas();
    }

    @Override
    public void setSuggestions(List<Idea> ideas) {
        getSuggestedIdeas().clear();
        getSuggestedIdeas().addAll(ideas);
    }

    @Override
    public int getIdeaCount() {
        return getUserIdeas().size() + getSuggestedIdeas().size();
    }

    @Override
    public int getUserIdeaCount() {
        return getUserIdeas().size();
    }

    @Override
    public void subscribeToIdeaStateChanges(Observer<Integer> observer) {
        mStateObservers.add(observer);
    }

    @Override
    public Idea getIdeaAtPos(int pos) {
        Pair<Integer, List<Idea>> adjustedPair = getAdjustedPositionAndCorrespondingList(pos);
        int adjustedPos = adjustedPair.first;
        List<Idea> targetList = adjustedPair.second;
        return targetList.get(adjustedPos);
    }

    @Override
    public Parcelable getSnapshot() {
        return Parcels.wrap(mIdeaSnapshotStore);
    }

    @Override
    public void setSnapshot(Parcelable ideaSnapshot) {
        mIdeaSnapshotStore = Parcels.unwrap(ideaSnapshot);
    }

    @Override
    public Plan getPlan() {
        List<Idea> ideas = getUserIdeas();
        // TODO: allow user to name the plan
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm:ss");
        StringBuilder titleBuilder =  new StringBuilder(mContext.getString(R.string.default_idea_prefix));
        titleBuilder.append(" ");
        titleBuilder.append(formatter.format(calendar.getTime()));
        return new Plan(ideas, titleBuilder.toString(), ConstantsAndUtils.getOwner(mContext));
    }

    private Pair<Integer, List<Idea>> getAdjustedPositionAndCorrespondingList(int pos) {
        if (pos < getUserIdeas().size()) {
            return new Pair<>(pos, getUserIdeas());
        }
        pos -= getUserIdeas().size();
        if (pos < getSuggestedIdeas().size()) {
            return new Pair<>(pos, getSuggestedIdeas());
        }
        return null;
    }

    private void notifyStateChange() {
        ConnectableObservable<Integer> connectedObservable = Observable.just(mIdeaState).publish();
        for (Observer<Integer> observer : mStateObservers) {
            connectedObservable.subscribeOn(Schedulers.immediate())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
        connectedObservable.connect();
    }

    private List<Idea> getUserIdeas() {
        return mIdeaSnapshotStore.mIdeas;
    }

    private List<Idea> getSuggestedIdeas() {
        return mIdeaSnapshotStore.mIdeaSuggestions;
    }
}
