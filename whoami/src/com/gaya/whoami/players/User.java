package com.gaya.whoami.players;

import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;

import java.util.Collection;

/**
 * Created by Lenovo-User on 12/08/2014.
 */
public interface User {
    Collection<Question> getAnsweredQuestions();
    Answer getAnswer(Question question);
}
