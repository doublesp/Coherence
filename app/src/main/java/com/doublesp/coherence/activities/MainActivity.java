package com.doublesp.coherence.activities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import com.crashlytics.android.Crashlytics;
import com.doublesp.coherence.R;
import com.doublesp.coherence.actions.ListFragmentActionHandler;
import com.doublesp.coherence.adapters.HomeFragmentPagerAdapter;
import com.doublesp.coherence.application.CoherenceApplication;
import com.doublesp.coherence.databinding.ActivityMainBinding;
import com.doublesp.coherence.dependencies.components.presentation.MainActivitySubComponent;
import com.doublesp.coherence.dependencies.modules.presentation.MainActivityModule;
import com.doublesp.coherence.fragments.GoalPreviewFragment;
import com.doublesp.coherence.fragments.GoalSearchFragment;
import com.doublesp.coherence.fragments.IdeaReviewFragment;
import com.doublesp.coherence.fragments.ListCompositionFragment;
import com.doublesp.coherence.fragments.SavedGoalsFragment;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.utils.TabUtils;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.User;
import com.firebase.ui.auth.AuthUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class MainActivity extends AppCompatActivity implements InjectorInterface,
        GoalActionHandlerInterface.PreviewHandlerInterface,
        ListFragmentActionHandler.IdeaShareHandlerInterface {

    public static final int RC_SIGN_IN = 1;
    static final String IDEA_PREVIEW_FRAGMENT = "IDEA_PREVIEW_FRAGMENT";
    static final String LIST_COMPOSITION_FRAGMENT = "LIST_COMPOSITION_FRAGMENT";
    static final String IDEA_SEARCH_RESULT_FRAGMENT = "IDEA_SEARCH_RESULT_FRAGMENT";
    ActivityMainBinding binding;
    MainActivitySubComponent mActivityComponent;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mUsersDatabaseReference;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.viewpager.setAdapter(
                new HomeFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));
        binding.tabs.setupWithViewPager(binding.viewpager);
        TabUtils.bindIcons(MainActivity.this, binding.viewpager, binding.tabs);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(ConstantsAndUtils.USERS);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user);
                    Toast.makeText(getContext(), getString(R.string.welcome, mUsername),
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

    public MainActivitySubComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent =
                    ((CoherenceApplication) getApplication()).getPresentationLayerComponent()
                            .newListCompositionActivitySubComponent(
                                    new MainActivityModule(this, R.id.idea_category_recipe_v2));
//                                    new MainActivityModule(this, R.id.idea_category_recipe));
// NOTE: recipe v2 provides more information
//                                    new MainActivityModule(this, R.id.idea_category_debug));
// NOTE: use idea_category_debug for mock data
        }
        return mActivityComponent;
    }

    @Override
    public void showPreviewDialog(Goal goal) {
        GoalPreviewFragment previewDialog = GoalPreviewFragment.newInstance(goal);
        previewDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        previewDialog.show(getSupportFragmentManager(), IDEA_PREVIEW_FRAGMENT);
    }

    public void share(Intent i) {
        startActivity(i);
    }

    @Override
    public void inject(ListCompositionFragment fragment) {
        getActivityComponent().inject(fragment);
    }

    @Override
    public void inject(IdeaReviewFragment fragment) {
        getActivityComponent().inject(fragment);
    }

    @Override
    public void inject(GoalSearchFragment fragment) {
        getActivityComponent().inject(fragment);
    }

    @Override
    public void inject(SavedGoalsFragment fragment) {
        getActivityComponent().inject(fragment);
    }

    private void onSignedInInitialize(FirebaseUser user) {
        mUsername = user.getDisplayName();
        // Add user to db
        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put(ConstantsAndUtils.TIMESTAMP, ServerValue.TIMESTAMP);
        User currentUser = new User(mUsername, user.getEmail().replace(".", ","), timestampJoined);
        mUsersDatabaseReference.child(user.getEmail().replace(".", ",")).setValue(currentUser);

        // add user information to sharedPref
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantsAndUtils.EMAIL, user.getEmail().replace(".", ","));
        editor.putString(ConstantsAndUtils.NAME, user.getDisplayName());
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
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
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

    public void onIdeaCompositionClick(View view) {
        ListCompositionFragment listCompositionFragment = ListCompositionFragment.newInstance();
        listCompositionFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        listCompositionFragment.show(getSupportFragmentManager(), LIST_COMPOSITION_FRAGMENT);
    }

    public void onIdeaSearchClick(View view) {
        GoalSearchFragment searchResultFragment = GoalSearchFragment.newInstance();
        searchResultFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        searchResultFragment.show(getSupportFragmentManager(), IDEA_SEARCH_RESULT_FRAGMENT);
    }
}
