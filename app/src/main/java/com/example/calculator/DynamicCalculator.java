package com.example.calculator;

import android.content.Intent;

public class DynamicCalculator {

    /* how to guide:
        https://medium.com/@kalpeshchandora12/dynamic-code-loading-in-android-dea83ba3bc85
     */

    public static double add(double d1, double d2) {
        return d1 + d2;
    }

    public static double subtract(double d1, double d2) {
        return d2 - d1;
    }

//    public void startServ() {
//
//        startService(new Intent(this, MyService.class));
//    }
}
