package com.jeongho.androiddemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jeongho.androiddemo.R;
import com.jeongho.androiddemo.base.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Jeongho on 16/7/3.
 */
public class CameraActivity extends BaseActivity{
    private static final int TAKE_PHOTO = 1;
    private static final int CROP_PHOTO = 2;

    private Button mTakePhotoBtn;
    private Button mChooseTaPhotoBtn;
    private ImageView mPhotoIv;
    private Uri mImageUri;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_camera);
    }

    @Override
    public void initView() {
        mTakePhotoBtn = (Button) findViewById(R.id.btn_take_photo);
        mTakePhotoBtn.setOnClickListener(this);
        mChooseTaPhotoBtn = (Button) findViewById(R.id.btn_choose_photo);
        mChooseTaPhotoBtn.setOnClickListener(this);
        mPhotoIv = (ImageView) findViewById(R.id.iv_photo);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
        if (outputImage.exists()){
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImageUri = Uri.fromFile(outputImage);

        if (v.getId() == R.id.btn_take_photo){
            takePhoto();
        }else if (v.getId() == R.id.btn_choose_photo){
            choosePhoto();
        }
    }

    private void choosePhoto() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, CROP_PHOTO);
    }

    private void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //把outputImage的唯一地址 指定为图片的输出地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public static void startAction(Context context) {
        Intent intent = new Intent(context, CameraActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(mImageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        mPhotoIv.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
