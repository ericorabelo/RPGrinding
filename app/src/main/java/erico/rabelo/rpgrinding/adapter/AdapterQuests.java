package erico.rabelo.rpgrinding.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import erico.rabelo.rpgrinding.R;
import erico.rabelo.rpgrinding.model.Quest;

public class AdapterQuests extends RecyclerView.Adapter<AdapterQuests.MyViewHolder> {

    private List<Quest> quests;
    private Context context;

    public AdapterQuests(List<Quest> quests, Context context) {

        this.quests = quests;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_quest, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Quest quest = quests.get(position);
        holder.titulo.setText(quest.getNome());
        holder.descricao.setText(quest.getDescricao());

        // pega a primeira imagem da lista
        List<String> urlFoto = quest.getFoto();
        String urlDoCard = urlFoto.get(0);

        Picasso.get().load(urlDoCard).into(holder.foto);

    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titulo;
        TextView descricao;
        ImageView foto;

        public MyViewHolder(View itemView){
            super(itemView);
            titulo = itemView.findViewById(R.id.textViewTitulo);
            descricao = itemView.findViewById(R.id.textViewDescricao);
            foto = itemView.findViewById(R.id.imageViewQuest);
        }
    }

}
