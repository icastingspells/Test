package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.koteki.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textView;
    DatabaseAdapter databaseAdapter;
    ProfileAdapter profileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list_item);
        textView = findViewById(R.id.ttv);
        FloatingActionButton btn = findViewById(R.id.floatingActionButton);
        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleCallback);
        touchHelper.attachToRecyclerView(recyclerView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfile(v);
            }
        });
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            switch (direction){
                case ItemTouchHelper.LEFT:{
                    Profile deletedProfile = databaseAdapter.getSingleProfile((long) viewHolder.itemView.getTag());
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Удаление")
                            .setMessage("Вы уверены " + deletedProfile.name + "?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseAdapter.delete(deletedProfile._id);
                                    onResume();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onResume();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
                }
                case ItemTouchHelper.RIGHT:{
                    editProfile(recyclerView, (long)viewHolder.itemView.getTag());
                    //-----------------------
                    break;
                }
            }
        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    protected void onResume() {
        databaseAdapter = new DatabaseAdapter(MainActivity.this);
        super.onResume();
        databaseAdapter.open();
        profileAdapter = new ProfileAdapter(MainActivity.this, databaseAdapter.profiles());
        recyclerView.setAdapter(profileAdapter);
        textView.setText("Найдено пользователей: " + profileAdapter.getItemCount());
        profileAdapter.notifyDataSetChanged();
    }
    public void addProfile(View view) {
        Dialog addDialog = new Dialog(this, R.style.Theme_Koteki);
        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100,0,0,0)));
        addDialog.setContentView(R.layout.add_profile_dialog);

        EditText dialogName = addDialog.findViewById(R.id.personName);
        EditText dialogAge = addDialog.findViewById(R.id.personAge);
        addDialog.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = dialogName.getText().toString();
                int age = Integer.parseInt(dialogAge.getText().toString());
                databaseAdapter.insert(new Profile(name, age));
                Toast.makeText(MainActivity.this, "Данные добавлены!", Toast.LENGTH_SHORT).show();
                onResume();
                addDialog.hide();
            }
        });
        addDialog.show();
    }
    public void editProfile(View view, long id) {
        Dialog editDialog = new Dialog(this, R.style.Theme_Koteki);
        editDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100,0,0,0)));
        editDialog.setContentView(R.layout.add_profile_dialog);

        EditText dialogName = editDialog.findViewById(R.id.personName);
        EditText dialogAge = editDialog.findViewById(R.id.personAge);

        Profile profile = databaseAdapter.getSingleProfile(id);
        dialogName.setText(profile.name);
        dialogAge.setText(String.valueOf(profile.age));

        editDialog.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = dialogName.getText().toString();
                int age = Integer.parseInt(dialogAge.getText().toString());

                databaseAdapter.update(new Profile(id, name, age));
                onResume();
                Toast.makeText(MainActivity.this, "Изменения внесены!", Toast.LENGTH_SHORT).show();

                editDialog.hide();
            }
        });
        editDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}