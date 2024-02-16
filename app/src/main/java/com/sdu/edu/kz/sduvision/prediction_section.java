package com.sdu.edu.kz.sduvision;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdu.edu.kz.sduvision.ml.MobilenetV110224Quant;
import com.sdu.edu.kz.sduvision.toolbar.fragment_main;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class prediction_section extends AppCompatActivity {
    Button selectBtn, predictBtn, captureBtn, backBtn, saveToNotesBtn;
    ImageView imageView;
    TextView result;
    Bitmap bitmap;
    private DatabaseReference mDatabase;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_section);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("notes");

        getPermission();

        String[] labels = new String[1001];
        int count = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("labels.txt")));
            String line = bufferedReader.readLine();
            while (line != null) {
                labels[count] = line;
                count++;
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = findViewById(R.id.predictBtn);
        imageView = findViewById(R.id.imageView);
        result = findViewById(R.id.result);
        captureBtn = findViewById(R.id.captureBtn);
        backBtn = findViewById(R.id.backBtn);
        saveToNotesBtn = findViewById(R.id.saveToNotesBtn);
        saveToNotesBtn.setOnClickListener(v -> saveToNotes());

        selectBtn.setOnClickListener(v -> selectImage());
        captureBtn.setOnClickListener(v -> captureImage());
        predictBtn.setOnClickListener(v -> predictImage(labels));
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(prediction_section.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 12);
    }

    private void predictImage(String[] labels) {
        try {
            // Load the TensorFlow Lite model.
            MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(this);

            // Create input for the model.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);

            // Preprocess the bitmap image to fit the model input.
            bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);
            inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());

            // Run model inference and get the result.
            MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            // Update the result TextView with the prediction.
            result.setText(labels[getMax(outputFeature0.getFloatArray())] + " ");

            // Release model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    private int getMax(float[] arr) {
        int maxIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                getPermission(); // Re-ask for permission if not granted.
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 10) {
                // Handle image selection
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 12) {
                // Handle image capture
                Bundle extras = data.getExtras();
                if (extras != null) {
                    bitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    public void saveToNotes() {
        Log.d("saveToNotes", "Method called");
        // Create a unique key for the new note entry
        String noteId = mDatabase.child("notes").push().getKey();

        // Create a note object or a Map
        // Assuming a Note class exists with appropriate fields and constructor
        Note note = new Note("Note Title", result.getText().toString(), System.currentTimeMillis());

        // Save the note object to Firebase
        mDatabase.child("notes").child(noteId).setValue(note)
                .addOnSuccessListener(aVoid -> {
                    Log.d("saveToNotes", "Note saved successfully");
                    Toast.makeText(prediction_section.this, "Note saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("saveToNotes", "Failed to save note", e);
                    Toast.makeText(prediction_section.this, "Failed to save note", Toast.LENGTH_SHORT).show();
                });
    }
}
