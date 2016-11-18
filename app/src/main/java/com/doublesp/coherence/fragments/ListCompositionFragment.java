package com.doublesp.coherence.fragments;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentListCompositionBinding;
import com.doublesp.coherence.interfaces.presentation.HomeInjectorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

public class ListCompositionFragment extends Fragment {

    FragmentListCompositionBinding binding;
    @Inject
    @Named("Composition")
    RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    @Inject
    ListFragmentActionHandlerInterface mActionHandler;

    public ListCompositionFragment() {
        // Required empty public constructor
    }

    public static ListCompositionFragment newInstance() {
        ListCompositionFragment fragment = new ListCompositionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_composition, container, false);
        binding.rvIdeas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIdeas.setAdapter(mAdapter);
        binding.setHandler(mActionHandler);
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeInjectorInterface) {
            HomeInjectorInterface injector = (HomeInjectorInterface) context;
            injector.inject(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
