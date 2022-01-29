package erico.rabelo.rpgrinding.atividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

    String filtroDificuldade = "";
    String filtroHabilidade = "";

    private boolean filtrandoPorDificuldade = false;

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

        recyclerQuestsPrincipal.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this, recyclerQuestsPrincipal,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Quest questSelecionada = listaQuests.get(position);
                                Intent i = new Intent(MainActivity.this, DetalhesQuestActivity.class);
                                i.putExtra("questSelecionada", questSelecionada);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

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


                        }
                    }
                }
                Collections.reverse(listaQuests);

                adapterQuests.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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



    //filtro 1 - vai ser chamado pelo botão
    public void filtrarPorDificuldade(View view){
        AlertDialog.Builder dialogDificuldade = new AlertDialog.Builder(this);
        dialogDificuldade.setTitle("Escolha uma dificuldade");

        // spinner, pega o layout e converte em view

        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        Spinner spinnerDificuldade = viewSpinner.findViewById(R.id.spinnerFiltro);

        //snipper dificuldade
        String [] dificuldades = new String[]{
                "Fácil", "Média", "Dificil", "Extrema"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item,
                dificuldades
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDificuldade.setAdapter(adapter);


        dialogDificuldade.setView(viewSpinner);

        dialogDificuldade.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filtroDificuldade = spinnerDificuldade.getSelectedItem().toString();
                recuperarQuestsPordificuldade();
                filtrandoPorDificuldade = true;
            }
        });

        dialogDificuldade.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = dialogDificuldade.create();
        dialog.show();

    }

    public void recuperarQuestsPordificuldade(){
        questsPrincipalRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("quests")
                .child(filtroDificuldade);

        questsPrincipalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaQuests.clear();
                    for(DataSnapshot habilidades : snapshot.getChildren()){
                        for(DataSnapshot quests : habilidades.getChildren()){
                            Quest quest = quests.getValue(Quest.class);
                            listaQuests.add(quest);


                        }
                    }

                Collections.reverse(listaQuests);

                adapterQuests.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //filtro 2 - vai ser chamado pelo botão
    public void filtrarPorHabilidade(View view){

        if(filtrandoPorDificuldade == true){
            AlertDialog.Builder dialogHabilidade = new AlertDialog.Builder(this);
            dialogHabilidade.setTitle("Escolha uma Habilidade");

            // spinner, pega o layout e converte em view

            View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
            Spinner spinnerHabilidade = viewSpinner.findViewById(R.id.spinnerFiltro);

            //snipper dificuldade
            String [] dificuldades = new String[]{
                    "Força", "Agilidade", "Inteligencia", "Constituição"
            };
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item,
                    dificuldades
            );
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHabilidade.setAdapter(adapter1);


            dialogHabilidade.setView(viewSpinner);

            dialogHabilidade.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    filtroHabilidade = spinnerHabilidade.getSelectedItem().toString();
                    recuperarQuestsPorHabilidade();
                }
            });

            dialogHabilidade.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog dialog = dialogHabilidade.create();
            dialog.show();
        }else{
            Toast.makeText(this, "escolha primeiro uma dificuldade",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void recuperarQuestsPorHabilidade(){
        questsPrincipalRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("quests")
                .child(filtroDificuldade)
                .child(filtroHabilidade);

        questsPrincipalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaQuests.clear();
                for(DataSnapshot quests : snapshot.getChildren()){
                    Quest quest = quests.getValue(Quest.class);
                    listaQuests.add(quest);

                }

                Collections.reverse(listaQuests);

                adapterQuests.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}