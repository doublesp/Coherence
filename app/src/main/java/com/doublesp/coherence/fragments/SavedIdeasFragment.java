package com.doublesp.coherence.fragments;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.doublesp.coherence.R;
import com.doublesp.coherence.activities.CurrentListDetails;
import com.doublesp.coherence.databinding.FragmentSavedIdeasBinding;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.UserList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.doublesp.coherence.R.id.owner;

public class SavedIdeasFragment extends Fragment {

    FragmentSavedIdeasBinding binding;
    private static FirebaseRecyclerAdapter<UserList, ItemViewHolder> mFirebaseRecyclerAdapter;
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

                    holder.mSinglePlan.setText(title);
                    holder.mOwner.setText(holder.mOwner.getResources().getString(R.string.owner,
                            owner.replace(",", ".")));
                }
            };

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_ideas, container, false);

        binding.rvSavedIdeas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSavedIdeas.setAdapter(mFirebaseRecyclerAdapter);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseRecyclerAdapter.cleanup();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView mSinglePlan;
        TextView mOwner;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mSinglePlan = (TextView) itemView.findViewById(R.id.single_plan);
            mOwner = (TextView) itemView.findViewById(owner);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), CurrentListDetails.class);
                    intent.putExtra(ConstantsAndUtils.LIST_ID,
                            mFirebaseRecyclerAdapter.getRef(getAdapterPosition()).getKey());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
