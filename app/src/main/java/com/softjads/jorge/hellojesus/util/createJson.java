package com.softjads.jorge.hellojesus.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.softjads.jorge.hellojesus.data.onLine.topic.model.Topic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class createJson {

    public static String storage = "/storage/emulated/0/";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void main(Context context) throws IOException {

        arquivo_Txt_Mattew("Mark");


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void arquivo_Txt_Mattew(String nomearquivo){



        StringBuilder text = new StringBuilder();
        try {

            File file = new File(storage, nomearquivo + ".txt");

            FileInputStream ostream = new FileInputStream(file);
          //  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(ostream, "UTF-8"));

            //BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedReader br = new BufferedReader(new InputStreamReader(ostream, "ISO-8859-1"));
            String line;

            JSONArray items = new JSONArray();

            List<Topic> data = new ArrayList<>();
            int i = 0;
            int ii = 0;

            Topic topic = new Topic();
            JSONArray itemsContent = new JSONArray();

            JSONObject obj = new JSONObject();


            while ((line = br.readLine()) != null) {
                text.append(line);

                i ++;
                String temp = "";
                String id = line.substring(0,line.indexOf("*"));


                temp = line.substring(line.indexOf("*")+1,line.length());
                String Content_english = temp.substring(0,temp.indexOf("*"));
                temp = temp.substring(temp.indexOf("*")+1,temp.length());
                String Content_portugues = temp.substring(0,temp.indexOf("*"));
                temp = temp.substring(temp.indexOf("*")+1,temp.length());
                String audio = temp.substring(0,temp.indexOf("*"));
                temp = temp.substring(temp.indexOf("*")+1,temp.length());
                int time = Integer.parseInt(temp.substring(0,temp.indexOf("*")));
                temp = temp.substring(temp.indexOf("*")+1,temp.length());
                int timestep = Integer.parseInt(temp);



                if (Content_english.equals("0")) {

                    ii ++;
                    i=0;
                    obj = new JSONObject();
                    obj.put("id", id);
                    obj.put("name", nomearquivo + " " + Integer.toString(ii));
                    obj.put("audio", audio);
                    obj.put("type", "bible");
                    obj.put("time", time);
                    obj.put("youtube", "youtube " + nomearquivo);
                    obj.put("glossary", "glossory " + nomearquivo);
                    obj.put("content", itemsContent);
                    itemsContent = new JSONArray();
                    items.add(obj);

                }else{


                    JSONObject objContent = new JSONObject();
                    objContent.put("id_content", i);
                    objContent.put("content_english",  Content_english);
                    objContent.put("content_portuguese", Content_portugues);
                    objContent.put("time", timestep);
                    objContent.put("correct_option","0");
                    objContent.put("option",null);
                    itemsContent.add(objContent);
                }


            }



             escreveJson(items,nomearquivo);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void escreveJson(JSONArray items, String partNameFile){

        JSONObject obj = new JSONObject();

        obj.put("items", items);

        // try-with-resources statement based on post comment below :)
        try (FileWriter file = new FileWriter(storage + partNameFile + "file.txt")) {
            file.write(obj.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
