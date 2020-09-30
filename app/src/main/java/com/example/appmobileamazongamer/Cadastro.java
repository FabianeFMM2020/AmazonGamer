package com.example.appmobileamazongamer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Cadastro extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button btnCadastrar;
    private ProgressBar progressBar;
    private CheckBox verSenha;

    private ClasseCadastro usuario;

    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Inicializarcomponentes();


        progressBar.setVisibility(View.GONE);
        btnCadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String TextoNome = campoNome.getText().toString();
                String TextoEmail = campoEmail.getText().toString();
                String TextoSenha = campoSenha.getText().toString();

                if( !TextoNome.isEmpty() ){
                    if( !TextoEmail.isEmpty() ){
                        if( !TextoSenha.isEmpty() ){

                            usuario = new ClasseCadastro();
                            usuario.setNome( TextoNome );
                            usuario.setEmail( TextoEmail );
                            usuario.setSenha( TextoSenha );
                            cadastrar( usuario );

                        }else{
                            Toast.makeText(Cadastro.this, "Preencha Senha!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Cadastro.this, "Preencha Email!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Cadastro.this, "Preencha seu nome!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        verSenha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    campoSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    campoSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }

    private void cadastrar(ClasseCadastro usuario) {

        autenticacao = CofiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful() ){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Cadastro.this, "Cadastro com sucesso", Toast.LENGTH_SHORT).show();

                            startActivity( new Intent(getApplicationContext(), Perfil.class));
                            finish();

                        }else{

                            progressBar.setVisibility(View.GONE);

                            String erroExcecao = "";
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e ){
                                erroExcecao = "Digite uma senha mais forte!";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erroExcecao = "Por favor, digite um e-mail válido";
                            }catch (FirebaseAuthUserCollisionException e){
                                erroExcecao = "Esta conta ja foi cadastrada!";
                            }catch (Exception e){
                                erroExcecao = "ao cadastrar usuario: " +e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(Cadastro.this, "Erro: " +erroExcecao, Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );
    }


    private void Inicializarcomponentes() {
        campoNome = (EditText)findViewById((R.id.txtNome));
        campoEmail = (EditText)findViewById(R.id.txtEmail);
        campoSenha = (EditText)findViewById(R.id.txtSenha);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);
        progressBar = (ProgressBar)findViewById(R.id.progressLogin);
        verSenha = (CheckBox)findViewById(R.id.Visualizar);

    }

    public void Voltar(View view){
        Intent MainActivity = new Intent(this, com.example.appmobileamazongamer.MainActivity.class);
        startActivity(MainActivity);
    }
}