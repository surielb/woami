package com.gaya.whoami.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.NetworkImageView;
import com.gaya.whoami.ImageLoaderHelpers;
import com.gaya.whoami.MainActivity;
import com.gaya.whoami.R;
import com.gaya.whoami.fragments.questions.AskFragment;
import com.gaya.whoami.ioc.Callback;
import com.gaya.whoami.players.User;
import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;
import com.gaya.whoami.questions.QuestionManager;
import com.gaya.whoami.social.ISocialManager;

import static com.gaya.whoami.ioc.ServiceLocator.getService;

/**
 * Created by Suri on 11/8/2014.
 */
public class ProfileFragment extends AskFragment {

    private User user = getService(ISocialManager.class).getActiveUser();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ((TextView) view.findViewById(R.id.profile_name)).setText(user.getName());
        ((NetworkImageView) view.findViewById(R.id.profile_pic)).setImageUrl(user.getImageUrl(), ImageLoaderHelpers.getImageLoader());
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        super.onViewCreated(view, savedInstanceState);

        getService(QuestionManager.class).registerReadyCallback(new Callback<Boolean>() {
            @Override
            public void callback(Boolean result, Throwable error) {
                progressBar.setVisibility(View.GONE);
                if (error != null)
                    Toast.makeText(getActivity(), "Error loading questions", Toast.LENGTH_LONG).show();
                else
                    setQuestions(getService(QuestionManager.class).getQuestions());
            }
        });
    }

    @Override
    public void onAnswer(Question question, Answer answer) {
        if(!hasNext())
            ((MainActivity)getActivity()).showFragment(Fragments.FIND_PLAYERS,true);
        super.onAnswer(question, answer);
        user.setAnswer(question, answer);

    }

    @Override
    protected boolean setQuestion(Question question) {
        if (super.setQuestion(question)) {
            Answer answer = user.getAnswer(question);
            questionFragment.setAnswer(answer);
            return true;
        }
        return false;
    }
}
