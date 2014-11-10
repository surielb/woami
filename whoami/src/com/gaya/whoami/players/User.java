package com.gaya.whoami.players;

import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;

import java.util.Collection;

/**
 * Created by Lenovo-User on 12/08/2014.
 */
public interface User extends Player {
    /**
     * updates the answer for the active user
     * @param question the question to update the answer for
     * @param answer the answer given by the user, null for reset
     */
    void setAnswer(Question question,Answer answer);

    void updateLocation();


    int getType();

    boolean isAuthenticated();

    Collection<Question> getUnansweredQuestions(Collection<Question> questions);

}
