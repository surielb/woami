package com.gaya.whoami.entities;

import com.gaya.whoami.questions.Answer;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Suri on 11/8/2014.
 */
@ParseClassName("answer")
public class ParseAnswer extends ParseObject implements Answer {
    public String getText(){
        return getString("text");
    }

    @Override
    public String getId() {
        return getObjectId();
    }

    public String getImage(){
        return getString("image");
    }
}
