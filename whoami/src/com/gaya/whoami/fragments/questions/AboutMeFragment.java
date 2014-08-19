package com.gaya.whoami.fragments.questions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gaya.whoami.Globals;
import com.gaya.whoami.R;
import com.gaya.whoami.questions.Question;
import com.gaya.whoami.questions.QuestionManager;

import java.util.LinkedList;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public class AboutMeFragment extends AskFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //load the question manager questions
        setQuestions(new LinkedList<Question>(Globals.getQuestionManager().getQuestions()));

        return inflater.inflate(R.layout.fragement_about_me,container,false);
    }
}
