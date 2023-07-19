package com.example.smartswitch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextView viewLampStatus;
    private EditText eTSetTime;
    private TextView tv_setTime;
    private Button btnSetTimer;

    public void showTimmer(){
        eTSetTime.setVisibility(View.VISIBLE);
        tv_setTime.setVisibility(View.VISIBLE);
    }
    public void hideTimmer(){
        eTSetTime.setVisibility(View.INVISIBLE);
        tv_setTime.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refData = database.getReference();
        DatabaseReference refDataType = database.getReference("type");

        viewLampStatus = findViewById(R.id.viewLampStatus);
        eTSetTime = findViewById(R.id.eTSetTime);
        tv_setTime = findViewById(R.id.tv_setTime);
        btnSetTimer = findViewById(R.id.btnSetTimer);
        ImageView imageViewLamp = findViewById(R.id.lampImage);

        Switch switchLamp = findViewById(R.id.SwitchLamp);

        Spinner spinner = findViewById(R.id.spinnerSet);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.timer_set, R.layout.spiner_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        refData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseModel firebaseModel = snapshot.getValue(FirebaseModel.class);
                boolean SwitchData = firebaseModel.isSwitch();
                boolean LampData = firebaseModel.isLamp();
                Integer TypeData = firebaseModel.getType();
                String TimeSetData = firebaseModel.getTimeSet();

                imageViewLamp.setImageResource(LampData? R.drawable.lamp : R.drawable.lamp_off);

                viewLampStatus.setText(LampData? "Hidup" : "Mati");
                switchLamp.setChecked(SwitchData);
                spinner.setSelection(TypeData);
                if(TypeData == 0){
                    hideTimmer();
                }else {
                    showTimmer();
                    eTSetTime.setText(TimeSetData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    hideTimmer();
                }else {
                    showTimmer();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        switchLamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                refData.child("Switch").setValue(b);
                Toast.makeText(getApplicationContext(), "Saklar Diubah", Toast.LENGTH_SHORT).show();
            }
        });

        btnSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Timer Diatur", Toast.LENGTH_SHORT).show();
                refData.child("Type").setValue(spinner.getSelectedItemPosition());
                if (spinner.getSelectedItemPosition()!= 0){
                    refData.child("TimeSet").setValue(eTSetTime.getText().toString());
                }else {
                    refData.child("TimeSet").setValue("");
                }
            }
        });



    }
}