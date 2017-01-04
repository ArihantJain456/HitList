package com.example.arihantjain.hitlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.Manifest;

public class AddEnemyActivity extends AppCompatActivity {
    Button addEnemyBtn;
    ImageButton addEnemyImageBtn;
    EnemyModel enemyModel;
    EditText enemyName,enemyPlace,enemyReason,enemyHouse;
    RadioGroup enemyAliveOrDead;
    private static final int GALLERY_PIC_CODE = 101;
    private static final int CAMERA_CODE = 102;
    private static final int READ_EXTERNAL_PERMISSION_CODE_GALLERY= 201;
    private static final int READ_EXTERNAL_PERMISSION_CODE_CAMERA= 2020;
    private static final int CAMERA_PERMISSION_CODE = 2021;
    byte[] enemyImage;
    EnemyModel enemyModelOld;
    String code;
    Bitmap yourSelectedEnemy;
    AryaPermission aryaPermission;
    String cameraPictureLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        code = intent.getStringExtra("code");

        setContentView(R.layout.activity_add_enemy);
        enemyName = (EditText)findViewById(R.id.enemy_name);
        enemyPlace = (EditText)findViewById(R.id.enemy_place);
        enemyReason = (EditText)findViewById(R.id.enemy_reason);
        enemyHouse = (EditText)findViewById(R.id.enemy_house);
        enemyAliveOrDead = (RadioGroup)findViewById(R.id.enemy_life);
        enemyModel = new EnemyModel();
        addEnemyBtn = (Button) findViewById(R.id.enemy_addBtn);
        aryaPermission = new AryaPermission(this);
        addEnemyImageBtn = (ImageButton) findViewById(R.id.enemy_image_addBtn);
        addEnemyBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendEnemyToMainActivity();
                    }
                }
        );
        addEnemyImageBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] imageMode = {"Gallery", "Camera"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddEnemyActivity.this);
                        builder.setTitle("Choose Image")
                                .setItems(imageMode, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(AddEnemyActivity.this, "" + which, Toast.LENGTH_LONG).show();
                                        if(which==0){
                                            galleryPicture();
                                        }
                                        else {
                                            takePicture();
                                        }
                                    }
                                });
                        builder.show();
                    }
                }
        );
        if(code.equals("U")){
            enemyModelOld = (EnemyModel) intent.getSerializableExtra("enemyEdit");
            setDataForEditing();
        }
    }
    public void sendEnemyToMainActivity(){
                enemyModel.setPlaceToFind(enemyPlace.getText() + "");
        enemyModel.setName(enemyName.getText() + "");
        enemyModel.setReason(enemyReason.getText() + "");
        enemyModel.setHouse(enemyHouse.getText() + "");

        int selectedChoice = enemyAliveOrDead.getCheckedRadioButtonId();
        enemyModel.setAlive(selectedChoice==(R.id.enemy_dead)?"No":"Yes");
        enemyModel.setImage(enemyImage);
        if(enemyModel.getImage()!=null){
            System.out.println("yes null");
        }
        if(enemyImage!=null){
            System.out.println("yes null");
        }
        if(!enemyModel.getName().equals("")){
            Intent enemyIntent = new Intent();
            enemyIntent.putExtra("enemyData",enemyModel);
            setResult(RESULT_OK,enemyIntent);
            finish();
        }
        else {
            Toast.makeText(this,"Empty ",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            System.out.println("setting image");
            switch (requestCode) {
                case GALLERY_PIC_CODE:
                    Uri uri = data.getData();
                    System.out.println("setting image");
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    yourSelectedEnemy = BitmapFactory.decodeFile(filePath);
                    addEnemyImageBtn.setImageBitmap(yourSelectedEnemy);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourSelectedEnemy.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    enemyImage = stream.toByteArray();
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CAMERA_CODE: rotateImage(setPic());
                    Bitmap bitmap = rotateImage(setPic());
                    addEnemyImageBtn.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream2);
                    enemyImage = stream2.toByteArray();
            }
        }
    }

    private Bitmap setPic() {
        // Get the dimensions of the View
        int targetW = addEnemyImageBtn.getWidth();
        int targetH = addEnemyImageBtn.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(cameraPictureLocation, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        return BitmapFactory.decodeFile(cameraPictureLocation, bmOptions);
    }
    public Bitmap rotateImage(Bitmap bitmap) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(cameraPictureLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }


    public void takePicture(){
        if(Build.VERSION.SDK_INT >=23){

            if(!aryaPermission.checkPermissionFor(android.Manifest.permission.CAMERA)){
                aryaPermission.requestPermissionFor(android.Manifest.permission.CAMERA,CAMERA_PERMISSION_CODE);
            }
            else {
                if(!aryaPermission.checkPermissionFor(android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    aryaPermission.requestPermissionFor(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            READ_EXTERNAL_PERMISSION_CODE_CAMERA);
                }
                else {
                    cameraIntent();
                }
            }
        }
        else {
            cameraIntent();
        }
    }
    public void galleryPicture(){
        if(Build.VERSION.SDK_INT >=23){
            if(!aryaPermission.checkPermissionFor(android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                aryaPermission.requestPermissionFor(android.Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXTERNAL_PERMISSION_CODE_GALLERY);
            }
            else {
                    galleryIntent();
            }
        }
        else {
            galleryIntent();
        }
    }
    private void cameraIntent() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
        startActivityForResult(camera,CAMERA_CODE);
    }
    private void galleryIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,GALLERY_PIC_CODE);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "ENEMY_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDirectory);
        cameraPictureLocation = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                // If request is cancelled, the result arrays are empty.
                if(aryaPermission.checkPermissionFor(android.Manifest.permission.READ_EXTERNAL_STORAGE))
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    cameraIntent();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            case READ_EXTERNAL_PERMISSION_CODE_GALLERY:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                galleryIntent();
                    return;
            }
            case READ_EXTERNAL_PERMISSION_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    cameraIntent();
                } else {

                }
                return;
        }

    }
    public void setDataForEditing(){
        addEnemyBtn.setText("Update");
        enemyModel.setID(enemyModelOld.getID());
        enemyName.setText(enemyModelOld.getName());
        enemyPlace.setText(enemyModelOld.getPlaceToFind());
        enemyReason.setText(enemyModelOld.getReason());
        enemyHouse.setText(enemyModelOld.getHouse());
        enemyImage = enemyModelOld.getImage();
        if(enemyModelOld.getImage()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(enemyModelOld.getImage(),0,enemyModelOld.getImage().length);
            addEnemyImageBtn.setImageBitmap(bitmap);
        }
        if(enemyModelOld.getAlive().equals("Yes")){
            enemyAliveOrDead.check(R.id.enemy_alive);
        }
        else {
            enemyAliveOrDead.check(R.id.enemy_dead);
        }
    }
}
