package com.example.appmobileamazongamer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class Perfil extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Configuração do Tollbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Amazon Gamer");
        setSupportActionBar(toolbar);

        //Configuração de objetos
        autenticacao = CofiguracaoFirebase.getFirebaseAutenticacao();

        //Configuracao do Botão de Navegação
        configuraBottomNavigationView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();

    }

    /** MÉTODO RESPONSÁVEL POR CRIAR O BOTÃO DE NAVEGAÇÃO
     */
    private void configuraBottomNavigationView(){

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);

        //faz configuracoes iniciais do bottom navigation
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(true);
        bottomNavigationViewEx.setTextVisibility(true);

        //Habilitar navegação
        habilitarNavegacao( bottomNavigationViewEx );

        //Configura Item Selecionado
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

    }

    //Método Responsável por tratar eventos de click na BottomNavigation
    private void habilitarNavegacao(BottomNavigationViewEx viewEx) {

        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()){

                    case R.id.perfil:
                    fragmentTransaction.replace(R.id.viewPager, new PerfilFragment()).commit();
                    return true;

                    case R.id.adicionar_jogo:
                        fragmentTransaction.replace(R.id.viewPager, new AdicionarJogoFragment()).commit();
                        return true;

                    case R.id.homegames:
                        fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();
                        return true;

                    case R.id.pesquisar_jogos:
                        fragmentTransaction.replace(R.id.viewPager, new PesquisarFragment()).commit();
                        return true;

                    case R.id.chat:
                        fragmentTransaction.replace(R.id.viewPager, new ChatFragment()).commit();
                        return true;
                }

                return false;
            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_sair :
                deslogarUsuari();
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuari() {
        try {
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}