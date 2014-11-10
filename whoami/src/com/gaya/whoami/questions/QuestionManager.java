package com.gaya.whoami.questions;

import com.gaya.whoami.ioc.ILazyLoader;
import com.gaya.whoami.ioc.LazyLoader;

import java.util.List;

/**
 * Created by Lenovo-User on 12/08/2014.
 */
public interface QuestionManager extends ILazyLoader {
    /**
     * gets a list of available questions in the system
     * @return
     */
    List<? extends Question> getQuestions() throws LazyLoader.InvalidStateException;


}
