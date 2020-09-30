package com.example.appmobileamazongamer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText campoemail, camposenha;
    private Button botaoLogin;
    private ProgressBar progressBar;
    private CheckBox campovisualizar;
    private TextView esqueceu;


    private ClasseCadastro usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Inicializarcomponentes();

        //Fazer o Login do Usuário
        progressBar.setVisibility(View.GONE);
        botaoLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String TEmail = campoemail.getText().toString();
                String TSenha = camposenha.getText().toString();

                if( !TEmail.isEmpty() ){

                    if( !TSenha.isEmpty() ){
                        usuario = new ClasseCadastro();
                        usuario.setEmail(TEmail);
                        usuario.setSenha(TSenha);
                        validarLogin( usuario );

                    }else{
                        Toast.makeText(Login.this, "Preencha a Senha!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Login.this, "Preencha o E-mail!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        campovisualizar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    camposenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    camposenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        esqueceu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, EsqueceuSenha.class));
            }
        });

    }

    private void validarLogin(ClasseCadastro usuario) {

        progressBar.setVisibility(View.VISIBLE);
        autenticacao = CofiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), Perfil.class));
                    finish();
                }else{
                    Toast.makeText(Login.this, "Erro ao fazer o Login!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void Inicializarcomponentes() {
        campoemail = (EditText)findViewById(R.id.email);
        camposenha = (EditText)findViewById(R.id.senha);
        botaoLogin = (Button)findViewById(R.id.btnLogin);
        progressBar = (ProgressBar)findViewById(R.id.progressLogin);
        campovisualizar = (CheckBox)findViewById(R.id.visualizar);
        esqueceu = (TextView)findViewById(R.id.txtEsqueceu);
    }

    public void Cadastro(View view){
        Intent Cadastro = new Intent(this, Cadastro.class);
        startActivity(Cadastro);
    }

    public void Senha(View view){
        Intent EsqueceuSenha = new Intent(this, com.example.appmobileamazongamer.EsqueceuSenha.class);
        startActivity(EsqueceuSenha);
    }
}