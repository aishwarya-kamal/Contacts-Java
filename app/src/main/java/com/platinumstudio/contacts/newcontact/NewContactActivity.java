package com.platinumstudio.contacts.newcontact;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.platinumstudio.contacts.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class NewContactActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT_NAME = "NAME";
    public static final String EXTRA_CONTACT_NUMBER = "NUMBER";
    public static final String EXTRA_CONTACT_EMAIL = "EMAIL";
    public static final String EXTRA_CONTACT_PROFILE_PICTURE = "PROFILE_PICTURE";

    private EditText mEditName, mEditNumber, mEditEmail;

    private Button buttonUpload;
    private ImageView profilePicture;
    private static final String IMAGE_DIRECTORY = "/myContactImages";
    private int GALLERY = 1, CAMERA = 2;

    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        mEditName = findViewById(R.id.edit_text_name);
        mEditNumber = findViewById(R.id.edit_text_number);
        mEditEmail = findViewById(R.id.edit_text_email);

        final Button mButtonSave = findViewById(R.id.button_save);

        mButtonSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent replyIntent = new Intent();

                if (TextUtils.isEmpty(mEditName.getText())
                        || TextUtils.isEmpty(mEditNumber.getText())
                        || TextUtils.isEmpty(mEditEmail.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);

                } else {

                    String name = mEditName.getText().toString();
                    long number = Long.parseLong(mEditNumber.getText().toString());
                    String email = mEditEmail.getText().toString();

                    replyIntent.putExtra(EXTRA_CONTACT_NAME, name);
                    replyIntent.putExtra(EXTRA_CONTACT_NUMBER, number);
                    replyIntent.putExtra(EXTRA_CONTACT_EMAIL, email);
                    replyIntent.putExtra(EXTRA_CONTACT_PROFILE_PICTURE, path);

                    setResult(RESULT_OK, replyIntent);
                }

                finish();
            }
        });

        buttonUpload = findViewById(R.id.button_upload);
        profilePicture = findViewById(R.id.profile_picture);

        requestMultiplePermissions();

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectActionDialog();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showSelectActionDialog() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Choose Action");

        String[] pictureDialogItems = {"Choose picture from Gallery", "Capture picture from Camera"};

        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int selectedOption) {
                switch (selectedOption) {

                    case 0:
                        choosePhotoFromGallery();
                        break;

                    case 1:
                        capturePhotoFromCamera();
                        break;
                }
            }
        });

        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void capturePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY) {

            if (data != null) {

                Uri contentURI = data.getData();

                path = contentURI.toString();
                Toast.makeText(NewContactActivity.this, "Picture Saved!", Toast.LENGTH_SHORT).show();

                Glide.with(NewContactActivity.this)
                        .load(contentURI)
                        .apply(RequestOptions.circleCropTransform())
                        .into((ImageView) findViewById(R.id.profile_picture));

            }

        } else if (requestCode == CAMERA) {

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            path = saveImage(thumbnail);

            Uri contentURI = Uri.fromFile(new File(path));

            path = contentURI.toString();

            Glide.with(NewContactActivity.this)
                    .load(contentURI)
                    .apply(RequestOptions.circleCropTransform())
                    .into((ImageView) findViewById(R.id.profile_picture));

            Toast.makeText(NewContactActivity.this, "Picture Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File file = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");

            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes.toByteArray());

            MediaScannerConnection.scanFile(this,
                    new String[]{file.getPath()},
                    new String[]{"image/jpeg"}, null);

            fileOutputStream.close();

            Log.d("TAG", "File Saved to:--->" + file.getAbsolutePath());

            return file.getAbsolutePath();

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return "";
    }

    private void requestMultiplePermissions() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are " +
                                    "granted by user!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(
                            List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })

                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ",
                                Toast.LENGTH_SHORT).show();
                    }
                })

                .onSameThread()
                .check();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}