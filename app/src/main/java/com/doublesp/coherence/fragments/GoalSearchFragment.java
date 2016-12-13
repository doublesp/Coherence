package com.doublesp.coherence.fragments;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentGoalSearchBinding;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.interfaces.presentation.ListCompositionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.ViewState;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.utils.ImageUtils;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observer;

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
        if (getActivity() instanceof ListCompositionHandlerInterface) {
            binding.setHandler((ListCompositionHandlerInterface) getActivity());
        }
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
        if (ConstantsAndUtils.getAndroidSDKVersion() >= ConstantsAndUtils.LATEST) {
            // NOTE: disabled image rotation on low end device for better performance
            ImageUtils.loadDefaultImageRotation(binding.ivIdeaSearchBackground);
        } else {
            binding.ivIdeaSearchBackground.setImageResource(R.drawable.background_0);
        }

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (mGoalInteractor.getDisplayGoalFlag()) {
                    case R.id.flag_explore_recipes:
                        mGoalInteractor.search(null);
                        break;
                    default:
                        binding.swipeRefreshLayout.setRefreshing(false);
                        break;
                }
            }
        });
        // Configure the refreshing colors
        binding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mGoalInteractor.subscribeToGoalStateChange(new Observer<ViewState>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ViewState viewState) {
                if (viewState.getState() == R.id.state_loaded) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int padding = ImageUtils.topImagePadding(getActivity().getWindowManager(), getResources());
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
