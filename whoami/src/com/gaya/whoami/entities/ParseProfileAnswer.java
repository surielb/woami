package com.gaya.whoami.entities;

import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;
import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Suri on 11/8/2014.
 */
@ParseClassName("ProfileAnswer")
public class ParseProfileAnswer extends ParseObject {
    public ParseQuestion getQuestion() {
        return (ParseQuestion) getParseObject("question");
    }

    public ParseAnswer getAnswer() {
        return (ParseAnswer) getParseObject("answer");
    }

    public static ParseProfileAnswer create(ParseQuestion question, ParseAnswer answer) {
        ParseProfileAnswer parseProfileAnswer = new ParseProfileAnswer();
        parseProfileAnswer.put("question", question);
        parseProfileAnswer.put("answer", answer);
        return parseProfileAnswer;
    }

    public static ParseProfileAnswer create(Question question, Answer answer) {
        ParseProfileAnswer parseProfileAnswer = new ParseProfileAnswer();
        if(question instanceof ParseQuestion)
            parseProfileAnswer.put("question", question);
        else
            parseProfileAnswer.put("question", ParseObject.createWithoutData("question", question.getId()));
        if(answer instanceof ParseObject)
            parseProfileAnswer.put("answer", answer);
        else
            parseProfileAnswer.put("answer", ParseObject.createWithoutData("answer", answer.getId()));
        return parseProfileAnswer;
    }
}
