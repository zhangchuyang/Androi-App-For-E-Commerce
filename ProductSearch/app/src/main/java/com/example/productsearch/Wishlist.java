package com.example.productsearch;


import android.util.Log;

import java.util.ArrayList;

public class Wishlist {

    public static ArrayList<String> wish_ImageName = new ArrayList<>();
    public static ArrayList<String> wish_Image  = new ArrayList<>();
    public static ArrayList<String> wish_PostalCode  = new ArrayList<>();
    public static ArrayList<String> wish_Condition = new ArrayList<>();
    public static ArrayList<String> wish_Price = new ArrayList<>();
    public static ArrayList<String> wish_Wish  = new ArrayList<>();
    public static ArrayList<String> wish_Shipping  = new ArrayList<>();
    public static ArrayList<String> wish_Id = new ArrayList<>() ;

    public String getSizeWish(){
        return Integer.toString(wish_Id.size());
    }

    public int getIntSizeWish(){
        return wish_Id.size();
    }

    public ArrayList<String> getWish_ImageName(){
        return wish_ImageName;
    }

    public ArrayList<String> getWish_Image(){
        return wish_Image;
    }
    public ArrayList<String> getWish_PostalCode(){
        return wish_PostalCode;
    }

    public ArrayList<String> getWish_Condition(){
        return wish_Condition;
    }

    public ArrayList<String> getWish_Price(){
        return wish_Price;
    }

    public ArrayList<String> getWish_Wish(){
        return wish_Wish;
    }

    public ArrayList<String> getWish_Shipping(){
        return wish_Shipping;
    }

    public ArrayList<String> getWish_Id(){
        return wish_Id;
    }

    public boolean checkExistByIdString(String currIdString){
        for (int i = 0;i < wish_Id.size();i++){
            if (wish_Id.get(i).equals(currIdString)){
            return true;
            }
        }
        return false;
    }

    public void addWish(String imageName, String image, String postalCode, String condition, String price, String wish, String shipping, String id){

        Log.i("imageAME", imageName);

        wish_Id.add(id);
        wish_ImageName.add(imageName);
        wish_Image.add(image);
        wish_Condition.add(condition);
        wish_PostalCode.add(postalCode);
        wish_Wish.add(wish);
        wish_Shipping.add(shipping);
        wish_Price.add(price);


    }

    //DELETE WISH

    public void deleteWish(String id){
        int index = wish_Id.indexOf(id);
        wish_Wish.remove(index);
        wish_Price.remove(index);
        wish_Shipping.remove(index);
        wish_PostalCode.remove(index);
        wish_Condition.remove(index);
        wish_Image.remove(index);
        wish_ImageName.remove(index);
        wish_Id.remove(index);

    }




}
