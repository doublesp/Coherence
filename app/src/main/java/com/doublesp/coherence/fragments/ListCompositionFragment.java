package com.doublesp.coherence.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentListCompositionBinding;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.utils.AnimationUtils;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Plan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import javax.inject.Inject;
import javax.inject.Named;

public class ListCompositionFragment extends DialogFragment {

    static final String LIST_COMPOSITION_VIEW_MODELS = "LIST_COMPOSITION_VIEW_MODELS";
    static final String LIST_COMPOSITION_LIST_ID = "LIST_COMPOSITION_LIST_ID";
    static final int LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL = 5000;
    FragmentListCompositionBinding binding;
    int[] mBackgroundImageIds;
    int mBackgroundImageIndex;
    @Inject
    @Named("Composition")
    RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    @Inject
    ListFragmentActionHandlerInterface mActionHandler;
    @Inject
    IdeaInteractorInterface mInteractor;

    public ListCompositionFragment() {
        // Required empty public constructor
    }

    public static ListCompositionFragment newInstance() {
        ListCompositionFragment fragment = new ListCompositionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ListCompositionFragment newInstance(String listId, Goal goal) {
        ListCompositionFragment fragment = new ListCompositionFragment();
        Bundle args = new Bundle();
        if (goal != null) {
            args.putString(LIST_COMPOSITION_LIST_ID, listId);
            args.putParcelable(LIST_COMPOSITION_VIEW_MODELS, Parcels.wrap(goal));
        }
        fragment.setArguments(args);
        return fragment;
    }

    public static ListCompositionFragment newInstance(String listId) {
        ListCompositionFragment fragment = new ListCompositionFragment();
        Bundle args = new Bundle();
        if (listId != null) {
            args.putString(LIST_COMPOSITION_LIST_ID, listId);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            final Goal goal = Parcels.unwrap(getArguments().getParcelable(LIST_COMPOSITION_VIEW_MODELS));
            String listId = getArguments().getString(LIST_COMPOSITION_LIST_ID);
            if (listId != null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference listsDatabaseReference = firebaseDatabase.getReference().child(
                        ConstantsAndUtils.SHOPPING_LISTS).child(listId);
                listsDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Plan plan = dataSnapshot.getValue(Plan.class);
                        if (plan != null) {
                            mInteractor.setPlan(plan);
                        }
                        if (goal != null) {
                            mInteractor.loadIdeasFromGoal(goal);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
        setupBackgroundImageId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_composition, container,
                false);
        binding.rvIdeas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIdeas.setAdapter(mAdapter);
        binding.setHandler(mActionHandler);
        binding.etIdea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mActionHandler.afterTextChanged(editable);
            }
        });
        rotateImage();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InjectorInterface) {
            InjectorInterface injector = (InjectorInterface) context;
            injector.inject(this);
        }
    }

    @Override
    public void onResume() {
        binding.multipleActions.collapse();
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractor.clearIdeas();
    }

    void setupBackgroundImageId() {
        mBackgroundImageIds = new int[5];
        mBackgroundImageIds[0] = R.drawable.background_0;
        mBackgroundImageIds[1] = R.drawable.background_1;
        mBackgroundImageIds[2] = R.drawable.background_2;
        mBackgroundImageIds[3] = R.drawable.background_3;
        mBackgroundImageIds[4] = R.drawable.background_4;
        mBackgroundImageIndex = 0;
    }

    void rotateImage() {
        binding.ivIdeaCompositionBackground.setImageResource(
                mBackgroundImageIds[mBackgroundImageIndex]);
        binding.ivIdeaCompositionBackground.startAnimation(AnimationUtils.fadeInOutAnimation(
                LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBackgroundImageIndex = (mBackgroundImageIndex + 1) % mBackgroundImageIds.length;
                rotateImage();
            }
        }, LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL);
    }

}
