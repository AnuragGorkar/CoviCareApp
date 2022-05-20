package com.example.covicareapp.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.covicareapp.R;
import com.example.covicareapp.databinding.FragmentHomeBinding;
import com.example.covicareapp.helpers.Constants;
import com.example.covicareapp.interfaces.OnHomePageClickListener;
import com.example.covicareapp.models.HomePageButton;
import com.example.covicareapp.ui.activities.DisplayImageActivity;
import com.example.covicareapp.ui.activities.QuizActivity;
import com.example.covicareapp.ui.activities.qrscan.ContinuousMonitorScanQRActivity;
import com.example.covicareapp.ui.activities.qrscan.ScanQrActivity;
import com.example.covicareapp.ui.adapters.GridViewAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnHomePageClickListener {

    private static final int RESULT_LOAD_IMAGE = 101;
    private static final int CAMERA_REQUEST_CODE = 990;
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 102;
    int preference = ScanConstants.OPEN_CAMERA;
    FragmentHomeBinding mBinding;

    //    Variables
    boolean isAllEFabVisible;

    //    UI Variables
//    FloatingActionButton oneTimeRecordVitals, continuousRecordVitals, ctScanImage;
//    ExtendedFloatingActionButton recordVitals;
//    MaterialTextView oneTimeRecordText, continuousMonitorText, ctScanText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = mBinding.getRoot();


//        oneTimeRecordVitals = rootView.findViewById(R.id.one_time_monitoring_fab);
//        continuousRecordVitals = rootView.findViewById(R.id.continuous_monitoring_fab);
//        recordVitals = rootView.findViewById(R.id.record_vitals_efab);
//        oneTimeRecordText = rootView.findViewById(R.id.one_time_record_tv);
//        continuousMonitorText = rootView.findViewById(R.id.continuous_monitoring_tv);
//        ctScanImage = rootView.findViewById(R.id.scan_ct_fab);
//        ctScanText = rootView.findViewById(R.id.scanct_tv);
//
//        oneTimeRecordVitals.setVisibility(View.GONE);
//        continuousRecordVitals.setVisibility((View.GONE));
//        ctScanImage.setVisibility((View.GONE));
//        oneTimeRecordText.setVisibility(View.GONE);
//        continuousMonitorText.setVisibility((View.GONE));
//        ctScanText.setVisibility((View.GONE));

        isAllEFabVisible = false;

//        recordVitals.shrink();

        Intent intent = getActivity().getIntent();
        String vitals_data = intent.getStringExtra("Vitals Data");

//        recordVitals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!isAllEFabVisible) {
//                    oneTimeRecordVitals.show();
//                    continuousRecordVitals.show();
//                    ctScanImage.show();
//                    oneTimeRecordText.setVisibility(View.VISIBLE);
//                    continuousMonitorText.setVisibility(View.VISIBLE);
//                    ctScanText.setVisibility((View.VISIBLE));
//
//                    recordVitals.extend();
//
//                    isAllEFabVisible = true;
//                } else {
//                    oneTimeRecordVitals.hide();
//                    continuousRecordVitals.hide();
//                    ctScanImage.hide();
//                    oneTimeRecordText.setVisibility(View.GONE);
//                    continuousMonitorText.setVisibility(View.GONE);
//                    ctScanText.setVisibility((View.GONE));
//
//                    recordVitals.shrink();
//
//                    isAllEFabVisible = false;
//                }
//            }
//        });

//        continuousRecordVitals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

//        oneTimeRecordVitals.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                requestCameraPermission(view);
//            }
//        });

//        ctScanImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        ArrayList<HomePageButton> homepageButtons = new ArrayList<>();

        homepageButtons.add(new HomePageButton(getString(R.string.capture_image), R.drawable.ic_capture_image, Constants.CAPTURE_IMAGE));
        homepageButtons.add(new HomePageButton(getString(R.string.pick_image), R.drawable.ic_pick_image, Constants.PICK_IMAGE));
        homepageButtons.add(new HomePageButton("Scan Vitals QR", R.drawable.ic_scan_qr, Constants.SCAN_QR));
        homepageButtons.add(new HomePageButton("Monitor Vitals", R.drawable.ic_baseline_monitor_heart_24, Constants.MONITOR_VITALS));
        homepageButtons.add(new HomePageButton(getString(R.string.evaluate_mental_health), R.drawable.ic_eval_mental_temp, Constants.MENTAL_HEALTH));

        GridViewAdapter adapter = new GridViewAdapter(getActivity(), homepageButtons, this);

        mBinding.gridview.setAdapter(adapter);
//        CourseGVAdapter adapter = new CourseGVAdapter(this, courseModelArrayList);
//        coursesGV.setAdapter(adapter);

        return rootView;
    }

    private void captureImage() {
        startActivity(new Intent(getActivity(), ScanQrActivity.class));
    }

    public void requestCameraPermission(View view) {
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ScanQrActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                showSnackbar(view, "Please allow permission for QR code Camera scan", "", "Error");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                showSnackbar(view, "Please allow permission for QR code Camera scan", "", "Error");
            }
        }).check();
    }

    protected void showSnackbar(View view, String messageStr, String actionStr, String resultState) {
        // pass the mSnackbarLayout as the view
        // to the make function
        Snackbar snackbar = Snackbar.make(view, messageStr, Snackbar.LENGTH_LONG);

        // the duration is in terms of milliseconds
        snackbar.setDuration(3600);
        // set the background tint color for the snackbar
        switch (resultState) {
            case "Success":
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.success_800));
                break;
            case "Warning":
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.warning_900));
                break;
            case "Error":
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.error_400));
                break;
            default:
                snackbar.setBackgroundTint(ContextCompat.getColor(getActivity(), R.color.information_800));
        }
        // set the action button text color of the snackbar however this is optional
        snackbar.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_50));
        snackbar.setActionTextColor(ContextCompat.getColor(getActivity(), R.color.white_50));

        snackbar.setAction(actionStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // as all the snackbar wont have the action button
        snackbar.show();

    }

    Dialog dialog;
    DialogFragment dialogFragment;
    ImageButton closeDialogueButton, backDialogueButton;

    public void showSelectPickImageDialogue() {
        dialog = new Dialog(getActivity());
//        dialogFragment = new DialogFragment(R.layout.custom_image_pick_dialog);
        dialog.setContentView(R.layout.custom_image_pick_dialog);
        closeDialogueButton = dialog.findViewById(R.id.close_dialogue);
        backDialogueButton = dialog.findViewById(R.id.back_dialogue);
        dialog.findViewById(R.id.covid_identification_button).setOnClickListener(view -> {

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
            startActivityForResult(pickIntent, RESULT_LOAD_IMAGE);


        });

        dialog.findViewById(R.id.image_segmentation_image).setOnClickListener(view -> {
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
            startActivityForResult(pickIntent, RESULT_LOAD_IMAGE + 1);
        });
        closeDialogueButton.setOnClickListener(view -> dialog.dismiss());
        backDialogueButton.setOnClickListener(view -> dialog.dismiss());
//
//
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);



        dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_toolbar_background);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

    @Override
    public void onItemClick(HomePageButton homePageButton) {

        switch (homePageButton.getButtonId()) {
            case Constants.CAPTURE_IMAGE:
                Intent intent = new Intent(getActivity(), ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                break;

            case Constants.PICK_IMAGE:
                showSelectPickImageDialogue();
                break;
            case Constants.SCAN_QR:
                startActivity(new Intent(getActivity(), ScanQrActivity.class));
                break;
            case Constants.MONITOR_VITALS:
                Intent intent1 = new Intent(getActivity(), ContinuousMonitorScanQRActivity.class);
//                Intent intent1 = new Intent(getActivity(), ContinuousDataActivity.class);
                intent1.putExtra("Fragment", "Vitals History");
                startActivity(intent1);
                break;
            case Constants.MENTAL_HEALTH:

                startActivity(new Intent(getActivity(), QuizActivity.class));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + homePageButton.getButtonId());
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();

            Intent intent = new Intent(requireActivity().getApplicationContext(), DisplayImageActivity.class);
            intent.putExtra(Constants.FROM_ACTIVITY, Constants.PICK_IMAGE);
            intent.putExtra(Constants.IMAGE_URI, selectedImage.toString());
            intent.putExtra(Constants.IMAGE_SEGMENTATION, Constants.NO);
            startActivity(intent);

        } else if (requestCode == RESULT_LOAD_IMAGE + 1 && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();

            Intent intent = new Intent(requireActivity().getApplicationContext(), DisplayImageActivity.class);
            intent.putExtra(Constants.FROM_ACTIVITY, Constants.PICK_IMAGE);
            intent.putExtra(Constants.IMAGE_URI, selectedImage.toString());
            intent.putExtra(Constants.IMAGE_SEGMENTATION, Constants.YES);
            startActivity(intent);

        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            if (data != null) {
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Intent intent = new Intent(requireActivity().getApplicationContext(), DisplayImageActivity.class);
                intent.putExtra(Constants.FROM_ACTIVITY, Constants.CAPTURE_IMAGE);
                intent.putExtra(Constants.IMAGE_URI, uri.toString());
                startActivity(intent);
            }
        }
    }
}