package com.doublesp.coherence.activities;

import static com.doublesp.coherence.R.id.owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doublesp.coherence.R;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.viewmodels.User;
import com.doublesp.coherence.viewmodels.UserList;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 1;
    private static FirebaseRecyclerAdapter<UserList, ItemViewHolder> mFirebaseRecyclerAdapter;
    private String mUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mListsDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mUsersDatabaseReference;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mCustomList;
    private FloatingActionButton mRecipeList;
    private FloatingActionsMenu mFloatingActionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        // TODO: need to define actions for custom list
        mCustomList = (FloatingActionButton) findViewById(R.id.custom_list);
        mRecipeList = (FloatingActionButton) findViewById(R.id.recipe_list);
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        mRecipeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mListsDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_LISTS).child(ConstantsAndUtils.getOwner(this));
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(ConstantsAndUtils.USERS);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

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

        mRecyclerView.setAdapter(mFirebaseRecyclerAdapter);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user);
                    Toast.makeText(MainActivity.this, getString(R.string.welcome, mUsername),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(
                                                    AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(
                                                    AuthUI.GOOGLE_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(
                                                    AuthUI.FACEBOOK_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void onSignedInInitialize(FirebaseUser user) {
        mUsername = user.getDisplayName();
        // Add user to db
        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put(ConstantsAndUtils.TIMESTAMP, ServerValue.TIMESTAMP);
        User currentUser = new User(mUsername, user.getEmail().replace(".", ","), timestampJoined);
        mUsersDatabaseReference.child(user.getEmail().replace(".", ",")).setValue(currentUser);

        // add user information to sharedPref
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantsAndUtils.EMAIL, user.getEmail().replace(".", ","));
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFloatingActionsMenu.collapse();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        mFirebaseRecyclerAdapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSignedOutCleanup() {
        mUsername = ConstantsAndUtils.ANONYMOUS;
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
