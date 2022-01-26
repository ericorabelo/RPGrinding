package erico.rabelo.rpgrinding.atividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import erico.rabelo.rpgrinding.R;
import erico.rabelo.rpgrinding.sup.Permissao;

public class CadastrarQuestActivity extends AppCompatActivity {

    private EditText campoTituloQuest, campoDescricao, campoXP;

    private String [] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_quest);

        //validar as permissoes
        Permissao.validarPermissao(permissoes, this, 1);

        inicializarComponentes();

    }

    public void salvarQuest(View view){
        String titulo = campoTituloQuest.getText().toString();

    }

    private void inicializarComponentes(){
        campoTituloQuest = findViewById(R.id.editTextTituloQuest);
        campoDescricao = findViewById(R.id.editTextDescricaoQuest);
        campoXP = findViewById(R.id.editTextXPQuest);
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