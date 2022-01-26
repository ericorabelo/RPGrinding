package erico.rabelo.rpgrinding.sup;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import erico.rabelo.rpgrinding.config.ConfiguracaoFirebase;
import erico.rabelo.rpgrinding.model.Usuario;

public class UsuarioFirebase {


    public static String getIndentificadorUsuario() {
        //retornar o identificador do usuario dono da imagem
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String identificadorUsuario = usuario.getCurrentUser().getUid();

        return identificadorUsuario;
    }

    //retornar um usuario com todos os dados
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome){

        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome");

                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean atualizarFotoUsuario(Uri url){
        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar foto");

                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseUser = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());

        if(firebaseUser.getPhotoUrl()==null){
            usuario.setFoto("");
        }
        else{
            usuario.setFoto(firebaseUser.getPhotoUrl().toString());
        }
        return usuario;
    }

}
