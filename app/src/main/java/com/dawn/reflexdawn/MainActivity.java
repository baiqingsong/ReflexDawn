package com.dawn.reflexdawn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Class classInteger = Integer.TYPE;
        Class classBoolean = Boolean.TYPE;
        Class classFloat = Float.TYPE;
        try {
            Class classString = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        initModel();
        initModelReflex();
    }
    private void initModel(){
        Model model = Model.getInstance("dawn", 1, true, 1.0f);
        Model model2 = new Model("dawn", 1, true, 1.0f);
        String jsonModel = new GsonBuilder().create().toJson(model);
        String jsonModel2 = new GsonBuilder().create().toJson(model2);
        Log.i("dawn", "json model = " + jsonModel);
        Log.i("dawn", "json model2 = " + jsonModel2);
    }
    private void initModelReflex(){
        try {
            Class<?> classModel = Class.forName("com.dawn.reflexdawn.Model");
            Method getInstance = classModel.getMethod("getInstance", new Class[]{
                    Class.forName("java.lang.String"),Integer.TYPE, Boolean.TYPE, Float.TYPE});
            Object model = getInstance.invoke(classModel, new Object[]{"dawn", 1, true, 1.0f});
            String jsonModel = new GsonBuilder().create().toJson(model);
            Log.i("dawn", "reflex json model = " + jsonModel);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Class<?> classModel2 = Class.forName("com.dawn.reflexdawn.Model");
            Constructor<?> constructor = classModel2.getConstructor(new Class[]{
                    Class.forName("java.lang.String"),Integer.TYPE, Boolean.TYPE, Float.TYPE});
            Object model2 = constructor.newInstance(new Object[]{"dawn", 1, true, 1.0f});
            String jsonModel2 = new GsonBuilder().create().toJson(model2);
            Log.i("dawn", "reflex json model2 = " + jsonModel2);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
