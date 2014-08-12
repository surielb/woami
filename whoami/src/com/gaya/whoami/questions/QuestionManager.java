package com.gaya.whoami.questions;

import com.gaya.whoami.players.User;

import java.util.Collection;

/**
 * Created by Lenovo-User on 12/08/2014.
 */
public interface QuestionManager {
    /**
     * gets a list of available questions in the system
     * @return
     */
    Collection<Question> getQuestions();

    /**
     * gets the answer for the question given by the active user
     * @param question the question to answer
     * @return
     */
    Answer getAnswer(Question question);

    /**
     * updates the answer for the active user
     * @param question the question to update the answer for
     * @param answer the answer given by the user, null for reset
     */
    void setAnswer(Question question,Answer answer);
}
