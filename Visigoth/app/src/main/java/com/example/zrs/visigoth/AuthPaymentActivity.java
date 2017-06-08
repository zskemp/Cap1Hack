package com.example.zrs.visigoth;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.VerifyResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;

public class AuthPaymentActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "CoreSkillsPrefsFile";

    private static final String TAG = "TakeAuthPicActivity";
    private Button takePictureButton;
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private String mPayee;
    private String mAmount;

    private ImageView pic0;

    // The URI of photo taken with camera
    private Uri mUriPhotoTaken;

    Button mSubmit;
    Button mTakePhoto;

    // Flag to indicate which task is to be performed.
    private static final int REQUEST_SELECT_IMAGE_0 = 0;

    // The IDs of the two faces to be verified.
    private UUID mFaceId0;
    private UUID mFaceId1;

    // The two images from where we get the two faces to verify.
    private Bitmap mBitmap0;
    private Bitmap mBitmap1;

    // When the activity is created, set all the member variables to initial state.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mPayee= null;
                mAmount= null;
            } else {
                mPayee= extras.getString("PERSON_TO_PAY");
                mAmount= extras.getString("AMOUNT");
            }
        } else {
            mPayee = (String) savedInstanceState.getSerializable("PERSON_TO_PAY");
            mAmount = (String) savedInstanceState.getSerializable("AMOUNT");
        }

        mSubmit = (Button) findViewById(R.id.authenticateBtn);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform API call to authenticate
                verify(v);
                makeTransation();
            }
        });

        textureView = (TextureView) findViewById(R.id.texture);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        takePictureButton = (Button) findViewById(R.id.btn_takepicture);
        assert takePictureButton != null;
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        pic0 = (ImageView)findViewById(R.id.picture0);

        //makeAPICall();
        setSecondPic();

        Toast.makeText(getApplicationContext(), mPayee + "  " + mAmount, Toast.LENGTH_SHORT).show();
    }


    public void setSecondPic() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        switch (mPayee) {
            case "Zach":
                pic0.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.zach, 100, 100));
                Bitmap im1 = decodeSampledBitmapFromResource(getResources(), R.drawable.zach, 100, 100);
                mBitmap0 = im1;
                break;
            case "Kyle":
                pic0.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.zach, 100, 100));
                Bitmap im2 = decodeSampledBitmapFromResource(getResources(), R.drawable.zach, 100, 100);
                mBitmap0 = im2;
                break;
            case "Ben":
                pic0.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.ben, 100, 100));
                Bitmap im3 = decodeSampledBitmapFromResource(getResources(), R.drawable.ben, 100, 100);
                mBitmap0 = im3;
                break;
            case "Riyu":
                pic0.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.zach, 100, 100));
                Bitmap im4 = decodeSampledBitmapFromResource(getResources(), R.drawable.zach, 100, 100);
                mBitmap0 = im4;
                break;
            default:
                pic0.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.zach, 100, 100));
                Bitmap im5 = decodeSampledBitmapFromResource(getResources(), R.drawable.zach, 100, 100);
                mBitmap0 = im5;
                break;
        }

        //pic0.setImageBitmap(mBitmap0);
