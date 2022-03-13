package com.example.covicareapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.airbnb.lottie.LottieAnimationView;
import com.example.covicareapp.R;
import com.example.covicareapp.ui.fragments.HomeFragment;
import com.example.covicareapp.ui.fragments.ProfilesAddedToFragment;
import com.example.covicareapp.ui.fragments.VitalsHistoryFragment;
import com.example.covicareapp.ui.fragments.addedGroups.AddedGroupsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private AppBarConfiguration mAppBarConfiguration;

    // Variables
    int prevItemId;

    //      UI Variables
    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    LottieAnimationView loadingAnimation;
    TextView loadingTv;

    HashMap<String, Object> userData = new HashMap<String, Object>();
    ArrayList<String> groupsCreatedIds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //      UI Hooks
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIconTint(getColor(R.color.teal_200));

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
                showFragments(new HomeFragment());
            } else if (fragmentName.equals("Vitals History")) {
                prevItemId = R.id.nav_vitals_history;
                toolbar.setTitle("Vitals History");
                navigationView.setCheckedItem(R.id.nav_vitals_history);
                showFragments(new VitalsHistoryFragment());
            } else if (fragmentName.equals("Groups Added to")) {
                prevItemId = R.id.nav_profiles_added_to;
                toolbar.setTitle("Groups Added to");
                navigationView.setCheckedItem(R.id.nav_profiles_added_to);
                showFragments(new ProfilesAddedToFragment());
            } else if (fragmentName.equals("Added Groups")) {
                prevItemId = R.id.nav_added_profiles;
                toolbar.setTitle("Added Groups");
                navigationView.setCheckedItem(R.id.nav_added_profiles);
                getFirebaseUserData();
            }

        } else {
            prevItemId = R.id.nav_home;
            toolbar.setTitle("CoviCare");
            navigationView.setCheckedItem(R.id.nav_home);
            showFragments(new HomeFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id != prevItemId) {
            if (id == R.id.nav_home) {
                prevItemId = id;
                toolbar.setTitle("CoviCare");
                showFragments(new HomeFragment());
            }
            if (id == R.id.nav_vitals_history) {
                toolbar.setTitle("Vitals History");
                showFragments(new VitalsHistoryFragment());
            }
            if (id == R.id.nav_profiles_added_to) {
                prevItemId = id;
                toolbar.setTitle("Groups Added to");
                showFragments(new ProfilesAddedToFragment());
            }
            if (id == R.id.nav_added_profiles) {
                prevItemId = id;
                toolbar.setTitle("Added Groups");
                getFirebaseUserData();
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void showFragments(Fragment fragment) {
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
        CollectionReference allGroupsCollectionReference = firebaseFirestore.collection("allGroups");

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
                                        groupsCreatedIds = (ArrayList<String>) documentSnapshot.getData().get("Groups Created");
                                        groupsCreatedIds.remove(userData.get("email").toString());

                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("userData", userData);
                                        bundle.putSerializable("groupsCreatedIds", groupsCreatedIds);
                                        AddedGroupsFragment addedGroupOnlineUsersFragment = new AddedGroupsFragment();
                                        addedGroupOnlineUsersFragment.setArguments(bundle);
                                        loadingTv.setVisibility(View.GONE);
                                        loadingAnimation.setVisibility(View.GONE);
                                        showFragments(addedGroupOnlineUsersFragment);
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