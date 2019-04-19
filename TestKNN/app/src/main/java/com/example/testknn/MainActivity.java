package com.example.testknn;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.testknn.activity.Main2Activity;
import com.example.testknn.constant.Constants;

public class MainActivity extends AppCompatActivity {
    private EditText editTextK;
    private EditText editTextSplitRatio;
    private Spinner spinnerDistance;
    private EditText editTextMinkowski;
    private Button buttonTune;
    private Button buttonCancel;

    private LinearLayout linearLayoutMinkowskiP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        setSpinnerListener();
        setButtonTuneListener();
        setButtonCancelListener();
    }

    private void setupUI(){
        editTextK = findViewById(R.id.editTextK);
        editTextSplitRatio = findViewById(R.id.editTextSplitRatio);
        spinnerDistance = findViewById(R.id.spinnerDistance);
        editTextMinkowski = findViewById(R.id.editTextMinkowski);
        linearLayoutMinkowskiP = findViewById(R.id.linearLayoutMinkowskiP);
        buttonTune = findViewById(R.id.buttonTune);
        buttonCancel = findViewById(R.id.buttonCancel);
    }
    private void setSpinnerListener() {
        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)spinnerDistance.getSelectedItem();
                if(item.equalsIgnoreCase("MINKOWSKI")){
                    linearLayoutMinkowskiP.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutMinkowskiP.setVisibility(View.GONE);
                    editTextMinkowski.clearFocus();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setButtonTuneListener(){
        buttonTune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canTune()){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage("Make sure to fill all the fields")
                            .setPositiveButton("Ok", null)
                            .create()
                            .show();
                    return;
                }

                int spinnerIndex = getSpinnerPosition(spinnerDistance);
                int K = Integer.parseInt(getText(editTextK));
                double splitRatio = Double.parseDouble(getText(editTextSplitRatio));

                Bundle bundle = new Bundle();
                bundle.putInt(Constants.DISTANCE_ALGORITHM, spinnerIndex);
                bundle.putInt(Constants.K, K);
                bundle.putDouble(Constants.SPLITE_RATIO, splitRatio);

                if (spinnerIndex == 2){
                    int p = Integer.parseInt(getText(editTextMinkowski));
                    bundle.putInt(Constants.MINKOWSKI_P, p);
                }

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void setButtonCancelListener(){
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private int getSpinnerPosition(Spinner spinner){
        return spinner.getSelectedItemPosition();
    }

    private String getText(EditText editText){
        return editText.getText().toString().trim();
    }

    private boolean isEmpty(EditText editText){
        return getText(editText).length() == 0;
    }

    private boolean canTune(){
        if (isEmpty(editTextSplitRatio))
            return false;
        if (isEmpty(editTextK))
            return false;
        if (getSpinnerPosition(spinnerDistance) == 2)
            if (isEmpty(editTextMinkowski))
                return false;
        return true;
    }
}
