package erico.rabelo.rpgrinding.atividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erico.rabelo.rpgrinding.R;
import erico.rabelo.rpgrinding.adapter.AdapterQuests;
import erico.rabelo.rpgrinding.config.ConfiguracaoFirebase;
import erico.rabelo.rpgrinding.model.Quest;
import erico.rabelo.rpgrinding.sup.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerQuestsPrincipal;
    private Button buttonDificuldade, buttonHabilidade;

    private AdapterQuests adapterQuests;

    private List<Quest> listaQuests = new ArrayList<>();

    private DatabaseReference questsPrincipalRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarQuestsPrincipal();

        //configs firebase
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        questsPrincipalRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("quests");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("RPGrinding");
        setSupportActionBar(toolbar);//faz com q a toolbar tenhar suporte a versões antigas do android

        //configurar recyclerViewPrincipal
        recyclerQuestsPrincipal.setLayoutManager(new LinearLayoutManager(this));
        recyclerQuestsPrincipal.setHasFixedSize(true);
        adapterQuests = new AdapterQuests(listaQuests, this);
        recyclerQuestsPrincipal.setAdapter(adapterQuests);


        retornarQuestsPrincipal();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);

    }

    //recupera os itens de menu criados
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                deslogarUsuario();
                finish();
                break;
            case R.id.menuPerfil:
                abrirPerfil();
                break;
            case R.id.menuMinhasQuests:
                abrirMinhasQuests();
                break;

        }
        return super.onOptionsItemSelected(item);

    }

    public void inicializarQuestsPrincipal(){
        recyclerQuestsPrincipal = findViewById(R.id.recyclerQuestsPrincipal);
    }


    public void abrirPerfil(){
       Intent intent = new Intent(MainActivity.this, ConfiguracoesActivity.class);
       startActivity( intent );

    }

    public void deslogarUsuario() {
        try {
            autenticacao.signOut();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirMinhasQuests(){
        Intent intent = new Intent(MainActivity.this, MinhasQuestsActivity.class);
        startActivity( intent );
    }

    public void retornarQuestsPrincipal(){
        listaQuests.clear();
        questsPrincipalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dificuldades : snapshot.getChildren()){
                    for(DataSnapshot habilidades : dificuldades.getChildren()){
                        for(DataSnapshot quests : habilidades.getChildren()){
                            Quest quest = quests.getValue(Quest.class);
                            listaQuests.add(quest);
                            Collections.reverse(listaQuests);

                            adapterQuests.notifyDataSetChanged();

                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}