package com.doublesp.coherence.activities;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import com.batch.android.Batch;
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
import com.doublesp.coherence.fragments.SavedIdeasFragment;
import com.doublesp.coherence.interfaces.domain.IdeaInteractorInterface;
import com.doublesp.coherence.interfaces.presentation.GoalActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.GoalDetailActionHandlerInterface;
import com.doublesp.coherence.interfaces.presentation.InjectorInterface;
import com.doublesp.coherence.interfaces.presentation.SavedIdeasActionHandlerInterface;
import com.doublesp.coherence.service.NotificationService;
import com.doublesp.coherence.utils.ConstantsAndUtils;
import com.doublesp.coherence.utils.TabUtils;
import com.doublesp.coherence.utils.ToolbarBindingUtils;
import com.doublesp.coherence.viewmodels.Goal;
import com.doublesp.coherence.viewmodels.Plan;
import com.doublesp.coherence.viewmodels.User;
import com.doublesp.coherence.viewmodels.UserList;
import com.firebase.ui.auth.AuthUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

public class MainActivity extends AppCompatActivity implements InjectorInterface,
        GoalActionHandlerInterface.PreviewHandlerInterface,
        GoalDetailActionHandlerInterface.ListCompositionDialogHandlerInterface,
        ListFragmentActionHandler.IdeaShareHandlerInterface,
        SavedIdeasActionHandlerInterface {

    public static final int RC_SIGN_IN = 1;
    static final String IDEA_PREVIEW_FRAGMENT = "IDEA_PREVIEW_FRAGMENT";
    static final String LIST_COMPOSITION_FRAGMENT = "LIST_COMPOSITION_FRAGMENT";
    static final String IDEA_SEARCH_RESULT_FRAGMENT = "IDEA_SEARCH_RESULT_FRAGMENT";
    ActivityMainBinding binding;
    MainActivitySubComponent mActivityComponent;
    @Inject
    IdeaInteractorInterface mIdeaInteractor;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mUsersDatabaseReference;
    private DatabaseReference mListDatabaseReference;
    private DatabaseReference mShoppingListDatabaseReference;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        getActivityComponent().inject(MainActivity.this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ToolbarBindingUtils.bind(this, binding.activityMainToolbarContainer.toolbar);
        binding.viewpager.setAdapter(
                new HomeFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));
        binding.tabs.setupWithViewPager(binding.viewpager);
        TabUtils.bindIcons(MainActivity.this, binding.viewpager, binding.tabs);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(ConstantsAndUtils.USERS);
        mListDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.USER_LISTS).child(ConstantsAndUtils.getOwner(this));
        mShoppingListDatabaseReference = mFirebaseDatabase.getReference().child(
                ConstantsAndUtils.SHOPPING_LISTS);

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

    @Override
    public void showListCompositionDialog(Goal goal) {
        DatabaseReference keyReference = mListDatabaseReference.push();

        HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put(ConstantsAndUtils.TIMESTAMP, ServerValue.TIMESTAMP);
        UserList userList = new UserList(ConstantsAndUtils.getDefaultTitle(this),
                ConstantsAndUtils.getOwner(this), timestampCreated);
        keyReference.setValue(userList);
        Plan plan = mIdeaInteractor.createPlan(keyReference.getKey());
        mShoppingListDatabaseReference.child(keyReference.getKey()).setValue(plan);

        ListCompositionFragment listCompositionFragment = ListCompositionFragment.newInstance(
                keyReference.getKey(), goal);
        listCompositionFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        listCompositionFragment.show(getSupportFragmentManager(), LIST_COMPOSITION_FRAGMENT);
    }

    @Override
    public void showListCompositionDialog(String listId) {
        ListCompositionFragment listCompositionFragment = ListCompositionFragment.newInstance(
                listId);
        listCompositionFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        listCompositionFragment.show(getSupportFragmentManager(), LIST_COMPOSITION_FRAGMENT);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Batch.onNewIntent(this, intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Batch.onStart(this);
    }

    @Override
    protected void onStop() {
        Batch.onStop(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Batch.onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void share(Intent i) {
        startActivity(i);
    }

    @Override
    public void search(Plan plan) {
        GoalSearchFragment goalSearchFragment = GoalSearchFragment.newInstance(plan);
        goalSearchFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        goalSearchFragment.show(getSupportFragmentManager(), IDEA_SEARCH_RESULT_FRAGMENT);
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

    @Override
    public void inject(GoalPreviewFragment fragment) {
        getActivityComponent().inject(fragment);
    }

    @Override
    public void inject(SavedIdeasFragment fragment) {
        getActivityComponent().inject(fragment);
    }

    private void onSignedInInitialize(FirebaseUser user) {
        mUsername = user.getDisplayName();
        // Add user to db
        HashMap<String, Object> timestampJoined = new HashMap<>();
        timestampJoined.put(ConstantsAndUtils.TIMESTAMP, ServerValue.TIMESTAMP);
        User currentUser = new User(mUsername, user.getEmail().replace(".", ","), timestampJoined);
        mUsersDatabaseReference.child(user.getEmail().replace(".", ",")).setValue(currentUser);

        startService(new Intent(this, NotificationService.class));

        // add user information to sharedPref
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                getContext());
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
    protected void onPostResume() {
        super.onPostResume();
        handleDeeplink();
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

        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO: perform query here
                int currentItem = binding.viewpager.getCurrentItem();


                // workaround to avoid issues with some emulators and keyboard devices
                // firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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
        stopService(new Intent(this, NotificationService.class));
    }

    public void onIdeaCompositionClick(View view) {
        DatabaseReference keyReference = mListDatabaseReference.push();

        HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put(ConstantsAndUtils.TIMESTAMP, ServerValue.TIMESTAMP);
        UserList userList = new UserList(ConstantsAndUtils.getDefaultTitle(this),
                ConstantsAndUtils.getOwner(this), timestampCreated);
        keyReference.setValue(userList);
        Plan plan = mIdeaInteractor.createPlan(keyReference.getKey());
        mShoppingListDatabaseReference.child(keyReference.getKey()).setValue(plan);

        showListCompositionDialog(keyReference.getKey());
    }

    public void onBookmarkClick(View view) {
        SavedGoalsFragment savedGoalsFragment = SavedGoalsFragment.newInstance();
        savedGoalsFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
        savedGoalsFragment.show(getSupportFragmentManager(), IDEA_SEARCH_RESULT_FRAGMENT);
    }

    private void handleDeeplink() {
        Intent intent = getIntent();
        final Uri data = intent.getData();
        if (data == null) {
            return;
        }
        int index = data.toString().indexOf("shared/") + "shared/".length();
        String listId = data.toString().substring(index);
        if (listId == null) {
            return;
        }
        showListCompositionDialog(listId);
    }

}
