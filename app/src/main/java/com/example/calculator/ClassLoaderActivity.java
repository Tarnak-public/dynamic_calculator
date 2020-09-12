package com.example.calculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

//
// https://gist.github.com/Tarnak-public/9d1c980000a65900ee62d10d20e161d8
//
// !!WARNING: Not recommended for production code!!
//
public class ClassLoaderActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState) {
        // file.jar has a dex'd "classes.dex" entry that you can generate with "dx" from any number of JARs or class files
        super.onCreate(savedInstanceState);
        ClassLoader dexLoader = new DexClassLoader("/path/to/file.jar", getCacheDir().getAbsolutePath(), null, getClassLoader());
        setAPKClassLoader(dexLoader);

        try {
            Class<?> activityClass = dexLoader.loadClass("com.company.MyActivity");
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finish();
    }

    private void setAPKClassLoader(ClassLoader classLoader)
    {
        try {
            Field mMainThread = getField(Activity.class, "mMainThread");
            Object mainThread = mMainThread.get(this);
            Class threadClass = mainThread.getClass();
            Field mPackages = getField(threadClass, "mPackages");

            HashMap<String,?> map = (HashMap<String,?>) mPackages.get(mainThread);
            WeakReference<?> ref = (WeakReference<?>) map.get(getPackageName());
            Object apk = ref.get();
            Class apkClass = apk.getClass();
            Field mClassLoader = getField(apkClass, "mClassLoader");

            mClassLoader.set(apk, classLoader);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Field getField(Class<?> cls, String name)
    {
        for (Field field: cls.getDeclaredFields())
        {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

}