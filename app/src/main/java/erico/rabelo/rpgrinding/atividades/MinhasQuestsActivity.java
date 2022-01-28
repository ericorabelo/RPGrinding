package erico.rabelo.rpgrinding.atividades;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import erico.rabelo.rpgrinding.R;
import erico.rabelo.rpgrinding.adapter.AdapterQuests;
import erico.rabelo.rpgrinding.config.ConfiguracaoFirebase;
import erico.rabelo.rpgrinding.model.Quest;

public class MinhasQuestsActivity extends AppCompatActivity {

    private RecyclerView recyclerQuests;
    private List<Quest> quests = new ArrayList<>();
    private AdapterQuests adapterQuests;
    private DatabaseReference questUsuarioRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_quests);
        //configs firebase
        questUsuarioRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("minhas_quests")
                .child(ConfiguracaoFirebase.getIdUsuario());

        inicializarComponentes();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastrarQuestActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //configurar recyclerView
        recyclerQuests.setLayoutManager(new LinearLayoutManager(this));
        recyclerQuests.setHasFixedSize(true);
        adapterQuests = new AdapterQuests(quests, this);
        recyclerQuests.setAdapter(adapterQuests);

        //carregar a lista de quests
        carregarQuests();
    }

    public void carregarQuests(){
        questUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quests.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    quests.add(ds.getValue(Quest.class));
                }
                Collections.reverse(quests);
                adapterQuests.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void inicializarComponentes(){
        recyclerQuests = findViewById(R.id.recyclerQuests);
    }

}