package com.sgp95.santiago.helpu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.util.concurrent.ExecutionException;

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

        auth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Validar();
            }
        });

       /* btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushUser();
            }
        }); */
    }

    public   void Validar()
    {
        new asyncValidateStudent().execute();
    }


    public String email() {return edtLoginCode.getText().toString();}
    public String password() {return edtLoginPass.getText().toString();}

    class asyncValidateStudent extends AsyncTask<String,Void,Void> {
        private ProgressDialog progressDialog;

        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this, "Validando Datos!!", "Espere unos segundos!!", true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String email = email();
                String pass = password();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Ingrese correo ejmplo: ****@utp.edu.pe",Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(getApplicationContext(),"Ingrese contraseña",Toast.LENGTH_SHORT).show();
                }

                auth.signInWithEmailAndPassword(email,pass)
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
            }catch (Exception e){

            }
            return null;
        }
    }

}
