package com.example.jorge.hellojesus.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.jorge.hellojesus.data.local.WordsRepository;
import com.example.jorge.hellojesus.data.local.helloWord.HelloWord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.jorge.hellojesus.util.KeyVar.storage;

public class CrunchifyJSONFileWrite {

    static Context mContext;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void main(Context context,WordsRepository mWordsRepository) throws IOException {
        mContext = context;

        arquivo_Txt_word("helloword", mWordsRepository);


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void arquivo_Txt_word(String nomearquivo, WordsRepository mWordsRepository ){




        StringBuilder text = new StringBuilder();
        try {

            File file = new File(storage, nomearquivo + ".txt");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            List<HelloWord> data = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                text.append(line);
                String name = line.substring(0,line.indexOf("_"));
                String tip1 = line.substring(line.indexOf("_") + 1, line.indexOf("*"));
                String tip2 = line.substring(line.indexOf("*") + 1, line.indexOf("@"));
                String tip3 = line.substring(line.indexOf("@") + 1, line.length());
                HelloWord helloWord = new HelloWord(name,tip1,tip2,tip3);

                mWordsRepository.saveHelloWord(helloWord);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {

        }
    }


}
