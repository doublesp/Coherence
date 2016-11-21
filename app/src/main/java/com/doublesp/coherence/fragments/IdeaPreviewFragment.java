package com.doublesp.coherence.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentIdeaPreviewBinding;
import com.doublesp.coherence.viewmodels.Idea;

import org.parceler.Parcels;

public class IdeaPreviewFragment extends DialogFragment {

    static final String IDEA_PREVIEW_FRAGMENT_VIEW_MODEL = "IDEA_PREVIEW_FRAGMENT_VIEW_MODEL";

    FragmentIdeaPreviewBinding binding;
    Idea mIdea;

    public IdeaPreviewFragment() {
        // Required empty public constructor
    }

    public static IdeaPreviewFragment newInstance(Idea idea) {
        IdeaPreviewFragment fragment = new IdeaPreviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(IDEA_PREVIEW_FRAGMENT_VIEW_MODEL, Parcels.wrap(idea));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdea = Parcels.unwrap(getArguments().getParcelable(IDEA_PREVIEW_FRAGMENT_VIEW_MODEL));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_idea_preview, container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(mIdea);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
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
    }

}
