package ru.smirnov.test.moretechapp.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.smirnov.test.moretechapp.R;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = CameraActivity.class.getName();

    public final static String BYTE_IMAGE = "byte_img";

    private byte[] currentImage;

    @BindView(R.id.take_photo)
    private FloatingActionButton takePicture;

    @BindView(R.id.image_preview)
    private ImageView imagePreview;

    @BindView(R.id.viewFinder)
    private PreviewView previewView;

    private FloatingActionButton repeatCapture;
    private FloatingActionButton nextBtn;
    private FloatingActionButton closeBtn;

    private ImageCapture imageCapture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        startCamera();

        takePicture = findViewById(R.id.take_photo);
        imagePreview = findViewById(R.id.image_preview);
        previewView = findViewById(R.id.viewFinder);
        repeatCapture = findViewById(R.id.repeat_btn);
        nextBtn = findViewById(R.id.next_btn);
        closeBtn = findViewById(R.id.close_camera);

        closeBtn.setOnClickListener(view -> {
            finish();
        });

        repeatCapture.setOnClickListener(view -> {
            imagePreview.setVisibility(View.GONE);
            repeatCapture.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            previewView.setVisibility(View.VISIBLE);
            takePicture.setVisibility(View.VISIBLE);
        });

        nextBtn.setOnClickListener(view -> {
            File imgFile = new File(getCacheDir(), "newImages.jpg");
            if (!imgFile.exists()) {
                try {
                    imgFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream stream = new FileOutputStream(imgFile);
                stream.write(currentImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, RecognitionResultActivity.class);
            intent.putExtra(BYTE_IMAGE, imgFile.getAbsolutePath());
//            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
        takePicture.setOnClickListener(this);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                bindCamera(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG,"Could not load camera");
            }
        }, ContextCompat.getMainExecutor(this));

    }

    private void bindCamera(ProcessCameraProvider cameraProvider) {
        previewView.setImplementationMode(PreviewView.ImplementationMode.PERFORMANCE);
        Preview preview = (new Preview.Builder())
                .build();

        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(Surface.ROTATION_0)
                .build();


        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, preview);
    }

    @Override
    public void onClick(View view) {
        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                previewView.setVisibility(View.GONE);
                takePicture.setVisibility(View.GONE);
                repeatCapture.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                imagePreview.setVisibility(View.VISIBLE);
                imagePreview.setImageBitmap(imageProxyToBitmap(image));
                imagePreview.setRotation(90f);
                super.onCaptureSuccess(image);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                super.onError(exception);
            }
        });
    }

    private Bitmap imageProxyToBitmap(ImageProxy imageProxy) {
        ImageProxy.PlaneProxy planeProxy = imageProxy.getPlanes()[0];
        ByteBuffer byteBuffer = planeProxy.getBuffer();
        byte[] imageBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(imageBytes);
        currentImage = imageBytes;
        return BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);
    }
}
