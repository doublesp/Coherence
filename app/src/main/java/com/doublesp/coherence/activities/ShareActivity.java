package com.doublesp.coherence.activities;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.ActivityShareBinding;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.utils.ToolbarBindingUtils;
import com.doublesp.coherence.viewmodels.User;
import com.doublesp.coherence.viewmodels.UserList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ShareActivity extends AppCompatActivity {
    ActivityShareBinding binding;
    public static FragmentManager fm;
    private static DatabaseReference mUsersListsDatabaseReference;
    private static DatabaseReference mSharedWithListsDatabaseReference;
    private static DatabaseReference mNotifySharedDatabaseReference;
    private static String mListId;
    private static FirebaseRecyclerAdapter<User, FriendsViewHolder> mFirebaseRecyclerAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListsDatabaseReference;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ShareActivity.this, R.layout.activity_share);
        ToolbarBindingUtils.bind(ShareActivity.this, binding.activityShareToolbarContainer.toolbar);

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
        mRecyclerView.addItemDecoration(dividerItemDecoration);

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

                TextDrawable.IBuilder initialConfig = TextDrawable.builder()
                        .beginConfig()
                        .endConfig()
                        .roundRect(10);
                TextDrawable initial = initialConfig.build(String.valueOf(name.charAt(0)), color);

                holder.mInitials.setImageDrawable(initial);
                holder.mEmail.setText(emailDecoded);
                holder.mName.setText(user.getName());

                mSharedWithListsDatabaseReference.child(email).addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final User userShared = dataSnapshot.getValue(
                                        User.class);

                                if (userShared != null) {
                                    holder.mAddFriendButton.setImageResource(R.drawable.ic_check);
                                    holder.mAddFriendButton.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    HashMap<String, Object> updatedUserData =
                                                            updateFriendInSharedWith(false,
                                                                    user);
                                                    mFirebaseDatabase.getReference().updateChildren(
                                                            updatedUserData);
                                                    mUsersListsDatabaseReference.child(
                                                            user.getEmail()).child(
                                                            mListId).removeValue();
                                                    mNotifySharedDatabaseReference.child(
                                                            user.getEmail()).removeValue();
                                                }
                                            });
                                } else {
                                    holder.mAddFriendButton.setImageResource(
                                            R.drawable.ic_add_green);
                                    holder.mAddFriendButton.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    HashMap<String, Object> updatedUserData =
                                                            updateFriendInSharedWith(true,
                                                                    user);
                                                    mFirebaseDatabase.getReference().updateChildren(
                                                            updatedUserData);

                                                    mUsersListsDatabaseReference.child(
                                                            ConstantsAndUtils.getOwner(
                                                                    ShareActivity.this)).child(
                                                            mListId)
                                                            .addListenerForSingleValueEvent(
                                                                    new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(
                                                                                DataSnapshot
                                                                                        dataSnapshot) {
                                                                            UserList userList =
                                                                                    dataSnapshot
                                                                                            .getValue(
                                                                                                    UserList.class);
                                                                            mUsersListsDatabaseReference.child(
                                                                                    user.getEmail
                                                                                            ())
                                                                                    .child(
                                                                                            mListId)
                                                                                    .setValue(
                                                                                            userList);
                                                                            mNotifySharedDatabaseReference.child(
                                                                                    user.getEmail
                                                                                            ())
                                                                                    .setValue(
                                                                                            mListId);
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(
                                                                                DatabaseError
                                                                                        databaseError) {

                                                                        }
                                                                    });
                                                }
                                            });
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

    public void addFriend(View view) {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        ImageView mInitials;
        TextView mEmail;
        TextView mName;
        ImageButton mAddFriendButton;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mInitials = (ImageView) itemView.findViewById(R.id.intials_image);
            mEmail = (TextView) itemView.findViewById(R.id.friend_email_address);
            mName = (TextView) itemView.findViewById(R.id.friend_name);
            mAddFriendButton = (ImageButton) itemView.findViewById(R.id.add_friend_button);
        }
    }
}
