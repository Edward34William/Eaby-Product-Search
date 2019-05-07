package com.example.aninterface;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DataHolder {
    public ArrayList<resultItems> myWishItems = new ArrayList<>();
    public Context context;

    public DataHolder(Context context) {
        this.myWishItems = readWish(context);
        this.context = context;
    }

    public ArrayList<resultItems> getMyWishItems() {
        return myWishItems;
    }

    public void setMyWishItems(ArrayList<resultItems> myWishItems, Context context) {
        this.myWishItems = myWishItems;
        saveWish(context, this.myWishItems);
    }

    public ArrayList<resultItems> readWish(Context context){
        SharedPreferences localWishList = context.getSharedPreferences("localWishList", MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<ArrayList<resultItems>>() {}.getType();
        String jsonRead = localWishList.getString("localWishList", gson.toJson(new ArrayList<resultItems>()));
        ArrayList<resultItems> myWishItems = gson.fromJson(jsonRead, itemType);
        return myWishItems;
    }

    public void saveWish(Context context, ArrayList<resultItems> myWishItems){
        SharedPreferences localWishList = context.getSharedPreferences("localWishList", MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<ArrayList<resultItems>>() {}.getType();
        SharedPreferences.Editor edit = localWishList.edit();
        String jsonWrite = gson.toJson(myWishItems, itemType);
        edit.putString("localWishList", jsonWrite);
        edit.commit();
    }

    public resultItems readItem(Context context){
        SharedPreferences Item = context.getSharedPreferences("Item", MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<resultItems>() {}.getType();
        String jsonRead = Item.getString("Item", "");
        resultItems myItem = gson.fromJson(jsonRead, itemType);
        return myItem;
    }

    public void saveItem(Context context, resultItems item){
        SharedPreferences Item = context.getSharedPreferences("Item", MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<resultItems>() {}.getType();
        SharedPreferences.Editor edit = Item.edit();
        String jsonWrite = gson.toJson(item, itemType);
        edit.putString("Item", jsonWrite);
        edit.commit();
    }

    public searchStructure readSearch(Context context){
        SharedPreferences search = context.getSharedPreferences("Search", MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<searchStructure>() {}.getType();
        String jsonRead = search.getString("Item", "");
        searchStructure mySearch = gson.fromJson(jsonRead, itemType);
        return mySearch;
    }

    public void saveSearch(Context context, searchStructure mySearch){
        SharedPreferences search = context.getSharedPreferences("Search", MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<searchStructure>() {}.getType();
        SharedPreferences.Editor edit = search.edit();
        String jsonWrite = gson.toJson(mySearch, itemType);
        edit.putString("Search", jsonWrite);
        edit.commit();
    }

    public ArrayList<String> readStringArrayList(Context context, String Str){
        SharedPreferences str = context.getSharedPreferences(Str, MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<ArrayList<String>>() {}.getType();
        String jsonRead = str.getString(Str, gson.toJson(new ArrayList<String>(), itemType));
        ArrayList<String> myStr = gson.fromJson(jsonRead, itemType);
        return myStr;
    }

    public void saveStringArrayList(Context context, ArrayList<String> myStr, String Str){
        SharedPreferences str = context.getSharedPreferences(Str, MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<ArrayList<String>>() {}.getType();
        SharedPreferences.Editor edit = str.edit();
        String jsonWrite = gson.toJson(myStr, itemType);
        edit.putString(Str, jsonWrite);
        edit.commit();
    }

    public String readString(Context context, String Str){
        SharedPreferences str = context.getSharedPreferences(Str, MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<String>() {}.getType();
        String jsonRead = str.getString(Str, "");
        String myStr = gson.fromJson(jsonRead, itemType);
        return myStr;
    }

    public void saveString(Context context, String myStr, String Str){
        SharedPreferences str = context.getSharedPreferences(Str, MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<String>() {}.getType();
        SharedPreferences.Editor edit = str.edit();
        String jsonWrite = gson.toJson(myStr, itemType);
        edit.putString(Str, jsonWrite);
        edit.commit();
    }

    public double readDouble(Context context, String DB){
        SharedPreferences db = context.getSharedPreferences(DB, MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<Double>() {}.getType();
        String jsonRead = db.getString(DB, "");
        double myDb = gson.fromJson(jsonRead, itemType);
        return myDb;
    }

    public void saveDouble(Context context, double myDb, String DB){
        SharedPreferences db = context.getSharedPreferences(DB, MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<Double>() {}.getType();
        SharedPreferences.Editor edit = db.edit();
        String jsonWrite = gson.toJson(myDb, itemType);
        edit.putString(DB, jsonWrite);
        edit.commit();
    }

    public String getDomain(){
        return "http://10.0.2.2:8080/"; //backend RESTApi url, Modified required
    }

    public ArrayList<simItem> readSim(Context context){
        SharedPreferences localSimList = context.getSharedPreferences("localSimList", MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<ArrayList<simItem>>() {}.getType();
        String jsonRead = localSimList.getString("localSimList", gson.toJson(new ArrayList<simItem>()));
        ArrayList<simItem> mySimItems = gson.fromJson(jsonRead, itemType);
        return mySimItems;
    }

    public void saveSim(Context context, ArrayList<simItem> mySimItems){
        SharedPreferences localSimList = context.getSharedPreferences("localSimList", MODE_PRIVATE);
        Gson gson = new Gson();
        Type itemType = new TypeToken<ArrayList<simItem>>() {}.getType();
        SharedPreferences.Editor edit = localSimList.edit();
        String jsonWrite = gson.toJson(mySimItems, itemType);
        edit.putString("localSimList", jsonWrite);
        edit.commit();
    }


}
