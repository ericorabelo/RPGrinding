package erico.rabelo.rpgrinding.atividades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import erico.rabelo.rpgrinding.R;
import erico.rabelo.rpgrinding.model.Quest;

public class DetalhesQuestActivity extends AppCompatActivity {

    private ImageView imageViewDetalhe;
    private TextView textViewTituloDetalhe;
    private TextView textViewDescricaoDetalhe;
    private TextView textViewXpDetalhe;
    private TextView textViewDificuldadeDetalhe;
    private TextView textViewHabilidadeDetalhe;
    private Button buttonCompletarDetalhe;

    private Quest questSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_quest);



        //configurações do toolbar

        //inciando componentes da interface
        inicializarComponentes();

        //recuperar dados da quest do firebase
        questSelecionada = (Quest) getIntent().getSerializableExtra("questSelecionada");

        if(questSelecionada != null){
            textViewTituloDetalhe.setText(questSelecionada.getNome());
            textViewDescricaoDetalhe.setText(questSelecionada.getDescricao());
            textViewXpDetalhe.setText(questSelecionada.getXp());
            textViewDificuldadeDetalhe.setText(questSelecionada.getDificuldade());
            textViewHabilidadeDetalhe.setText(questSelecionada.getHabilidade());

            String urlString = questSelecionada.getFoto().get(0);
            Picasso.get().load(urlString).into(imageViewDetalhe);

        }

    }

    private void inicializarComponentes(){
        imageViewDetalhe = findViewById(R.id.imageViewDetalhe);
        textViewTituloDetalhe = findViewById(R.id.textViewTituloDetalhe);
        textViewDescricaoDetalhe = findViewById(R.id.textViewDescricaoDetalhe);
        textViewXpDetalhe = findViewById(R.id.textViewXpDetalhe);
        textViewDificuldadeDetalhe = findViewById(R.id.textViewDificuldadeDetalhe);
        textViewHabilidadeDetalhe = findViewById(R.id.textViewHabilidadeDetalhe);

    }
}