package com.example.covicareapp.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.airbnb.lottie.LottieAnimationView;
import com.example.covicareapp.R;
import com.example.covicareapp.databinding.ActivityMainBinding;
import com.example.covicareapp.logic.EncryptDecryptData;
import com.example.covicareapp.ui.activities.addedGroups.SelectGroupActivity;
import com.example.covicareapp.ui.fragments.GroupsAddedToFragment;
import com.example.covicareapp.ui.fragments.HomeFragment;
import com.example.covicareapp.ui.fragments.VitalsHistoryFragment;
import com.example.covicareapp.ui.fragments.addedGroups.AddedGroupsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.scanlibrary.ScanConstants;

import java.util.ArrayList;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    // Variables
    int prevItemId;
    String email;

    //      UI Variables
    Dialog dialog;
    ImageView qr_code;
    TextView qr_info;
    LinearLayout select_qr_type_layout, show_qr_layout;
    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    ImageButton closeDialogueButton, backDialogueButton;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    LottieAnimationView loadingAnimation;
    TextView loadingTv;
    MaterialButton show_add_user_qr_code, show_share_vitals_qr_code;
    Menu menu;
    HashMap<String, Object> userData = new HashMap<String, Object>();
    ArrayList<String> groupsCreatedIds = new ArrayList<String>();
    ArrayList<String> groupsAddedToIds = new ArrayList<String>();
    private AppBarConfiguration mAppBarConfiguration;


    private static final int RESULT_LOAD_IMAGE = 101;
    private static final int CAMERA_REQUEST_CODE = 99;
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 102;
    private static final int SCAN_QR_CODE_REQUEST_CODE = 103;
    int preference = ScanConstants.OPEN_CAMERA;
    AlertDialog alertDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private ActivityMainBinding mBinding;


    ConstraintLayout addedGroups, groupsAddedTo, home, vitalsHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
        }

        //      UI Hooks
