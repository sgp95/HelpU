package com.sgp95.santiago.helpu;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sgp95.santiago.helpu.scanner.Scanner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.app.Activity.RESULT_OK;


public class CreateCompleinFragment extends Fragment {
    EditText edtComplain;
    ImageButton btnPicture;
    ImageView imgComplain,photo;
    Button btnSend;

    StorageReference storageReference;
    private Uri file;
    private static final String LOG_TAG = "Barcode Scanner API";
    private static final int PHOTO_REQUEST = 10;
    private Uri imageuri;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complein_create,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtComplain = (EditText) view.findViewById(R.id.edt_complain);
        btnPicture = (ImageButton) view.findViewById(R.id.btn_picture);
        imgComplain = (ImageView) view.findViewById(R.id.img_photo_camera);
        photo = (ImageView) view.findViewById(R.id.photo);
        btnSend = (Button) view.findViewById(R.id.btn_send_complain);

        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());




        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    btnPicture.setEnabled(false);
                    ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                }else{
                    takePicture();
                }

            }
        });

       /* btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgComplain.setImageBitmap(null);
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_PERMISSION);
                //ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_PERMISSION);
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnPicture.setEnabled(true);
            }
        }
    }


    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                photo.setImageURI(file);
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_WRITE_PERMISSION:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //!!!!!!!!!!
                    //Este metodo es el que bota el problema
                    //!!!!!!!!!!!
                    takePicture();
                }
                else{
                    Toast.makeText(getActivity(), "Permission Denied!"+requestCode, Toast.LENGTH_SHORT).show();
                }
        }
    } */

  /*   public void takePicture(){

       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Random random = new Random();
        int key =random.nextInt(1000);
        File photo = new File(Environment.getExternalStorageDirectory(),"picture"+key+".jpg");
        imageuri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
        startActivityForResult(intent,PHOTO_REQUEST);
    }*/

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK){
            launchMediaScanIntent();
            try{
                Scanner scanner = new Scanner();
                Bitmap bitmap = scanner.decodeBitmapUri(getActivity(),imageuri);
                imgComplain.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(getActivity(), "Failed to load Image", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, e.toString());
            }
        }

    }

    private void launchMediaScanIntent(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageuri);
        getActivity().sendBroadcast(mediaScanIntent);
    }*/
}
