package com.example.sharing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sharing.databinding.ActivityShareContentBinding;
import com.sharing.composer.ShareComposer;
import com.sharing.composer.TextMimeType;

import java.io.File;

import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class ShareContentActivity extends AppCompatActivity {

    ActivityShareContentBinding binding;
    ShareComposer shareComposer;
    Uri selectedUri;
    EasyImage easyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareContentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getIntent().hasExtra("Image")) {
            selectedUri = getIntent().getParcelableExtra("Image");
        }
        shareComposer = new ShareComposer(ShareContentActivity.this);
        binding.btnShare.setOnClickListener(v -> shareText());
        binding.btnShareImage.setOnClickListener(v -> shareImage());

        binding.btnSendEmail.setOnClickListener(v -> sendEmail());
    }

    private void sendEmail() {
        if (isEmpty(binding.etEmailReceiver)) {
            Toast.makeText(this, "Enter receiver email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEmpty(binding.etEmailSubject)) {
            Toast.makeText(this, "Enter email subject", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEmpty(binding.etEmailMsg)) {
            Toast.makeText(this, "Enter email message", Toast.LENGTH_SHORT).show();
            return;
        }

        shareComposer.sendEmail("Send email to", getValidData(binding.etEmailMsg),
                getValidData(binding.etEmailSubject), getValidData(binding.etEmailReceiver).split(","));
    }

    private void shareImage() {
        easyImage = new EasyImage.Builder(ShareContentActivity.this)
                .setChooserTitle("Pick Images")
                .setCopyImagesToPublicGalleryFolder(true)
                .setFolderName("EasyImage")
//                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .allowMultiple(true)
                .build();
        easyImage.openChooser(this);
    }


    private void shareText() {
        if (isEmpty(binding.etText)) {
            Toast.makeText(this, "Enter some text to share", Toast.LENGTH_SHORT).show();
            return;
        }

        shareComposer.shareText("Share Test", getValidData(binding.etText), TextMimeType.HTML);
    }

    private boolean isEmpty(EditText editText) {
        return editText == null || TextUtils.isEmpty(editText.getText().toString().trim());
    }

    private String getValidData(EditText editText) {
        if (editText == null) {
            return "";
        } else {
            return editText.getText().toString().trim();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {

            @Override
            public void onMediaFilesPicked(@NonNull MediaFile[] imageFiles, @NonNull MediaSource source) {
                File[] files = new File[imageFiles.length];
                for (int i = 0; i < imageFiles.length; i++) {
                    files[i] = imageFiles[i].getFile();
                }
                shareComposer.shareFile("Share Files Test", files);
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }
}