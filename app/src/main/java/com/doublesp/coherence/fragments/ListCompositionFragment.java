package com.doublesp.coherence.fragments;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentListCompositionBinding;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.interfaces.presentation.ListFragmentActionHandlerInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import jp.wasabeef.blurry.Blurry;

public class ListCompositionFragment extends Fragment {

    static final String LIST_COMPOSITION_VIEW_MODELS = "LIST_COMPOSITION_VIEW_MODELS";
    static final String LIST_COMPOSITION_LIST_ID = "LIST_COMPOSITION_LIST_ID";
    static final int LIST_COMPOSITION_BACKGROUND_IMAGE_ROTATION_INTERVAL = 5000;
    public static FragmentListCompositionBinding binding;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_composition, container,
                false);
        binding.rvIdeas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIdeas.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.line_divider_edge_to_edge));
        binding.rvIdeas.addItemDecoration(dividerItemDecoration);
        binding.setHandler(mActionHandler);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ConstantsAndUtils.getAndroidSDKVersion() >= ConstantsAndUtils.LATEST) {
            binding.ivIdeaCompositionBackground.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            binding.ivIdeaCompositionBackground.layout(
                    0, 0, binding.ivIdeaCompositionBackground.getMeasuredWidth(),
                    binding.ivIdeaCompositionBackground.getMeasuredHeight());

            Blurry.with(getContext())
                    .capture(binding.ivIdeaCompositionBackground)
                    .into(binding.ivIdeaCompositionBackground);
        }
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
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractor.discardPlanIfEmpty();
        mInteractor.clearPlan();
    }

}
