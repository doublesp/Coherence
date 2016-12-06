package com.doublesp.coherence.fragments;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.FragmentSavedIdeasBinding;
import com.doublesp.coherence.databinding.SinglePlanBinding;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.interfaces.presentation.ListCompositionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.SavedIdeasActionHandlerInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.utils.ImageUtils;
import com.doublesp.coherence.viewmodels.Idea;
import com.doublesp.coherence.viewmodels.Plan;
import com.doublesp.coherence.viewmodels.UserList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import jp.wasabeef.blurry.Blurry;

public class SavedIdeasFragment extends Fragment {

    static final int SAVED_IDEAS_IMAGE_ROTATION_INTERVAL = 3000;
    public static SavedIdeasActionHandlerInterface mActionHandlerRef;
    public static Set<Handler> mHandlers;
    private static FirebaseRecyclerAdapter<UserList, ItemViewHolder> mFirebaseRecyclerAdapter;
    @Inject
    public SavedIdeasActionHandlerInterface mActionHandler;
    FragmentSavedIdeasBinding binding;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListsDatabaseReference;

    public SavedIdeasFragment() {
        // Required empty public constructor
    }

    public static SavedIdeasFragment newInstance() {
        SavedIdeasFragment fragment = new SavedIdeasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mFirebaseDatabase = FirebaseDatabase.getInstance();

            mListsDatabaseReference = mFirebaseDatabase.getReference().child(
                    ConstantsAndUtils.USER_LISTS).child(ConstantsAndUtils.getOwner(getContext()));

            mHandlers = new HashSet<>();

            mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserList, ItemViewHolder>(
                    UserList.class, R.layout.single_plan, ItemViewHolder.class,
                    mListsDatabaseReference) {
                @Override
                protected void populateViewHolder(ItemViewHolder holder, UserList userList,
                        int position) {
                    String title = userList.getlistName().isEmpty() ? ConstantsAndUtils.ANONYMOUS
                            : userList.getlistName();
                    String owner = userList.getOwner().isEmpty() ? ConstantsAndUtils.ANONYMOUS
                            : userList.getOwner();

                    int colorResource = ImageUtils.getColorForPosition(position);
                    int color = ContextCompat.getColor(getContext(), colorResource);

                    FrameLayout.LayoutParams paramsForImage = (FrameLayout.LayoutParams)
                            holder.binding.ivPlanImage.getLayoutParams();
                    FrameLayout.LayoutParams paramsForBox = (FrameLayout.LayoutParams)
                            holder.binding.llDescriptionBox.getLayoutParams();
                    if (position % 2 == 0) {
                        paramsForImage.gravity = Gravity.LEFT | Gravity.START;
                        paramsForBox.leftMargin = paramsForImage.width;
                        paramsForBox.setMarginStart(paramsForImage.width);
                        paramsForBox.rightMargin = 0;
                        paramsForBox.setMarginEnd(0);
                    } else {
                        paramsForImage.gravity = Gravity.RIGHT | Gravity.END;
                        paramsForBox.leftMargin = 0;
                        paramsForBox.setMarginStart(0);
                        paramsForBox.rightMargin = paramsForImage.width;
                        paramsForBox.setMarginEnd(paramsForImage.width);
                    }

                    holder.binding.setPos(position);
                    holder.binding.singlePlan.setText(title);
                    holder.binding.owner.setText(owner.replace(',', '.'));
                    holder.binding.llSinglePlan.setBackgroundColor(color);

                    String listId = mFirebaseRecyclerAdapter.getRef(position).getKey();
                    asyncLoadImage(holder.binding.ivPlanImage, holder.binding.llSinglePlan, listId);
                }
            };

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_ideas, container,
                false);
        if (getActivity() instanceof ListCompositionHandlerInterface) {
            binding.setHandler((ListCompositionHandlerInterface) getActivity());
        }
        binding.rvSavedIdeas.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, true));
        binding.rvSavedIdeas.setAdapter(mFirebaseRecyclerAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.line_divider_edge_to_edge));
        binding.rvSavedIdeas.addItemDecoration(dividerItemDecoration);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.ivSavedIdeasBackground.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        binding.ivSavedIdeasBackground.layout(
                0, 0, binding.ivSavedIdeasBackground.getMeasuredWidth(),
                binding.ivSavedIdeasBackground.getMeasuredHeight());

        Blurry.with(getContext())
                .capture(binding.ivSavedIdeasBackground)
                .into(binding.ivSavedIdeasBackground);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InjectorInterface) {
            InjectorInterface injector = (InjectorInterface) context;
            injector.inject(this);
            mActionHandlerRef = mActionHandler;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseRecyclerAdapter.cleanup();
        for (Handler handler : mHandlers) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void asyncLoadImage(
            final ImageView imageView, final ViewGroup container, String listId) {
        DatabaseReference listsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.SHOPPING_LISTS).child(listId);
        listsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Plan plan = dataSnapshot.getValue(Plan.class);
                if (plan == null) {
                    return;
                }
                String imageUrl = null;
                for (Idea idea : plan.getIdeas()) {
                    if (idea == null || idea.getMeta() == null) {
                        continue;
                    }
                    imageUrl = idea.getMeta().getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        break;
                    }
                }
                if (imageUrl == null || imageUrl.isEmpty()) {
                    return;
                }
                // TODO: [Monetization] replace image with ads
                ImageUtils.loadImageWithProminentColor(imageView, container, imageUrl);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void asyncRotateImage(final Handler handler, final ImageView imageView, String listId) {
        handler.removeCallbacksAndMessages(null);
        DatabaseReference listsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.SHOPPING_LISTS).child(listId);
        listsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Plan plan = dataSnapshot.getValue(Plan.class);
                List<String> imageUrls = new ArrayList<>();
                for (Idea idea : plan.getIdeas()) {
                    if (idea == null || idea.getMeta() == null) continue;
                    imageUrls.add(idea.getMeta().getImageUrl());
                }

                if (!imageUrls.isEmpty()) {
                    ImageUtils.rotateImage(
                            handler, imageView, imageUrls, 0, SAVED_IDEAS_IMAGE_ROTATION_INTERVAL);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handler.removeCallbacksAndMessages(null);
            }
        });
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public SinglePlanBinding binding;
        public Handler mHandler;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = SinglePlanBinding.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: inject action handler into FirebaseRecyclerAdapter
                    // instead of using static reference
                    SavedIdeasFragment.mActionHandlerRef.compose(
                            mFirebaseRecyclerAdapter
                                    .getRef(getAdapterPosition())
                                    .getKey());
                }
            });
            mHandler = new Handler();
            SavedIdeasFragment.mHandlers.add(mHandler);
        }
    }
}
