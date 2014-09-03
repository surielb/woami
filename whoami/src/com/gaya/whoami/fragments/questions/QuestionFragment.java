package com.gaya.whoami.fragments.questions;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import com.gaya.whoami.*;
import com.gaya.whoami.adapters.*;
import com.gaya.whoami.questions.*;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public class QuestionFragment extends Fragment {
    private AbsListView gridView;
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
            //gridView.setSelection(-1);
            gridView.setItemChecked(gridView.getCheckedItemPosition(),false);
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

        gridView = (AbsListView) view.findViewById(R.id.answers);

        gridView.setAdapter(answerAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Answer answer =answerAdapter.getItem(position);
                //gridView.setItemChecked(position,true);
                onAnswerSelected(answer);
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
                gridView.setItemChecked(i,true);
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
