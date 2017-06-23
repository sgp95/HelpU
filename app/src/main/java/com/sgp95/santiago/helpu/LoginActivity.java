package com.sgp95.santiago.helpu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sgp95.santiago.helpu.model.User;

import static com.sgp95.santiago.helpu.R.id.btn_login;

public class LoginActivity extends AppCompatActivity {

    private EditText edtLoginCode, edtLoginPass;
    private Button btnLogin,btnPush;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        edtLoginCode = (EditText)findViewById(R.id.txt_login_code);
        edtLoginPass = (EditText)findViewById(R.id.txt_login_password);
        btnLogin = (Button)findViewById(btn_login);
        btnPush = (Button)findViewById(R.id.btn_push_user);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushUser();
            }
        });
    }

    public void loginUser(){
        String code = edtLoginCode.getText().toString();

        databaseReference.child("users").orderByChild("userCode").startAt(code).endAt(code).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Este log es para probar si obtengo el user
                Log.d("LoginActivity","user found: "+dataSnapshot.toString());
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
                Log.w("LoginActivity","LoginError",databaseError.toException());
            }
        });
    }
    public void pushUser(){
        String code = edtLoginCode.getText().toString();
        String pass = edtLoginPass.getText().toString();

        User user = new User(code,pass);

        databaseReference.child("users").setValue(user);
    }
}
