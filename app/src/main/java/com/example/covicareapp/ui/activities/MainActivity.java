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
import com.example.covicareapp.ui.fragments.AddedProfilesFragment;
import com.example.covicareapp.ui.fragments.HomeFragment;
import com.example.covicareapp.ui.fragments.ProfilesAddedToFragment;
import com.example.covicareapp.ui.fragments.VitalsHistoryFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private AppBarConfiguration mAppBarConfiguration;

    //      UI Variables
    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

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

        showFragments(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
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
            showFragments(new AddedProfilesFragment());
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
}