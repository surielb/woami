package com.gaya.whoami.questions;

import com.gaya.whoami.entities.ParseQuestion;
import com.gaya.whoami.ioc.LazyLoader;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class ParseQuestionManager extends LazyLoader implements QuestionManager {

    private List<ParseQuestion> questions;


    @Override
    public List<? extends Question> getQuestions() throws InvalidStateException {
        ensureValidState();
        return questions;
    }

    @Override
    protected void lazyLoad() {
        ParseQuery<ParseQuestion> query = ParseQuery.getQuery(ParseQuestion.class);
        query.include("answers");
        query.findInBackground(new FindCallback<ParseQuestion>() {
            @Override
            public void done(final List<ParseQuestion> parseQuestions, final ParseException e) {
                questions = parseQuestions;
                setLoadComplete(e);


            }
        });
    }
}
