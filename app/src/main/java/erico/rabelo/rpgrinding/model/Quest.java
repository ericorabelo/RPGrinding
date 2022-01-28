package erico.rabelo.rpgrinding.model;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import erico.rabelo.rpgrinding.config.ConfiguracaoFirebase;

public class Quest {
    private String idQuest;
    private String dificuldade;
    private String habilidade;
    private String nome;
    private String descricao;
    private String xp;
    private List<String> foto;

    public Quest() {
        DatabaseReference questRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("minhas_quests");
        setIdQuest(questRef.push().getKey());
    }

    public void salvar(){

        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference questRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("minhas_quests");

        questRef.child(idUsuario)
                .child(getIdQuest())
                .setValue(this);


    }

    public void deletar(){

        String idUsuario = ConfiguracaoFirebase.getIdUsuario();
        DatabaseReference questRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("minhas_quests")
                .child(idUsuario)
                .child(getIdQuest());

        questRef.removeValue();
    }

    public String getIdQuest() {
        return idQuest;
    }

    public void setIdQuest(String idQuest) {
        this.idQuest = idQuest;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(String habilidade) {
        this.habilidade = habilidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getXp() {
        return xp;
    }

    public void setXp(String xp) {
        this.xp = xp;
    }

    public List<String> getFoto() {
        return foto;
    }

    public void setFoto(List<String> foto) {
        this.foto = foto;
    }
}
