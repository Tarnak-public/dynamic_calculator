package com.example.calculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DynamicClass";
    private EditText firstInput;
    private EditText secondInput;
    private TextView resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstInput = findViewById(R.id.first_input_et);
        secondInput = findViewById(R.id.second_input_et);
        resultTv = findViewById(R.id.result_tv);

        try {
            Log.v(TAG,"Load class from assets");
            FileUtilities.copyFromAssetsToAppPrivate(this,"app-debug.apk");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if (i[0] > getVersion()) {
        loadDynamicClasses();
        //}else {
        //    loadDynamicClasses();
        //}

    }


    private void loadDynamicClasses() {
        new Thread() {
            @Override
            public void run() {
                new LoadDynamicClasses(MainActivity.this).loadClassesFromApk();
            }
        }.start();
    }

    public void add(View view) {
        if (LoadDynamicClasses.getDymnamicClass() != null) {
            try {
                int firstNum = Integer.parseInt(firstInput.getText().toString());
                int secondNum = Integer.parseInt(secondInput.getText().toString());
                Method addMethod = LoadDynamicClasses.getDymnamicClass().getMethod("add", double.class, double.class);
                double result = (double) addMethod.invoke(null, firstNum, secondNum);
                resultTv.setText(String.valueOf(result));
                Log.d(TAG, "callDynamicClasses: addResult: " + result);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePrefs(int version) {
        SharedPreferences preferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        preferences.edit().putInt("version", version).apply();
    }

    private int getVersion() {
        return getSharedPreferences("my_prefs", Context.MODE_PRIVATE).getInt("version", -1);
    }

}
