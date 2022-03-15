package com.example.covicareapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.airbnb.lottie.LottieAnimationView;
import com.example.covicareapp.R;
import com.example.covicareapp.ui.fragments.GroupAddedToFragment;
import com.example.covicareapp.ui.fragments.HomeFragment;
import com.example.covicareapp.ui.fragments.VitalsHistoryFragment;
import com.example.covicareapp.ui.fragments.addedGroups.AddedGroupsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    // Variables
    int prevItemId;
    //      UI Variables
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    LottieAnimationView loadingAnimation;
    TextView loadingTv;
    Menu menu;
    HashMap<String, Object> userData = new HashMap<String, Object>();
    ArrayList<String> groupsCreatedIds = new ArrayList<String>();
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //      UI Hooks
        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        loadingAnimation = findViewById(R.id.loading_lottie);
        loadingTv = findViewById(R.id.loading_text);

        loadingTv.setVisibility(View.GONE);
        loadingAnimation.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        goToFragment();
    }

    public void goToFragment() {
        // Get from previous activity which fragment to open
        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra("Fragment");
        if (fragmentName != null) {
            if (fragmentName.equals("Home")) {
                prevItemId = R.id.nav_home;
                toolbar.setTitle("CoviCare");
                navigationView.setCheckedItem(R.id.nav_home);
                showFragments(new HomeFragment(), true);
            } else if (fragmentName.equals("Vitals History")) {
                prevItemId = R.id.nav_vitals_history;
                toolbar.setTitle("Vitals History");
                navigationView.setCheckedItem(R.id.nav_vitals_history);
                showFragments(new VitalsHistoryFragment(), false);
            } else if (fragmentName.equals("Groups Added to")) {
                prevItemId = R.id.nav_profiles_added_to;
                toolbar.setTitle("Groups Added to");
                navigationView.setCheckedItem(R.id.nav_profiles_added_to);
                showFragments(new GroupAddedToFragment(), false);
            } else if (fragmentName.equals("Added Groups")) {
                prevItemId = R.id.nav_added_profiles;
                toolbar.setTitle("Added Groups");
                navigationView.setCheckedItem(R.id.nav_added_profiles);
                showMenuOptions(false);
                getFirebaseUserData();
            }

        } else {
            prevItemId = R.id.nav_home;
            toolbar.setTitle("CoviCare");
            navigationView.setCheckedItem(R.id.nav_home);
            showFragments(new HomeFragment(), true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id != prevItemId) {
            if (id == R.id.nav_home) {
                prevItemId = id;
                toolbar.setTitle("CoviCare");
                showMenuOptions(true);
                showFragments(new HomeFragment(), true);
            }
            if (id == R.id.nav_vitals_history) {
                prevItemId = id;
                toolbar.setTitle("Vitals History");
                showMenuOptions(false);
                showFragments(new VitalsHistoryFragment(), false);
            }
            if (id == R.id.nav_profiles_added_to) {
                prevItemId = id;
                toolbar.setTitle("Groups Added to");
                showMenuOptions(false);
                showFragments(new GroupAddedToFragment(), false);
            }
            if (id == R.id.nav_added_profiles) {
                prevItemId = id;
                toolbar.setTitle("Added Groups");
                showMenuOptions(false);
                getFirebaseUserData();
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showMenuOptions(boolean show) {
        if (menu != null) {
            Log.i("Menu", "Menu is not null");
            if (show) {
                Log.i("Menu Options Show", "True");
                menu.setGroupVisible(R.id.expandable_options_menu, true);
                menu.setGroupVisible(R.id.show_qr_options, true);
            } else {
                Log.i("Menu Options Show", "False");
                menu.setGroupVisible(R.id.expandable_options_menu, false);
                menu.setGroupVisible(R.id.show_qr_options, false);
            }
        } else
            Log.i("Menu", "Menu is null");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_app_bar, this.menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Log.i("Options Menu Click", "Settings Clicked");
                break;
            case R.id.logout:
                Log.i("Options Menu Click", "Logout Clicked");
                break;
            case R.id.share_by_qr:
                Log.i("Options Menu Click", "Share Info by QR Clicked");
                break;
            case R.id.add_new_user: {
                Intent intent = new Intent(MainActivity.this, SelectGroupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFragments(Fragment fragment, boolean showOptions) {
        showMenuOptions(showOptions);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.frame_layout, fragment, null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
        }

    }

    public void getFirebaseUserData() {
        loadingTv.setText("Loading Groups...");
        loadingTv.setVisibility(View.VISIBLE);
        loadingAnimation.setVisibility(View.VISIBLE);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference userCollectionReference = firebaseFirestore.collection("users");

        userCollectionReference.document(firebaseUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        userData = (HashMap<String, Object>) documentSnapshot.getData();

                        userCollectionReference.document(userData.get("email").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        groupsCreatedIds = (ArrayList<String>) documentSnapshot.getData().get("groupsCreated");
                                        groupsCreatedIds.remove(userData.get("email").toString());

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("userData", userData);
                                        bundle.putSerializable("groupsCreatedIds", groupsCreatedIds);
                                        AddedGroupsFragment addedGroupOnlineUsersFragment = new AddedGroupsFragment();
                                        addedGroupOnlineUsersFragment.setArguments(bundle);
                                        loadingTv.setVisibility(View.GONE);
                                        loadingAnimation.setVisibility(View.GONE);
                                        showFragments(addedGroupOnlineUsersFragment, false);
                                    } else {
                                        // No Profile Created

                                    }
                                } else {
                                    // Error Fetching Profiles
                                }

                            }
                        });

                    } else {
                        // No User
                    }

                } else {
                    // No User
                }
            }
        });
    }
}