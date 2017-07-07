package com.sgp95.santiago.helpu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sgp95.santiago.helpu.model.Comment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class CreateCompleinFragment extends Fragment {
    EditText edtComplain;
    ImageButton btnPicture;
    ImageView imgComplain;
    Button btnSend;
    Switch swtPrivacy;

    private String complainImgUrl,userCode;

    StorageReference storageReference;
    DatabaseReference databaseReference,complainRef,commentRef;
    ProgressDialog progressDialog;
    private static final int PICK_IMAGE_REQUEST = 20;
    //private static final int REQUEST_WRITE_PERMISSION = 21;
    private static final String COMPLAIN_STATE = "Pendiente";
    Uri filePath;
    String pushKey;

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
        imgComplain = (ImageView) view.findViewById(R.id.img_photo);
        btnSend = (Button) view.findViewById(R.id.btn_send_complain);
        swtPrivacy = (Switch) view.findViewById(R.id.swt_privacy);
        swtPrivacy.setChecked(true);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        complainRef = databaseReference.child("complaint");
        //commentRef = databaseReference.child("comment");

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading...");

        TextView textView = (TextView) getActivity().findViewById(R.id.user_code_header);
        userCode = textView.getText().toString();

        swtPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    swtPrivacy.setText("Publico");
                    Toast.makeText(getActivity(),"Publico: todos los usuarios veran esta queja",Toast.LENGTH_SHORT).show();
                }else{
                    swtPrivacy.setText("Privado");
                    Toast.makeText(getActivity(),"Privado: solo tu y el administrador podran ver la queja",Toast.LENGTH_SHORT).show();
                }
            }
        });


       btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_PERMISSION);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE_REQUEST);


            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(filePath != null) {

                    progressDialog.show();

                    pushKey = complainRef.push().getKey();
                    StorageReference stRef = storageReference.child(pushKey);
                    UploadTask uploadTask = stRef.putFile(filePath);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri imgUrl = taskSnapshot.getDownloadUrl();
                            complainImgUrl = imgUrl.toString();
                            Comment comment = new Comment();
                            comment.setComplain(edtComplain.getText().toString());
                            comment.setComplainImage(complainImgUrl);
                            comment.setComplaintId(pushKey);
                            comment.setDateCreated(getDateCreated());
                            comment.setUserCode(userCode);
                            comment.setState(COMPLAIN_STATE);
                            comment.setPrivacy(getPrivacyState());

                            complainRef.child(pushKey).setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),"Queja enviada satisfactoriamente",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Log.e("filePath", "Upload Failed -> " + e);
                        }
                    });

                    Log.d("push", pushKey);
                }
                else {
                    Log.e("filePath","filePath vacio -->"+filePath.toString());
                }
            }
        });
    }
    public String getDateCreated(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = dateFormat.format(calendar.getTime());

        return date;
    }

    public String getPrivacyState(){
        String state;
        if(swtPrivacy.isChecked()){
            state = "Publico";
        }
        else {
            state = "Privado";
        }
        return state;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!= null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);

                imgComplain.setImageBitmap(bitmap);
                imgComplain.setVisibility(View.VISIBLE);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
