package com.gaya.whoami.questions;

import java.util.Collection;

/**
 * Created by Lenovo-User on 12/08/2014.
 */
public interface Question {
    String  getId();
    String getText();
    Collection<Answer> getAnswers();
}
