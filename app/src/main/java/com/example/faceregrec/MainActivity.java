package com.example.faceregrec;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1337;
    ImageView imageView;
    Button cameraBtn, galleryBtn;

    //getting the image from gallery and displaying it
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Uri imageUri = result.getData().getData();
                        imageView.setImageURI(imageUri);
                    }
                }
            });

    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        Uri imageUri = result.getData().getData();
                        imageView.setImageURI(imageUri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageView = findViewById(R.id.imageView);
        galleryBtn = findViewById(R.id.button);
        cameraBtn = findViewById(R.id.button2);
        //asking for camera permission upon first launch of application
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                String[] permission = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, 112);
            }
        }

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(galleryIntent);
            }
        });

        Intent cameraOpen = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(cameraIntent);
            }
        });
    }
}