package com.gaya.whoami.questions;

import android.content.Context;
import com.gaya.whoami.JSONHelpers;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Lenovo-User on 12/08/2014.
 */
public abstract class JSONQuestionManager implements QuestionManager {
    private List<Question> questions = Collections.EMPTY_LIST;
    private final Context context;

    protected JSONQuestionManager(Context context) {
        this.context = context;
    }

    protected void loadQuestions(JSONObject jsonObject) {

        questions = (JSONHelpers.map(jsonObject.optJSONArray("questions"), new JSONHelpers.Factory<Object, Question>() {
            @Override
            public Question build(Object item) {
                if (item instanceof JSONObject)
                    return new JSONQuestion((JSONObject) item);
                else
                    return null;
            }
        }));
    }

    @Override
    public Collection<Question> getQuestions() {
        return questions;
    }

    @Override
    public Answer getAnswer(Question question) {
        return null;
    }

    @Override
    public void setAnswer(Question question, Answer answer) {

    }


    class JSONAnswer implements Answer {
        final String imageUrl;
        final String title;
        final String id;

        JSONAnswer(JSONObject jsonObject) {
            imageUrl = jsonObject.optString("thumb");
            title = jsonObject.optString("text");
            id = jsonObject.optString("id");
        }

        @Override
        public String getImageUrl() {
            return imageUrl;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getId() {
            return id;
        }
    }

    class JSONQuestion implements Question {
        final String id;
        final String text;
        final List<Answer> answers;

        JSONQuestion(JSONObject jsonObject) {
            id = jsonObject.optString("id");
            text = jsonObject.optString("title");
            answers = new ArrayList<Answer>();

            /*
            //Works the same as below
            answers.addAll(JSONHelpers.map(jsonObject.optJSONArray("answers"), new JSONHelpers.Factory<Object, Answer>() {
                @Override
                public Answer build(Object item) {
                    if (item instanceof JSONObject)
                        return new JSONAnswer((JSONObject) item);
                    return null;
                }
            }));
            */
            //get the array of answers
            JSONArray jsonAnswers = jsonObject.optJSONArray("answers");
            if (jsonAnswers != null) //make sure we have one
            {
                for (int i = 0; i < jsonAnswers.length(); i++) //iterate through answers
                {
                    JSONObject jsonAnswer = jsonAnswers.optJSONObject(i);
                    if (jsonAnswer == null) continue; //we only deal with JSONObjects
                    answers.add(new JSONAnswer(jsonAnswer)); //create new answer and add to collection
                }
            }


        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public Collection<Answer> getAnswers() {
            return answers;
        }
    }
}
