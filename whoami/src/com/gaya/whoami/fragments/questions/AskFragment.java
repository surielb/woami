package com.gaya.whoami.fragments.questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import com.gaya.whoami.Globals;
import com.gaya.whoami.R;
import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;

import java.util.List;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public abstract class AskFragment extends Fragment implements QuestionFragment.AnswerSelectedListener {
    protected QuestionFragment questionFragment;
    private View nextView;
    private View backView;
    private List<? extends Question> questions;
    private int currentQuestion;
    private String noun = "you";
    private TextView questionNumber;

    public String getNoun() {
        return noun;
    }

    public AskFragment setNoun(String noun) {
        this.noun = noun;
        if (questionFragment != null)
            questionFragment.setNoun(noun);
        return this;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        questionFragment = (QuestionFragment) getChildFragmentManager().findFragmentById(R.id.fragment_container);
        if (questionFragment == null) {
            questionFragment = new QuestionFragment();

            getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, questionFragment).commit();
        }
        questionNumber = (TextView) view.findViewById(R.id.questionNumber);
        questionFragment.setNoun(noun);
        questionFragment.setAnswerSelectedListener(this);
        nextView = view.findViewById(R.id.next);
        backView = view.findViewById(R.id.prev);
        if (nextView != null)
            nextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveNext();
                }
            });
        if (backView != null)
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveBack();
                }
            });

        setQuestions(questions);
        validateButtons();
    }

    @Override
    public void onAnswer(Question question, Answer answer) {

        //user.setAnswer(question, answer);

        if (hasNext())
            moveNext();
        else {

            //todo start game


        }
    }

    /**
     * returns the number of questions available
     *
     * @return
     */
    public int size() {
        if (questions == null)
            return 0;
        return questions.size();
    }

    /**
     * gets the current question position
     *
     * @return
     */
    public int getCurrentPosition() {
        return currentQuestion;
    }

    /**
     * returns the current question
     */
    public Question getCurrentQuestion() {
        if (questionFragment == null)
            return null;
        return questionFragment.getQuestion();
    }


    /**
     * tests if another question is available
     *
     * @return
     */
    protected boolean hasNext() {
        return currentQuestion < size() - 1;
    }

    /**
     * tests if a previous question is available
     *
     * @return
     */
    protected boolean hasPrevious() {
        return currentQuestion > 0 && size() > 0;
    }


    /**
     * displays the next question
     *
     * @return true if the next question was displayed
     */
    public boolean moveNext() {
        if (hasNext()) {
            return setQuestion(questions.get(++currentQuestion));
        }
        return false;
    }

    /**
     * displays the previous question if available
     *
     * @return true if the prev question was displayed
     */
    public boolean moveBack() {
        if (hasPrevious()) {
            currentQuestion = Math.min(currentQuestion - 1, size() - 1);
            return setQuestion(questions.get(currentQuestion));
        }
        return false;
    }

    protected boolean setQuestion(Question question) {
        if (questionFragment == null || getActivity() == null)
            return false;

        questionFragment.setQuestion(question);
        questionNumber.setText(getString(R.string.question_number,questions.indexOf(question) +1,questions.size()));
        /*Answer answer = user.getAnswer(question);
        questionFragment.setAnswer(answer);*/
        validateButtons();
        return question != null;
    }

    /**
     * validates the next prev buttons if available
     */
    protected void validateButtons() {
        if (backView != null) {
            backView.setEnabled(hasPrevious());
        }
        if (nextView != null)
            nextView.setEnabled(hasNext());
    }

    protected void setAnswer(Answer answer) {
        if (questionFragment != null) {
            questionFragment.setAnswer(answer);
        }
    }

    public void setQuestions(List<? extends Question> questions) {
        this.questions = questions;
        currentQuestion = 0;
        if (questions != null && questions.size() > 0)
            setQuestion(questions.get(0));
    }

    public List<? extends Question> getQuestions() {
        return questions;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (questionFragment != null) {
            try {
                getChildFragmentManager().beginTransaction().remove(questionFragment).commitAllowingStateLoss();
            } catch (Exception e) {

            }
        }
        Globals.detachFragment(this);
    }
}
