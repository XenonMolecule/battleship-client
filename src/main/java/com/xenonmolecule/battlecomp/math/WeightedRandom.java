package com.xenonmolecule.battlecomp.math;

import java.util.Random;

public class WeightedRandom {

    double[] chances;

    public WeightedRandom(int... weight) {
        int totalW = 0;
        for(int i = 0; i < weight.length; i ++) {
            totalW += weight[i];
        }
        double lastChance = 0.0;
        chances = new double[weight.length];
        for(int j = 0; j < weight.length; j ++) {
            chances[j] = lastChance + (weight[j]/totalW);
            lastChance += (weight[j]/totalW);
        }
    }

    public WeightedRandom(double... weight) {
        double totalW = 0.0;
        for (int i = 0; i < weight.length; i ++) {
            totalW += weight[i];
        }
        double lastChance = 0.0;
        chances = new double[weight.length];
        for(int j = 0; j < weight.length; j ++) {
            chances[j] = lastChance + (weight[j]/totalW);
            lastChance += (weight[j]/totalW);
        }
    }

    public int generateRandom() {
        return binarySearch(0, chances.length, Math.random());
    }

    // Not exactly traditional binary search, finds index of range that number falls in
    private int binarySearch(int start, int end, double val) {
        if(start == end)
            return start;
        int mid = (start + end) / 2;
        if(start+1 == end) {
            if (chances[start] < val) {
                return end;
            } else {
                return start;
            }
        }
        if(chances[mid] > val)
            return binarySearch(start, mid, val);
        else if (chances[mid] < val)
            return binarySearch(mid, end, val);
        else
            return mid;
    }

}
