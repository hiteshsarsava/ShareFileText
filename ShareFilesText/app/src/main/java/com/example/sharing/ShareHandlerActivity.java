package com.example.sharing;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.sharing.handler.ShareHandler;
import com.sharing.handler.ShareModel;
import com.sharing.handler.TaskResult;

import java.util.List;

public class ShareHandlerActivity extends AppCompatActivity {

    TextView tvReceivedText;
    LinearLayout linImages;
    ShareHandler shareHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initViews();
    }

    private void initViews() {
        tvReceivedText = findViewById(R.id.tvReceivedText);
        linImages = findViewById(R.id.linImages);
        shareHandler = new ShareHandler();
        TaskResult taskResult = shareHandler.handle(ShareHandlerActivity.this, getIntent());
        if (taskResult.isSuccessFull()) {
            addImages(taskResult.getShareDataList());
        }
    }

    private void addImages(List<ShareModel> sharedData) {

        for (ShareModel shareModel : sharedData) {

            switch (shareModel.getShareType()) {

                case TEXT:
                    AppCompatTextView tvText = new AppCompatTextView(this);
                    tvText.setText(String.format("Shared text : %s", shareModel.getText()));

                    linImages.addView(tvText);
                    break;

                case AUDIO:
                    AppCompatImageView imageView = new AppCompatImageView(this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
                    Glide.with(this).load(R.drawable.ic_music).into(imageView);

                    AppCompatTextView tvPath = new AppCompatTextView(this);
                    tvPath.setText(String.format("Path : %s", shareModel.getFileUri().getPath()));

                    linImages.addView(imageView);
                    linImages.addView(tvPath);
                    break;

                case IMAGE:
                    imageView = new AppCompatImageView(this);
                    Glide.with(this).load(shareModel.getFileUri()).into(imageView);
//                    imageView.setImageURI(shareModel.getFileUri());

                    linImages.addView(imageView);
                    break;

                case VIDEO:
                    imageView = new AppCompatImageView(this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
                    Glide.with(this).load(shareModel.getFileUri()).into(imageView);

                    AppCompatImageView playerImage = new AppCompatImageView(this);
                    playerImage.setMaxHeight(50);
                    playerImage.setImageResource(android.R.drawable.ic_media_play);

                    FrameLayout frameLayout = new FrameLayout(this);

                    frameLayout.addView(imageView);
                    frameLayout.addView(playerImage);

                    linImages.addView(frameLayout);
                    break;

                case DOC:
                    imageView = new AppCompatImageView(this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
                    Glide.with(this).load(R.drawable.ic_doc).into(imageView);

                    tvPath = new AppCompatTextView(this);
                    tvPath.setText(String.format("Path : %s", shareModel.getFileUri().getPath()));

                    linImages.addView(imageView);
                    linImages.addView(tvPath);
                    break;
            }

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setMinimumHeight(20);
            linImages.addView(linearLayout);

        }
    }
}