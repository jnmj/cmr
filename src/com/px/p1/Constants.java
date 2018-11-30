package com.px.p1;

import java.util.Random;

public class Constants {
    public static int M = 12;
    public static int K = 4;
    public static int Q = K;
    public static int r = 2;
    public static int PartSize = 1;
    public static int U = 100;
    public static int V = 16;

    public static int getNormrnd() {
        return (int) (new Random().nextGaussian()*Constants.V + Constants.U);
    }
}

