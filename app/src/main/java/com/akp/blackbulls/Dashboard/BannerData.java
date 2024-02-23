package com.akp.blackbulls.Dashboard;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class BannerData implements Serializable {
    String BannerImg;

    public String getBannerImg() {
        return BannerImg;
    }

    public void setBannerImg(String BannerImg) {
        this.BannerImg = BannerImg;
    }

    public static List<BannerData> createJsonInList(String str){
        Gson gson=new Gson();
        Type type=new TypeToken<List<BannerData>>(){
        }.getType();
        return gson.fromJson(str,type);
    }
}

