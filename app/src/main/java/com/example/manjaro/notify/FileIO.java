package com.example.manjaro.notify;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileIO {
    public static void writeArrayListToFile(ArrayList<String> al, String filename, Context context){
        try{
            File directory = context.getFilesDir();
            File file = new File(directory, filename);

            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(al);
            oos.close();
        }catch(IOException f){
            Log.e("ERR",f.toString());
        }
    }

    public static ArrayList<String> readArrayListFromFile(String filename, Context context) throws FileNotFoundException {
        ArrayList al=new ArrayList();
        try {
            File directory = context.getFilesDir();
            File file = new File(directory, filename);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            al= (ArrayList) ois.readObject();
            ois.close();
        }catch(IOException e){
            throw new FileNotFoundException(e.toString());
        }catch(ClassNotFoundException c){}//readObject()


        return al;
    }
}
