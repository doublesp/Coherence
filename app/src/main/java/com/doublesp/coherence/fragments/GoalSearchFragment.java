package com.doublesp.coherence.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentGoalSearchBinding;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.utils.ImageUtils;

import javax.inject.Inject;
import javax.inject.Named;

public class GoalSearchFragment extends Fragment {

    FragmentGoalSearchBinding binding;
    @Inject
    GoalActionHandlerInterface mActionHandler;
    @Inject
    @Named("Goal")
    RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
    @Inject
    GoalInteractorInterface mGoalInteractor;

    public GoalSearchFragment() {
        // Required empty public constructor
    }

    public static GoalSearchFragment newInstance() {
        GoalSearchFragment fragment = new GoalSearchFragment();
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
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_goal_search, container, false);

        binding.rvIdeaSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIdeaSearchResults.setAdapter(mAdapter);
        binding.rvIdeaSearchResults.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int scrollY = binding.rvIdeaSearchResults.computeVerticalScrollOffset();
                float max = 0;
                float translationY = Math.min(max, -scrollY);
                binding.flGoalSearchBackground.setTranslationY(translationY);
            }
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.line_divider));
        binding.rvIdeaSearchResults.addItemDecoration(dividerItemDecoration);
        binding.etIdeaSearchBox.addTextChangedListener(new TextWatcher() {
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
        ImageUtils.loadDefaultImageRotation(binding.ivIdeaSearchBackground);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int padding = ImageUtils.topImagePadding(getActivity().getWindowManager(), getResources());
        padding -= (getResources().getDimension(R.dimen.item_goal_height) / 4);
        binding.rvIdeaSearchResults.setPadding(0, padding, 0, 0);
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
        mGoalInteractor.clearGoal();
    }

}
