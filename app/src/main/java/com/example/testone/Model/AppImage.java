package com.example.testone.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public class AppImage {
    File file;
    boolean isUpLoad;
    Bitmap bitmap;
  
    public AppImage() {
    }
  
    public AppImage(File file) {
        this.file=file;
        isUpLoad=true;
    }

    public AppImage(File file, boolean isUpLoad, Bitmap bitmap) {
        this.file = file;
        this.isUpLoad = isUpLoad;
        this.bitmap = bitmap;
    }
    public File getFile() {
        return file;
    }

    public void setFile(File uri) {
        this.file = file;
    }

    public boolean getIsUpLoad(){
        return isUpLoad;
    }

    public void setIsUpLoad(boolean isUpLoad){
        this.isUpLoad=isUpLoad;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
}
