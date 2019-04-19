package com.example.testknn.model.asyncs;

import com.example.testknn.model.DataPoint;

public class DistanceAlgorithm {
    public double calculateDistance(double x1, double y1, double x2, double y2) {
        double result, ycoord, xcoord;
        ycoord = Math.abs(y2 - y1);
        xcoord = Math.abs (x2-x1);
        result = Math.sqrt((ycoord) * (ycoord) + (xcoord) * (xcoord));
        System.out.println(result);
        return result;
    }
}