//        pic0.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.zach, 100, 100));

    }

    // Start detecting in image specified by index.
    private void detect(Bitmap bitmap, int index) {
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        // Start a background task to detect faces in the image.
        new AuthPaymentActivity.DetectionTask(index).execute(inputStream);
    }

    // Called when the "Verify" button is clicked.
    public void verify(View view) {
        detect(mBitmap1, 1);

        detect(mBitmap0, 0);

        new AuthPaymentActivity.VerificationTask(mFaceId0, mFaceId1).execute();
    }

    private void setUiAfterDetection(Face[] result, int index, boolean succeed) {
        if (succeed) {
            // Set the default face ID to the ID of first face, if one or more faces are detected.
            if (result.length != 0) {
                if (index == 0) {
                    mFaceId0 = result[0].faceId;
                }
                else {
                    Log.i("HI", "I got here");
                    mFaceId1 = result[0].faceId;
                }
            }
        }
    }

    //THIS IS THE VERIFICATION STUFF

    // Background task for face verification.
    public class VerificationTask extends AsyncTask<Void, String, VerifyResult> {
        // The IDs of two face to verify.
        private UUID mFaceId0;
        private UUID mFaceId1;

        VerificationTask (UUID faceId0, UUID faceId1) {
            mFaceId0 = faceId0;
            mFaceId1 = faceId1;
        }

        @Override
        protected VerifyResult doInBackground(Void... params) {
            // Get an instance of face service client to detect faces in image.
            FaceServiceClient faceServiceClient = FaceService.getFaceServiceClient();
            try{
                publishProgress("Verifying...");

                // Start verification.
                return faceServiceClient.verify(
                        mFaceId0,      /* The first face ID to verify */
                        mFaceId1);     /* The second face ID to verify */
            }  catch (Exception e) {
                publishProgress(e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPostExecute(VerifyResult result) {
            if (result != null) {
            }

            // Show the result on screen when verification is done.
            if(result.isIdentical) {
                //IF API call comes back positive, do this
                Intent intent = new Intent(AuthPaymentActivity.this, HomeActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Payment Transaction Successful", Toast.LENGTH_SHORT).show();
            } else {
                //If API call comes back negative
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "Authentication Failed! \n Please Try Again :)", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Background task of face detection.
    private class DetectionTask extends AsyncTask<InputStream, String, Face[]> {
        // Index indicates detecting in which of the two images.
        private int mIndex;
        private boolean mSucceed = true;

        DetectionTask(int index) {
            mIndex = index;
        }

        @Override
        protected Face[] doInBackground(InputStream... params) {
            // Get an instance of face service client to detect faces in image.
            FaceServiceClient faceServiceClient = FaceService.getFaceServiceClient();
            try {
                // Start detection.
                return faceServiceClient.detect(
                        params[0],  /* Input stream of image to detect */
                        true,       /* Whether to return face ID */
                        false,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                        null);
            } catch (Exception e) {
                mSucceed = false;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Face[] result) {
            // Set the default face ID to the ID of first face, if one or more faces are detected.
            setUiAfterDetection(result, mIndex, mSucceed);
//            if (result.length != 0) {
//                if (mIndex == 0) {
//                    mFaceId0 = result[0].faceId;
//                } else {
//                    mFaceId1 = result[0].faceId;
//                }
//            }
        }
    }

    //THIS IS THE CAMERA STUFF

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            //open your camera here
            openCamera();
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //This is called when the camera is open
            Log.e(TAG, "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }
        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }
        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Toast.makeText(AuthPaymentActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
            createCameraPreview();
        }
    };
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    protected void takePicture() {
        if(null == cameraDevice) {
            Log.e(TAG, "cameraDevice is null");
            return;
        }
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            final File file = new File(Environment.getExternalStorageDirectory()+"/pic.jpg");
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                        //THIS IS THE IMAGE
                        mBitmap1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }
                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);

                    //API Call
                    Toast.makeText(AuthPaymentActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
                    //createCameraPreview();
                    //Move to next thing

                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(AuthPaymentActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AuthPaymentActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }
    protected void updatePreview() {
        if(null == cameraDevice) {
            Log.e(TAG, "updatePreview error, return");
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void closeCamera() {
        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(AuthPaymentActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        //closeCamera();
        stopBackgroundThread();
        super.onPause();
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_auth_payment);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                mPayee= null;
//                mAmount= null;
//            } else {
//                mPayee= extras.getString("PERSON_TO_PAY");
//                mAmount= extras.getString("AMOUNT");
//            }
//        } else {
//            mPayee = (String) savedInstanceState.getSerializable("PERSON_TO_PAY");
//            mAmount = (String) savedInstanceState.getSerializable("AMOUNT");
//        }
//
//        mSubmit = (Button) findViewById(R.id.authenticateBtn);
//        mSubmit.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform API call to authenticate
//                //IF API call comes back positive, do this
//                Intent intent = new Intent(AuthPaymentActivity.this, HomeActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(), "Payment Transaction Successful", Toast.LENGTH_SHORT).show();
//                //If API call comes back negative
////                finish();
////                startActivity(getIntent());
////                Toast.makeText(getApplicationContext(), "Authentication Failed! \n Please Try Again :)", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        Toast.makeText(getApplicationContext(), mPayee + "  " + mAmount, Toast.LENGTH_SHORT).show();
//    }
public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                     int reqWidth, int reqHeight) {

    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(res, resId, options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(res, resId, options);
}

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void makeTransation(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String accountID = settings.getString("accountID", "");
        Log.i("zzz",accountID);
        APIInterface apiService = APIClient.getClient(accountID).create(APIInterface.class);
        Call<Example> call = apiService.transfer("medium", mPayee, mAmount, "2017-06-08", "description");
    }
}