//        addedGroups = findViewById(R.id.added_groups_fragment);
//        groupsAddedTo = findViewById(R.id.groups_added_to_fragment);
//        home = findViewById(R.id.home_fragment);
//        vitalsHistory = findViewById(R.id.vitals_history_fragment);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIconTint(getColor(R.color.teal_200));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        loadingAnimation = findViewById(R.id.loading_lottie);
        loadingTv = findViewById(R.id.loading_text);
        setSupportActionBar(toolbar);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        email = firebaseUser.getEmail();

        loadingTv.setVisibility(View.GONE);
        loadingAnimation.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        goToFragment();
    }

    public void goToFragment(String fragmentName) {
        if (fragmentName.equals("Vitals History")) {
//                vitalsHistory.setVisibility(View.VISIBLE);
            prevItemId = R.id.nav_vitals_history;
            toolbar.setTitle("Vitals History");
            navigationView.setCheckedItem(R.id.nav_vitals_history);
            showFragments(new VitalsHistoryFragment(), false);
        }
    }

    public void goToFragment() {
        // Get from previous activity which fragment to open
        Intent intent = getIntent();
        String fragmentName = intent.getStringExtra("Fragment");
//        groupsAddedTo.setVisibility(View.GONE);
//        addedGroups.setVisibility(View.GONE);
//        home.setVisibility(View.GONE);
//        vitalsHistory.setVisibility(View.GONE);
        if (fragmentName != null) {
            if (fragmentName.equals("Home")) {
//                home.setVisibility(View.VISIBLE);
                prevItemId = R.id.nav_home;
                toolbar.setTitle("CoviCare");
                navigationView.setCheckedItem(R.id.nav_home);
                showFragments(new HomeFragment(), true);
            } else if (fragmentName.equals("Vitals History")) {
//                vitalsHistory.setVisibility(View.VISIBLE);
                prevItemId = R.id.nav_vitals_history;
                toolbar.setTitle("Vitals History");
                navigationView.setCheckedItem(R.id.nav_vitals_history);
                showFragments(new VitalsHistoryFragment(), false);
            } else if (fragmentName.equals("Groups Added to")) {
//                groupsAddedTo.setVisibility(View.VISIBLE);
                prevItemId = R.id.nav_profiles_added_to;
                toolbar.setTitle("Groups Added to");
                navigationView.setCheckedItem(R.id.nav_profiles_added_to);
                getFirebaseUserData("Groups Added to");
            } else if (fragmentName.equals("Added Groups")) {
//                addedGroups.setVisibility(View.VISIBLE);
                prevItemId = R.id.nav_added_profiles;
                toolbar.setTitle("Added Groups");
                navigationView.setCheckedItem(R.id.nav_added_profiles);
                showMenuOptions(false);
                getFirebaseUserData("Added Groups");
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
//        addedGroups.setVisibility(View.GONE);
//        groupsAddedTo.setVisibility(View.GONE);
//        home.setVisibility(View.GONE);
//        vitalsHistory.setVisibility(View.GONE);
        if (id != prevItemId) {
            if (id == R.id.nav_home) {
//                home.setVisibility(View.VISIBLE);
                prevItemId = id;
                toolbar.setTitle("CoviCare");
                showMenuOptions(true);
                showFragments(new HomeFragment(), true);
            }
            if (id == R.id.nav_vitals_history) {
//                vitalsHistory.setVisibility(View.VISIBLE);
                prevItemId = id;
                toolbar.setTitle("Vitals History");
                showMenuOptions(false);
                showFragments(new VitalsHistoryFragment(), false);
            }
            if (id == R.id.nav_profiles_added_to) {
//                groupsAddedTo.setVisibility(View.VISIBLE);
                prevItemId = id;
                toolbar.setTitle("Groups Added to");
                showMenuOptions(false);
                getFirebaseUserData("Groups Added to");
            }
            if (id == R.id.nav_added_profiles) {
//                addedGroups.setVisibility(View.VISIBLE);
                prevItemId = id;
                toolbar.setTitle("Added Groups");
                showMenuOptions(false);
                getFirebaseUserData("Added Groups");
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
                showAddOnlineUserDialogue();
                break;
            case R.id.add_new_user: {
                Intent intent = new Intent(MainActivity.this, SelectGroupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFragments(Fragment fragment, boolean showOptions) {
        showMenuOptions(showOptions);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.frame_layout, fragment, null).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
        }

    }



    public void showAddOnlineUserDialogue() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_show_qr_dialogue_box);

        // UI Hooks
        closeDialogueButton = dialog.findViewById(R.id.close_dialogue);
        backDialogueButton = dialog.findViewById(R.id.back_dialogue);
        select_qr_type_layout = dialog.findViewById(R.id.select_qr_type);
        show_add_user_qr_code = dialog.findViewById(R.id.show_add_user_qr);
        show_share_vitals_qr_code = dialog.findViewById(R.id.show_share_vitals_qr);

        show_qr_layout = dialog.findViewById(R.id.share_by_qr);
        qr_code = dialog.findViewById(R.id.qr_code_view);
        qr_info = dialog.findViewById(R.id.qr_info);

        show_qr_layout.setVisibility(View.GONE);
        backDialogueButton.setVisibility(View.GONE);

        show_add_user_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_qr_type_layout.setVisibility(View.GONE);
                show_qr_layout.setVisibility(View.VISIBLE);
                backDialogueButton.setVisibility(View.VISIBLE);

                EncryptDecryptData encryptDecryptData = new EncryptDecryptData();

                try {
                    String val = encryptDecryptData.encryptEmail(email).toString();

                    QRGEncoder qrgEncoder = new QRGEncoder(val, null, QRGContents.Type.TEXT, 200);
                    Bitmap qrBits = qrgEncoder.getBitmap();
                    qr_code.setImageBitmap(qrBits);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        show_share_vitals_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        backDialogueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_qr_type_layout.setVisibility(View.VISIBLE);
                show_qr_layout.setVisibility(View.GONE);
                backDialogueButton.setVisibility(View.GONE);


            }
        });

        closeDialogueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.70);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_toolbar_background);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

    }

    public void getFirebaseUserData(String fragmentName) {
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
                                        if (fragmentName.equals("Groups Added to")) {
                                            groupsAddedToIds = (ArrayList<String>) documentSnapshot.getData().get("groupsAddedTo");

                                            Log.i("User Data    ", userData.toString());

                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("userData", userData);
                                            bundle.putSerializable("groupsAddedToIds", groupsAddedToIds);
                                            GroupsAddedToFragment groupsAddedToFragment = new GroupsAddedToFragment();
                                            groupsAddedToFragment.setArguments(bundle);
                                            loadingTv.setVisibility(View.GONE);
                                            loadingAnimation.setVisibility(View.GONE);
                                            showFragments(groupsAddedToFragment, false);

                                        } else if (fragmentName.equals("Added Groups")) {
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
                                        }
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