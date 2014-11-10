package com.gaya.whoami.players;

import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: gaya Date: 9/3/14 Time: 9:49 PM To change this template use File | Settings |
 * File Templates.
 */
public interface Player {
    String getId();
    String getName();
    String getImageUrl();
    String getLink();

    /**
     * gets the answer for the question given by the active user
     * @param question the question to answer
     * @return
     */
    Answer getAnswer(Question question);

    /**
     * returns the questions this player has answered
     * @return
     */
    List<Question> getQuestions(Collection<? extends Question> questions);

    int getAnsweredQuestions();

}
