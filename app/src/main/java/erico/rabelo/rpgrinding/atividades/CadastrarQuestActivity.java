package erico.rabelo.rpgrinding.atividades;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import erico.rabelo.rpgrinding.R;
import erico.rabelo.rpgrinding.config.ConfiguracaoFirebase;
import erico.rabelo.rpgrinding.model.Quest;
import erico.rabelo.rpgrinding.sup.Permissao;

public class CadastrarQuestActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText campoTituloQuest, campoDescricao, campoXP;

    private String [] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private ImageView imagem;
    String imagemEscolhida;

    Spinner campoDificuldade, campoHabilidade;

    private Quest quest;

    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_quest);

        //configuracoes firebase
        storage = ConfiguracaoFirebase.getFirebaseStorage();

        //validar as permissoes
        Permissao.validarPermissao(permissoes, this, 1);

        inicializarComponentes();

        carregarDadosSpinner();

    }

    public void salvarQuest(){
        salvarFotoStorage(imagemEscolhida);
    }

    private void salvarFotoStorage(String url){
        //cria no storage
        final StorageReference imagemQuest = storage
                .child("imagens")
                .child("quests")
                .child(quest.getIdQuest())
                .child("imagem");

        //upload do arquivo
        UploadTask uploadTask = imagemQuest.putFile(Uri.parse(url));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //recuperando a url da imagem
                imagemQuest.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri urlFirebase = task.getResult();
                        String urlConvertida = urlFirebase.toString();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                exibirMensagemErro("Falha no upload");
                Log.i("INFO","Falha ao fazer uploud, " + e.getMessage());
            }
        });

    }

    public Quest configurarQuest(){
        String dificuldade = campoDificuldade.getSelectedItem().toString();
        String habilidade = campoHabilidade.getSelectedItem().toString();
        String titulo = campoTituloQuest.getText().toString();
        String descricao = campoDescricao.getText().toString();
        String xp = campoXP.getText().toString();//getRawValue();

        Quest quest = new Quest();
        quest.setDificuldade(dificuldade);
        quest.setHabilidade(habilidade);
        quest.setNome(titulo);
        quest.setDescricao(descricao);
        quest.setXp(xp);

        return quest;
    }

    public void validarDadosQuest(View view){

        quest = configurarQuest();

        if(!quest.getFoto().isEmpty()){
            if(!quest.getDificuldade().isEmpty() ){
                if(!quest.getHabilidade().isEmpty()){
                    if(!quest.getNome().isEmpty()){
                        if (!quest.getXp().isEmpty()) {
                            if (!quest.getDescricao().isEmpty()) {
                                salvarQuest();
                            } else {
                                exibirMensagemErro("Preencha a descrição!");
                            }
                        }else{
                            exibirMensagemErro("Preencha o campo XP!");
                        }
                    }else{
                        exibirMensagemErro("Preencha o campo Titulo!");
                    }
                }else{
                    exibirMensagemErro("Selecione uma Habilidade!");
                }
            }else{
                exibirMensagemErro("Selecione uma Dificuldade!");
            }
        }else{
            exibirMensagemErro("Selecione uma foto!");
        }
    }

    private void exibirMensagemErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.imageView1:
                selecionarImagem(1);
                break;
        }

    }

    public void selecionarImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            //Recuperar Imagem
            Uri selecionarImagem = data.getData();
            String caminhoImagem = selecionarImagem.toString();

            //configura imagem no ImageView
            if(requestCode==1){
                imagem.setImageURI(selecionarImagem);
            }
            imagemEscolhida = caminhoImagem;
        }

    }

    private void carregarDadosSpinner(){
        String [] dificuldades = new String[]{
                "Fácil", "Média", "Dificil", "Extrema"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                dificuldades
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoDificuldade.setAdapter(adapter);

        String [] habilidades = new String[]{
                "Força", "Ágilidade", "Inteligencia", "Constituição"
        };
        ArrayAdapter<String> adapterHabilidade = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                habilidades
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campoHabilidade.setAdapter(adapterHabilidade);

    }

    private void inicializarComponentes(){
        campoTituloQuest = findViewById(R.id.editTextTituloQuest);
        campoDescricao = findViewById(R.id.editTextDescricaoQuest);
        campoXP = findViewById(R.id.editTextXPQuest);

        imagem = findViewById(R.id.imageView1);
        imagem.setOnClickListener(this);

        campoDificuldade = findViewById(R.id.spinnerDificuldade);
        campoHabilidade = findViewById(R.id.spinnerHabilidade);

    }

    //verificar se o usuario realmente autorizou a utilização da galeria
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if (permissaoResultado== PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }

    }

    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissão Negada");
        builder.setMessage("Para utulizar o app é necessario aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}