package com.gaya.whoami.entities;

import com.gaya.whoami.Logger;
import com.gaya.whoami.players.User;
import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;
import com.parse.*;

import java.util.*;

@ParseClassName("_User")
public class ParseUser extends com.parse.ParseUser implements User {

    public static final SaveCallback SAVE_CALLBACK = new SaveCallback() {
        @Override
        public void done(ParseException e) {
            if (e != null)
                Logger.w(e);
            else
                Logger.d("Saved user");
        }
    };

    @Override
    public String getId() {
        return getObjectId();
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getImageUrl() {
        return getString("image");
    }

    public int getType() {
        return getInt("type");
    }

    @Override
    public Collection<Question> getUnansweredQuestions(Collection<Question> questions) {
        List<Question> questionList = new LinkedList<Question>();
        Map<String, String> answers = getMap("answers");
        if (answers == null) {
            questionList.addAll(questions);
            return questionList;
        }
        for (Question question : questions)
            if (!answers.containsKey(question.getId()))
                questionList.add(question);
        return questionList;
    }

    @Override
    public List<Question> getQuestions(Collection<? extends Question> questions) {

        List<Question> questionList = new LinkedList<Question>();
        Map<String, String> answers = getMap("answers");
        if (answers == null)
            return questionList;
        for (Question question : questions)
            if (answers.containsKey(question.getId()))
                questionList.add(question);
        return questionList;

    }

    @Override
    public int getAnsweredQuestions() {
        Map<String, String> answers = getMap("answers");
        if (answers == null)
            return 0;
        return answers.size();
    }

    public void setType(int type) {
        put("type", type);
    }

    public void setLink(String link) {
        put("link", link);
    }

    public String getLink() {
        return getString("link");
    }


    public void setImageUrl(String image) {
        put("image", image);
    }


    @Override
    public Answer getAnswer(Question question) {
        Map<String, String> answers = getMap("answers");
        if (answers == null)
            return null;
        String answer = answers.get(question.getId());
        if (answer == null)
            return null;
        for (Answer ans : question.getAnswers())
            if (ans.getId().equals(answer))
                return ans;
        return null;
    }


    @Override
    public void setAnswer(final Question question, Answer answer) {
        Map<String, String> answers = getMap("answers");
        if (answers == null)
            answers = new HashMap<String, String>();
        if (answer == null)
            answers.remove(question.getId());
        else
            answers.put(question.getId(), answer.getId());
        put("answers", answers);

        saveEventually(SAVE_CALLBACK);
    }

    @Override
    public void updateLocation() {

        ParseGeoPoint.getCurrentLocationInBackground(30000, new LocationCallback() {
            @Override
            public void done(ParseGeoPoint parseGeoPoint, ParseException e) {
                if (parseGeoPoint != null) {
                    put("location", parseGeoPoint);
                    put("lastSeen", new Date());
                    Logger.d("Updating user location");
                } else {
                    put("lastSeen", new Date());
                    Logger.d("Could not fetch location");
                    if (e != null)
                        Logger.e(e);
                }
                saveInBackground(SAVE_CALLBACK);
            }
        });
    }


}
