package com.oscar.hernandez.oscarhernandez;

import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String SETTINGS = "settings";

    public static final String VALUE = "Value";
    public static final String NAME = "Name";
    public static final String DESCRIPTION = "Description";


    public static final String CHEAT_MODE = "enable_cheat_mode";
    public static final String WHITELIST = "enforce_whitelist";
    public static final String FLIGHT = "allow_flight";


    Adapter adapter;
    private RecyclerView settingsView;

    List<setting> settings = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    EventListener<QuerySnapshot> settingListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

            if (e != null) {
                Log.e("", e.getMessage());
                return;
            }
            settings.clear();

            for (DocumentSnapshot document : documentSnapshots) {

                String name = document.getString(NAME);
                boolean value = document.getBoolean(VALUE);
                String description = document.getString(DESCRIPTION);

                settings.add(new setting(name, value, description));
            }

            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadDefaultSettings();

        adapter = new Adapter();

        settingsView = findViewById(R.id.settingsView);
        settingsView.setLayoutManager(new LinearLayoutManager(this));
        settingsView.setAdapter(adapter);



        db.collection(SETTINGS).addSnapshotListener(settingListener);
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

    //Rooms listener

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView settingDescription;
        TextView settingValue;
        TextView bgChangeSetting;

        Switch viewSettingValue;

        boolean showSettingValue = false;

        public ViewHolder(View itemView) {
            super(itemView);

            settingDescription = itemView.findViewById(R.id.descriptionView);
            settingValue = itemView.findViewById(R.id.valueView);
            bgChangeSetting = itemView.findViewById(R.id.bgChangeSetting);
            viewSettingValue = itemView.findViewById(R.id.showInfo);

            viewSettingValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSettingValue = !showSettingValue;

                    adapter.notifyDataSetChanged();
                }
            });


        }
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder>
    {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.setting, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
        {
            setting setting = settings.get(position);

            holder.settingDescription.setText(setting.getDescription());

            if (holder.showSettingValue)
               holder.settingValue.setText(Boolean.toString(setting.isValue()));
            else
                holder.settingValue.setText("");

            holder.bgChangeSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.showSettingValue)
                    {
                        db.collection(SETTINGS).document(settings.get(position).getName()).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists())
                                        {
                                            Map<String, Object> newValue = documentSnapshot.getData();
                                            boolean realValue = !documentSnapshot.getBoolean(VALUE);
                                            newValue.put(VALUE, realValue);

                                            db.collection(SETTINGS).document(settings.get(position).getName()).set(newValue);

                                            Toast.makeText(MainActivity.this, settings.get(position).getDescription() + " Value changed from " + Boolean.toString(!realValue) + " to " +  Boolean.toString(realValue), Toast.LENGTH_SHORT).show();

                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return settings.size();
        }
    }
}
