package com.gaya.whoami.questions;

import android.content.Context;
import com.gaya.whoami.JSONHelpers;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lenovo-User on 12/08/2014.
 * implementation of @see QuestionManager that reads a json file from the apps assets
 */
public class AssetsQuestionManager extends JSONQuestionManager {
    public AssetsQuestionManager(Context context) {
        super(context);

        try {
            //load file from assets
            InputStream inputStream = context.getAssets().open("questions.json");
            //load questions from json
            loadQuestions(JSONHelpers.getJson(inputStream));

            inputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
