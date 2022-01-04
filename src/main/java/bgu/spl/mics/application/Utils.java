package bgu.spl.mics.application;

import bgu.spl.mics.Input;
import bgu.spl.mics.application.passiveObjects.Diary;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class Utils {


    public static Input readJson(String inputString){

        Gson gson = new Gson();
        Input input= new Input();
        try (Reader reader = new FileReader(inputString)) {
            input = gson.fromJson(reader, Input.class);
        } catch (Exception e) {e.getStackTrace();}

        return input;
    }
    public static void WriteJson(String outputName , Diary diary) throws IOException {
        Gson gson = new Gson();
        FileWriter write =new  FileWriter(outputName);
        gson.toJson(diary,write);
        write.close();
    }


}
