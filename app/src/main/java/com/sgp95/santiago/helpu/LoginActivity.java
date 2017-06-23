package com.sgp95.santiago.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.sgp95.santiago.helpu.R.id.btn_login;

public class LoginActivity extends AppCompatActivity {

    private EditText edtLoginCode, edtLoginPass;
    private Button btnLogin,btnPush;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

        edtLoginCode = (EditText)findViewById(R.id.txt_login_code);
        edtLoginPass = (EditText)findViewById(R.id.txt_login_password);
        btnLogin = (Button)findViewById(btn_login);
        btnPush = (Button)findViewById(R.id.btn_push_user);

        auth = FirebaseAuth.getInstance();
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
        String pass = edtLoginPass.getText().toString();

        if(TextUtils.isEmpty(code)){
            Toast.makeText(getApplicationContext(),"Ingrese correo ejmplo: ****@utp.edu.pe",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(),"Ingrese contraseña",Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(code,pass)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Login fallido verifique correo y contraseña",Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
    public void pushUser(){
    }
}
