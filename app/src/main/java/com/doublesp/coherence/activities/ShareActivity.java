package com.doublesp.coherence.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.ActivityShareBinding;
import com.doublesp.coherence.databinding.SingleAddFriendBinding;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.utils.ImageUtils;
import com.doublesp.coherence.utils.ItemClickSupport;
import com.doublesp.coherence.utils.ToolbarBindingUtils;
import com.doublesp.coherence.viewmodels.User;
import com.doublesp.coherence.viewmodels.UserList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class ShareActivity extends AppCompatActivity {
    public static FragmentManager fm;
    private static DatabaseReference mUsersListsDatabaseReference;
    private static DatabaseReference mSharedWithListsDatabaseReference;
    private static DatabaseReference mNotifySharedDatabaseReference;
    private static String mListId;
    private static FirebaseRecyclerAdapter<User, FriendsViewHolder> mFirebaseRecyclerAdapter;
    ActivityShareBinding binding;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListsDatabaseReference;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ShareActivity.this, R.layout.activity_share);
        ToolbarBindingUtils.bind(ShareActivity.this, binding.activityShareToolbarContainer.toolbar);

        if (ConstantsAndUtils.getAndroidSDKVersion() > ConstantsAndUtils.LATEST) {
            binding.shareBackground.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            binding.shareBackground.layout(
                    0, 0, binding.shareBackground.getMeasuredWidth(),
                    binding.shareBackground.getMeasuredHeight());

            Blurry.with(getContext())
                    .capture(binding.shareBackground)
                    .into(binding.shareBackground);
        }

        setTitle(R.string.share_list);
        fm = getSupportFragmentManager();

        mListId = getIntent().getStringExtra(ConstantsAndUtils.LIST_ID);

        mRecyclerView = binding.friendsListRecyclerView;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_FRIENDS).child(ConstantsAndUtils.getOwner(this));
        mUsersListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_LISTS);
        mSharedWithListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.SHARED_WITH).child(mListId);
        mNotifySharedDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.NOTIFY);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(
                ContextCompat.getDrawable(this, R.drawable.line_divider_edge_to_edge));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        User user = mFirebaseRecyclerAdapter.getItem(position);
                        FriendsViewHolder viewHolder = (FriendsViewHolder) recyclerView
                                .getChildViewHolder(v);
                        boolean isShared = viewHolder.mAddFriendButton.isSelected();
                        if (isShared) {
                            unshare(user);
                        } else {
                            share(user);
                        }
                    }
                });

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, FriendsViewHolder>(
                User.class, R.layout.single_add_friend, FriendsViewHolder.class,
                mListsDatabaseReference) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder holder, final User user,
                                              int position) {
                String name = user.getName();
                String email = user.getEmail();
                String emailDecoded = email.replace(",", ".");

                ColorGenerator generator = ColorGenerator.MATERIAL;
                int color = generator.getColor(emailDecoded);
                int bgColor = ImageUtils.getIlluminatedColor(color);

                TextDrawable.IBuilder initialConfig = TextDrawable.builder()
                        .beginConfig()
                        .endConfig()
                        .round();
                TextDrawable initial = initialConfig.build(String.valueOf(name.charAt(0)), color);

                holder.binding.intialsImage.setImageDrawable(initial);
                holder.mEmail.setText(emailDecoded);
                holder.mName.setText(user.getName());
                holder.binding.llAddFriend.setBackgroundColor(bgColor);

                mSharedWithListsDatabaseReference.child(email).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final User userShared = dataSnapshot.getValue(
                                        User.class);
                                boolean isShared = (userShared != null);

                                if (isShared) {
                                    holder.mAddFriendButton.setSelected(true);
                                } else {
                                    holder.mAddFriendButton.setSelected(false);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        };

        mRecyclerView.setAdapter(mFirebaseRecyclerAdapter);
    }

    private void share(final User user) {
        HashMap<String, Object> updatedUserData = updateFriendInSharedWith(true, user);
        mFirebaseDatabase.getReference().updateChildren(updatedUserData);
        mUsersListsDatabaseReference.child(ConstantsAndUtils.getOwner(ShareActivity.this))
                .child(mListId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserList userList = dataSnapshot.getValue(UserList.class);
                mUsersListsDatabaseReference.child(user.getEmail()).child(mListId)
                        .setValue(userList);
                mNotifySharedDatabaseReference.child(user.getEmail()).setValue(mListId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void unshare(final User user) {
        HashMap<String, Object> updatedUserData = updateFriendInSharedWith(false, user);
        mFirebaseDatabase.getReference().updateChildren(updatedUserData);
        mUsersListsDatabaseReference.child(user.getEmail()).child(mListId).removeValue();
        mNotifySharedDatabaseReference.child(user.getEmail()).removeValue();
    }


    public void addFriend(View view) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ItemClickSupport.removeFrom(mRecyclerView);
        mFirebaseRecyclerAdapter.cleanup();

    }

    private HashMap<String, Object> updateFriendInSharedWith(Boolean addFriend,
                                                             User user) {
        HashMap<String, Object> updatedUserData = new HashMap<>();

        if (addFriend) {
            final Map<String, Object> userMap = user.toMap();

            updatedUserData.put("/" + ConstantsAndUtils.SHARED_WITH + "/" + mListId + "/"
                    + user.getEmail(), userMap);
        } else {
            updatedUserData.put("/" + ConstantsAndUtils.SHARED_WITH + "/" + mListId + "/"
                    + user.getEmail(), null);
        }

        return updatedUserData;
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView mEmail;
        TextView mName;
        ImageButton mAddFriendButton;
        SingleAddFriendBinding binding;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            binding = SingleAddFriendBinding.bind(itemView);

            // TODO: use data bind
            mEmail = (TextView) itemView.findViewById(R.id.friend_email_address);
            mName = (TextView) itemView.findViewById(R.id.friend_name);
            mAddFriendButton = (ImageButton) itemView.findViewById(R.id.add_friend_button);
        }
    }
}
