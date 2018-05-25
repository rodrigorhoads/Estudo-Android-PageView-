package com.meuteste.testejenkins;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {

    public static Bitmap getScaleBitmap(String path,int larguraDest,int alturaDest){
        //ler a dimensoes da imagem no disco
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);


        float srcLargura=options.outWidth;
        float srcAltura=options.outHeight;

        //Descobre quanto precisa reduzir a escala
        int inSampleSize=1;
        if(srcAltura>alturaDest||srcLargura>larguraDest){
            float escalaAltura=srcAltura/alturaDest;
            float escalaLargura=srcLargura/larguraDest;

            inSampleSize=Math.round(escalaAltura>escalaLargura?escalaAltura:escalaLargura);
        }

        options= new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;

        //ler e criar o bitmap final

        return BitmapFactory.decodeFile(path,options);
    }

    public static Bitmap getScaleBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaleBitmap(path,size.x,size.y);
    }

}
