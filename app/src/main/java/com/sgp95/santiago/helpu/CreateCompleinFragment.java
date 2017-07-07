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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sgp95.santiago.helpu.adapter.SpinnerAdapter;
import com.sgp95.santiago.helpu.model.Category;
import com.sgp95.santiago.helpu.model.Complain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class CreateCompleinFragment extends Fragment {
    EditText edtComplain;
    ImageButton btnPicture;
    ImageView imgComplain;
    Button btnSend;
    Switch swtPrivacy;
    Spinner spnCategorias,spnSedes;

    private String complainImgUrl,userCode;
    private FirebaseDatabase mFirebaseInstance;
    StorageReference storageReference;
    DatabaseReference databaseReference,complainRef;
    ProgressDialog progressDialog;
    DatabaseReference db;
    SpinnerAdapter helper;
    List<String> categories = null;
    ArrayList<String> sedes;
    ArrayAdapter<String> spnArrayAdapter;
    private static final int PICK_IMAGE_REQUEST = 20;
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
        spnCategorias = (Spinner) view.findViewById(R.id.spnCategorias);
        spnSedes = (Spinner) view.findViewById(R.id.spn_sedes);
        swtPrivacy.setChecked(true);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        complainRef = databaseReference.child("complaint");

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

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE_REQUEST);


            }
        });

        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference ref = database.getReference("category");
        categories = new ArrayList<>();
        databaseReference.child("category").orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Category category = dataSnapshot.getValue(Category.class);
                categories.add(category.getName());
                    spnArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,categories);
                    spnArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnCategorias.setAdapter(spnArrayAdapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            // ...
        });

        populateSpinnerSedes();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushKey = complainRef.push().getKey();
                if(filePath != null) {

                    progressDialog.show();

                    StorageReference stRef = storageReference.child(pushKey);
                    UploadTask uploadTask = stRef.putFile(filePath);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") Uri imgUrl = taskSnapshot.getDownloadUrl();
                            complainImgUrl = imgUrl.toString();
                            //sendCompalin
                            sendComaplin(pushKey,complainImgUrl);
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
                    progressDialog.show();
                    sendComaplin(pushKey,"null");
                    Log.e("filePath","filePath vacio");
                }
            }
        });
    }

    public void sendComaplin(String pushKey,String complainImgUrl){
        Complain comment = new Complain();
        comment.setComplain(edtComplain.getText().toString());
        comment.setComplainImage(complainImgUrl);
        comment.setComplaintId(pushKey);
        comment.setDateCreated(getDateCreated());
        comment.setUserCode(userCode);
        comment.setState(COMPLAIN_STATE);
        comment.setPrivacy(getPrivacyState());
        comment.setCategory(spnCategorias.getSelectedItem().toString());

        complainRef.child(pushKey).setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"Queja enviada satisfactoriamente",Toast.LENGTH_SHORT).show();
                goToCommentsFragment(userCode);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"No se pudo enviar la queja",Toast.LENGTH_SHORT).show();
                Log.e("ErrorSentComplain", "Upload Failed -> " + e);
            }
        });
    }

    public void goToCommentsFragment(String userCode){

    }

    public void populateSpinnerSedes(){
        sedes = new ArrayList<>();
        databaseReference.child("sedes").orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                sedes.add(String.valueOf(dataSnapshot.getValue()));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,sedes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnSedes.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
