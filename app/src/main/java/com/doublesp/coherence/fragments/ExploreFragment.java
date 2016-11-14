package com.doublesp.coherence.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentExploreBinding;
import com.doublesp.coherence.interfaces.presentation.ExploreFragmentActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.ExploreFragmentInjectorInterface;

import javax.inject.Inject;

public class ExploreFragment extends Fragment {

    FragmentExploreBinding binding;
    @Inject
    ExploreFragmentActionHandlerInterface mHandler;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        // TODO: store view model here
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: retrieve view model
        }
        mHandler = new ExploreFragmentActionHandlerInterface() {
            @Override
            public void onCategorySelected(int category) {
                switch (category) {
                    case R.id.idea_category_travel:
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.flExploreContainer, MapFragment.newInstance(), "MapFragment")
                                .addToBackStack("MapFragment")
                                .commit();
                        break;
                    case R.id.idea_category_groceries:

                        break;
                    case R.id.idea_category_movies:

                        break;
                    default:

                        break;
                }

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        binding.setHandler(mHandler);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExploreFragmentInjectorInterface) {
            ExploreFragmentInjectorInterface injector = (ExploreFragmentInjectorInterface) context;
            injector.inject(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
