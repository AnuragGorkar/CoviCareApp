package com.example.covicareapp.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

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

    //      UI Variables
    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

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

        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        showFragments(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            toolbar.setTitle("CoviCare");
            showFragments(new HomeFragment());
        }
        if (id == R.id.nav_vitals_history) {
            toolbar.setTitle("Vitals History");
            showFragments(new VitalsHistoryFragment());
        }
        if (id == R.id.nav_profiles_added_to) {
            toolbar.setTitle("Profiles added to");
            showFragments(new ProfilesAddedToFragment());
        }
        if (id == R.id.nav_added_profiles) {
            toolbar.setTitle("Added Profiles");
            getFirebaseUserData();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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