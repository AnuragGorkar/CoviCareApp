package com.example.covicareapp.ui.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covicareapp.R;
import com.example.covicareapp.databinding.ActivityDisplayImageBinding;
import com.example.covicareapp.helpers.Constants;
import com.example.covicareapp.ml.ModelMobilenet20epoch;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

public class DisplayImageActivity extends AppCompatActivity {

    private static final String TAG = "DisplayImageActivity";
    ActivityDisplayImageBinding binding;
//    Session sess;
    byte[] graphDef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        int fromActivity = intent.getIntExtra(Constants.FROM_ACTIVITY, Constants.PICK_IMAGE);
        Bitmap bitmap = null;
        if (fromActivity == Constants.CAPTURE_IMAGE) {

            binding.imageName.setVisibility(View.GONE);
            Uri uri = Uri.parse(intent.getStringExtra(Constants.IMAGE_URI));

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                binding.image.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.e(TAG, "onCreate: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (fromActivity == Constants.PICK_IMAGE) {

            Uri selectedImage = Uri.parse(intent.getStringExtra(Constants.IMAGE_URI));
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            binding.imageName.setVisibility(View.VISIBLE);

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.d(TAG, "onActivityResult: picturePath : " + picturePath);


            binding.imageName.setText(Paths.get(picturePath).getFileName().toString());
            bitmap = BitmapFactory.decodeFile(picturePath);
            binding.image.setImageBitmap(bitmap);
        }

        try {
            assert bitmap != null;
            infer(bitmap);
//            inferSeg(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void infer(Bitmap bitmap) throws IOException {


        Log.d(TAG, "infer: " + bitmap.getWidth() + " x " + bitmap.getHeight());
        ModelMobilenet20epoch modelMobilenet20epoch = ModelMobilenet20epoch.newInstance(this);
//        Covid19DetectModel covid19DetectModel = Covid19DetectModel.newInstance(this);

        // Creates inputs for reference.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                        .build();

        TensorImage tfImage = new TensorImage(DataType.FLOAT32);
        tfImage.load(bitmap);
        tfImage = imageProcessor.process(tfImage);

        Log.d(TAG, "infer: remaining " + tfImage.getBuffer().remaining());


//        TensorImage tfImage = TensorImage.fromBitmap(bitmap);


        ByteBuffer byteBuffer = ByteBuffer.allocate(224 * 224 * 3);
        byteBuffer.rewind();
        byteBuffer = tfImage.getBuffer();
        Log.d(TAG, "infer: " + tfImage.getBuffer().array().length);

        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
        inputFeature0.loadBuffer(byteBuffer);


        FloatBuffer outputBuffer = FloatBuffer.allocate(1);

//        ModelMobilenet95.Outputs outputs = model.process(inputFeature0);
//        Covid19DetectModel.Outputs outputs = covid19DetectModel.process(inputFeature0);

        TensorBuffer probabilityBuffer =
                TensorBuffer.createFixedSize(new int[]{1, 1}, DataType.FLOAT32);

        try {
            Interpreter interpreter = new Interpreter(loadModelFile());
            interpreter.run(tfImage.getBuffer(), outputBuffer);

            float[] output = outputBuffer.array();
            Log.d(TAG, "infer: output buffer : " + output[0]);

            updateUI(output[0]);

            interpreter.close();
        } catch (IOException e) {
            Log.e(TAG, "train: " + e.getMessage());
            e.printStackTrace();
        }


//        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

//        Log.d(TAG, "infer: " + outputFeature0.toString());
//
//        float[] data = outputFeature0.getFloatArray();
//         updateUI(data[0]);
//


        // Releases model resources if no longer used.
//        model.close();
        modelMobilenet20epoch.close();


//        binding.trainButton.setOnClickListener(view -> {
////            trainModel(bitmap);
//            trainVisionAir(bitmap);
//        });

//        covid19DetectModel.close();
    }

    private void updateUI(float pred) {
        binding.preds.setText(String.valueOf(pred));

        if (pred > 1 - pred) {
            binding.result.setText(R.string.positive);
            binding.result.setTextColor(getResources().getColor(R.color.red));
        } else {
            binding.result.setText(R.string.negative);
            binding.result.setTextColor(getResources().getColor(R.color.green));
        }

    }


    private MappedByteBuffer loadModelFile() throws IOException {
        String MODEL_ASSETS_PATH = "model_mobilenet_20epoch.tflite";
//        String MODEL_ASSETS_PATH = "covid_19_detect_model_1.tflite";

        AssetFileDescriptor assetFileDescriptor = getApplicationContext().getAssets().openFd(MODEL_ASSETS_PATH);
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startoffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startoffset, declaredLength);
    }

}