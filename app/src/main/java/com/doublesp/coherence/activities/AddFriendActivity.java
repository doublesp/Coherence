package com.doublesp.coherence.activities;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.doublesp.coherence.R;
import com.doublesp.coherence.databinding.ActivityAddFriendBinding;
import com.doublesp.coherence.databinding.SingleAddFriendBinding;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.utils.ImageUtils;
import com.doublesp.coherence.utils.ToolbarBindingUtils;
import com.doublesp.coherence.viewmodels.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AddFriendActivity extends AppCompatActivity {

    private static DatabaseReference mListsDatabaseReference;
    private static FirebaseRecyclerAdapter<User, AddFriendViewHolder> mFirebaseRecyclerAdapter;
    private static RelativeLayout sRelativeLayout;
    private static ArrayList<String> userFriends;
    ActivityAddFriendBinding binding;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                AddFriendActivity.this, R.layout.activity_add_friend);
        ToolbarBindingUtils.bind(
                AddFriendActivity.this, binding.activityAddFriendToolbarContainer.toolbar);
        setTitle(R.string.add_friend);

        sRelativeLayout = (RelativeLayout) findViewById(R.id.activity_add_friend);
        mRecyclerView = (RecyclerView) findViewById(R.id.add_friends_recycler_view);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(ConstantsAndUtils.USERS);
        mListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_FRIENDS);

        userFriends = new ArrayList<>();
        userFriends.add(ConstantsAndUtils.getOwner(this));

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(
                ContextCompat.getDrawable(this, R.drawable.line_divider_edge_to_edge));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, AddFriendViewHolder>(
                User.class, R.layout.single_add_friend, AddFriendViewHolder.class,
                mUsersDatabaseReference) {

            @Override
            protected void populateViewHolder(final AddFriendViewHolder holder, User user,
                    int position) {
                String name = user.getName();
                String email = user.getEmail();
                String emailDecoded = email.replace(",", ".");
                if (name == null || name.isEmpty()) name = emailDecoded;

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

                if (email.equals(ConstantsAndUtils.getOwner(AddFriendActivity.this))) {
                    return;
                }

                mListsDatabaseReference.child(ConstantsAndUtils.getOwner(AddFriendActivity.this))
                        .child(email).addListenerForSingleValueEvent(

                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User thisUser = dataSnapshot.getValue(User.class);
                                if (thisUser != null) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseRecyclerAdapter.cleanup();
    }

    public static class AddFriendViewHolder extends RecyclerView.ViewHolder {
        SingleAddFriendBinding binding;
        TextView mEmail;
        TextView mName;
        ImageButton mAddFriendButton;

        public AddFriendViewHolder(View itemView) {
            super(itemView);
            binding = SingleAddFriendBinding.bind(itemView);

            // TODO: use data bind
            mEmail = (TextView) itemView.findViewById(R.id.friend_email_address);
            mName = (TextView) itemView.findViewById(R.id.friend_name);
            mAddFriendButton = (ImageButton) itemView.findViewById(R.id.add_friend_button);

            mAddFriendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = mFirebaseRecyclerAdapter.getItem(getAdapterPosition());
                    if (userFriends.contains(user.getEmail())) {
                        if (user.getEmail().equals(
                                ConstantsAndUtils.getOwner(mEmail.getContext()))) {
                            Snackbar.make(sRelativeLayout, R.string.cannot_remove_self,
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        mListsDatabaseReference.child(ConstantsAndUtils.getOwner(view.getContext()))
                                .child(user.getEmail()).removeValue();
                        userFriends.remove(user.getEmail());
                        mAddFriendButton.setSelected(false);
                    } else {
                        userFriends.add(user.getEmail());
                        mListsDatabaseReference.child(ConstantsAndUtils.getOwner(view.getContext()))
                                .child(user.getEmail()).setValue(user);
                        mAddFriendButton.setSelected(true);
                    }
                }
            });
        }
    }
}
