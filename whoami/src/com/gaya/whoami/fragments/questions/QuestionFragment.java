package com.gaya.whoami.fragments.questions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import com.gaya.whoami.Globals;
import com.gaya.whoami.R;
import com.gaya.whoami.adapters.AnswerAdapter;
import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public class QuestionFragment extends Fragment {
    private GridView gridView;
    private TextView textView;
    private Question question;
    private AnswerAdapter answerAdapter;
    private AnswerSelectedListener answerSelectedListener;

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
        if (gridView != null) {
            if (question != null) {
                textView.setText(question.getText());
                answerAdapter.setAnswers(question.getAnswers());
            } else {
                textView.setText(null);
                answerAdapter.setAnswers(null);
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        answerAdapter = new AnswerAdapter(getActivity());

        gridView = (GridView) view.findViewById(R.id.answers);

        gridView.setAdapter(answerAdapter);
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Answer answer =answerAdapter.getItem(position);
              //  onAnswerSelected(answer);
            }
        });


        textView = (TextView) view.findViewById(R.id.question);

        //someone may have set the question before we where ready
        setQuestion(question);
    }

    protected void onAnswerSelected(Answer answer) {
        if(answerSelectedListener != null)
            answerSelectedListener.onAnswer(question,answer);
    }

    public AnswerSelectedListener getAnswerSelectedListener() {
        return answerSelectedListener;
    }

    public void setAnswerSelectedListener(AnswerSelectedListener answerSelectedListener) {
        this.answerSelectedListener = answerSelectedListener;
    }

    /**
     * selects the answer
     * @param answer
     * @return
     */
    public boolean setAnswer(Answer answer) {
        if(gridView==null)return false;
        if(answer == null)
        {
            gridView.setSelection(-1);
            return true;
        }
        for(int i=0;i<answerAdapter.getCount();i++)
        {
            if(answer.equals(answerAdapter.getItem(i))){
                gridView.setSelection(i);
                return true;
            }
        }
        return false;
    }


    public interface AnswerSelectedListener {
        void onAnswer(Question question, Answer answer);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Globals.detachFragment(this);

    }
}
