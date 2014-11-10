package com.gaya.whoami.entities;

import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Collection;

/**
 * Created by Suri on 11/8/2014.
 */
@ParseClassName("question")
public class ParseQuestion extends ParseObject implements Question {
    @Override
    public String getId() {
        return getObjectId();
    }

    public String getText(){
        return getString("title");
    }
    public Collection<Answer> getAnswers(){
        return getList("answers");
    }
}
