package com.oscar.hernandez.oscarhernandez;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String SETTINGS = "settings";

    public static final String VALUE = "Value";
    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";


    public static final String CHEAT_MODE = "enable_cheat_mode";
    public static final String WHITELIST = "enforce_whitelist";
    public static final String FLIGHT = "allow_flight";


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadDefaultSettings();

    }

    private void LoadDefaultSettings() {
        Map<String, Object> nodeCheat = new HashMap<>();
        nodeCheat.put(NAME, CHEAT_MODE);
        nodeCheat.put(VALUE, false);
        nodeCheat.put(DESCRIPTION, "Enable Cheat Mode");

        Map<String, Object> nodeWhite = new HashMap<>();
        nodeWhite.put(NAME, WHITELIST);
        nodeWhite.put(VALUE, true);
        nodeWhite.put(DESCRIPTION, "Enforce Whitelist");

        Map<String, Object> nodeFlight = new HashMap<>();
        nodeFlight.put(NAME, FLIGHT);
        nodeFlight.put(VALUE, true);
        nodeFlight.put(DESCRIPTION, "Allow Flight");


        db.collection(SETTINGS).document(CHEAT_MODE).set(nodeCheat)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.e("", e.toString());
                    }
                });

        db.collection(SETTINGS).document(WHITELIST).set(nodeWhite)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.e("", e.toString());
                    }
                });

        db.collection(SETTINGS).document(FLIGHT).set(nodeFlight)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.e("", e.toString());
                    }
                });
    }

    void LoadFromDB()
    {
        final DocumentReference documentReference = db.collection("Test Collection").document("Test Document");

        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists())
                        {
                            String title = documentSnapshot.getString("Title");
                            String Description = documentSnapshot.getString("Description");

                            Map<String, Object> note = documentSnapshot.getData(); // All the data inside the document
                        }
                        else
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("", e.toString());
                    }
                });
    }
}
