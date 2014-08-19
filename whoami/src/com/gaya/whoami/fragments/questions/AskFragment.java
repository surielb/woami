package com.gaya.whoami.fragments.questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gaya.whoami.Globals;
import com.gaya.whoami.R;
import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;

import java.util.List;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public abstract class AskFragment extends Fragment implements QuestionFragment.AnswerSelectedListener {
    private QuestionFragment questionFragment;
    private View nextView;
    private View backView;
    private List<Question> questions;
    private int currentQuestion;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(questionFragment == null) {
            questionFragment = new QuestionFragment();
            questionFragment.setAnswerSelectedListener(this);
            nextView = view.findViewById(R.id.next);
            backView = view.findViewById(R.id.prev);
            if(nextView != null)
                nextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moveNext();
                    }
                });
            if(backView != null)
                backView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        moveBack();
                    }
                });

        }
        getChildFragmentManager().beginTransaction().add(R.id.fragment_container,questionFragment).commit();
        setQuestions(questions);
        validateButtons();
    }

    @Override
    public void onAnswer(Question question, Answer answer) {

        if(hasNext())
            moveNext();
    }

    /**
     * returns the number of questions available
     * @return
     */
    public int size(){
        if(questions == null)
            return 0;
        return questions.size();
    }

    /**
     * gets the current question position
     * @return
     */
    public int getCurrentPosition(){
        return currentQuestion;
    }

    /**
     * returns the current question
     */
    public Question getCurrentQuestion(){
        if(questionFragment == null)
            return null;
        return questionFragment.getQuestion();
    }


    /**
     * tests if another question is available
     * @return
     */
    protected boolean hasNext(){
        return currentQuestion< size()-1;
    }

    /**
     * tests if a previous question is available
     * @return
     */
    protected boolean hasPrevious(){
        return currentQuestion>0 && size()>0;
    }


    /**
     * displays the next question
     * @return true if the next question was displayed
     */
    public boolean moveNext(){
        if(hasNext()) {
            return setQuestion(questions.get(++currentQuestion));
        }
        return false;
    }

    /**
     * displays the previous question if available
     * @return true if the prev question was displayed
     */
    public boolean moveBack(){
        if(hasPrevious()){
            currentQuestion=Math.min(currentQuestion-1,size()-1);
            return setQuestion(questions.get(currentQuestion));
        }
        return false;
    }

    protected boolean setQuestion(Question question)
    {
        if(questionFragment == null)
            return false;
        questionFragment.setQuestion(question);
        validateButtons();
        return question != null;
    }

    /**
     * validates the next prev buttons if available
     */
    protected void validateButtons(){
        if(backView != null)
        {
            backView.setEnabled(hasPrevious());
        }
        if(nextView != null)
            nextView.setEnabled(hasNext());
    }

    protected void setAnswer(Answer answer){
        if(questionFragment != null)
        {
            questionFragment.setAnswer(answer);
        }
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        currentQuestion=0;
        if(questions != null && questions.size()>0)
            setQuestion(questions.get(0));
    }

    public List<Question> getQuestions(){
        return questions;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Globals.detachFragment(this);
    }
}
