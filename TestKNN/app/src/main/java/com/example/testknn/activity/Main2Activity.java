package com.example.testknn.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.example.testknn.MainActivity;
import com.example.testknn.R;
import com.example.testknn.cavas.DataPointCanvas;
import com.example.testknn.constant.Constants;
import com.example.testknn.model.Category;
import com.example.testknn.model.DataPoint;
import com.example.testknn.model.asyncs.Classifier;
import com.example.testknn.model.asyncs.DistanceAlgorithm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    private final int REQUEST_CODE = 101;

    private DistanceAlgorithm[] distanceAlgorithms = {new DistanceAlgorithm()};
    private Classifier classifier;
    private List<DataPoint> listDataPoint = new ArrayList<>();
    private List<DataPoint> listDataPointOriginal = new ArrayList<>();

    private DataPointCanvas dataPointCanvas;
    private Button buttonTune;
    private Button buttonPredict;
    private Button buttonReset;
    private TextView textViewAccuracy;
    private String TAG="MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        classifier = new Classifier();

        populateList();
        setupUI();
        setupCanvas();
        setButtonTuneListener();
        setButtonPredictListener();
        setButtonResetListener();
    }

    private void setupUI(){
        dataPointCanvas = findViewById(R.id.dataPointCanvas);
        buttonTune = findViewById(R.id.buttonTune);
        buttonPredict = findViewById(R.id.buttonPredict);
        buttonReset = findViewById(R.id.buttonReset);
        textViewAccuracy = findViewById(R.id.textViewAccuracy);
    }
    private void setButtonTuneListener(){
        buttonTune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void setButtonPredictListener(){
        buttonPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classifier.classify();
                textViewAccuracy.setText("Accuracy = " + classifier.getAccuracy());
                dataPointCanvas.invalidate();
            }
        });
    }
    private void setButtonResetListener(){
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classifier.reset();
                listDataPoint.clear();
                listDataPoint.addAll(listDataPointOriginal);
                classifier.setListDataPoint(listDataPoint);
                dataPointCanvas.invalidate();
                buttonPredict.setEnabled(false);
            }
        });

    }
    private void setupCanvas(){
        dataPointCanvas.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dataPointCanvas.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dataPointCanvas.setListDataPoints(listDataPoint);
            }
        });
    }
    private void populateList() {
        try{
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(getAssets().open
                    ("points.txt")));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] point = line.split(",");
                double x = Double.parseDouble(point[0]);
                double y = Double.parseDouble(point[1]);
                int category = Integer.parseInt(point[2]);
                DataPoint dataPoint = new DataPoint(x, y, Category.values()[category]);
                listDataPointOriginal.add(new DataPoint(dataPoint));
                listDataPoint.add(dataPoint);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }

        Log.d(TAG, ""+listDataPoint.size());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (data == null)
                return;
            Bundle bundle = data.getExtras();
            if (bundle == null)
                return;
            int spinnerIndex = bundle.getInt(Constants.DISTANCE_ALGORITHM);
            int K = bundle.getInt(Constants.K);
            double splitRatio = bundle.getDouble(Constants.SPLITE_RATIO);

//            DistanceAlgorithm distanceAlgorithm = distanceAlgorithms[spinnerIndex];
//            if (distanceAlgorithm instanceof MinkowskiDistance){
//                int p = bundle.getInt(Constants.MINKOWSKI_P);
//                (distanceAlgorithm).setP(p);
//            }
            classifier.reset();
            classifier.setDistanceAlgorithm(distanceAlgorithms[spinnerIndex]);
            classifier.setK(K);
            classifier.setSplitRatio(splitRatio);
            classifier.setListDataPoint(listDataPoint);
            classifier.splitData();
            listDataPoint.clear();
            listDataPoint.addAll(classifier.getListTestData());
            listDataPoint.addAll(classifier.getListTrainData());
            dataPointCanvas.invalidate();
            buttonPredict.setEnabled(true);
        }
    }
}
