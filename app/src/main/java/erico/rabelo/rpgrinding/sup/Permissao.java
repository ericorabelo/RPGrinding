package erico.rabelo.rpgrinding.sup;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {
    public static boolean validarPermissao(String[] permissoes, Activity activity, int requestCode){
        if(Build.VERSION.SDK_INT > 23){
            List<String> listaPermissoes = new ArrayList<>();
            //percorre uma lista de permissoes e verifica se ja tem a permissao liberada
            for (String permissao : permissoes){
               Boolean temPermissao =  ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
               if(!temPermissao){
                   listaPermissoes.add(permissao);
               }
            }
            //se a lista de permissoes esta vazia, não precisa solicitar permissões
            if(listaPermissoes.isEmpty()) return true;
            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //solicita permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }

        return true;
    }
}
