package erico.rabelo.rpgrinding.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    private static DatabaseReference database;
    private static FirebaseAuth auth;

    //retorna a instancia do firebaseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        if(database==null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    //retora a instacia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(auth==null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
