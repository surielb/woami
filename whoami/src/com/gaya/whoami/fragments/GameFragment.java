package com.gaya.whoami.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import com.android.volley.toolbox.NetworkImageView;
import com.gaya.whoami.ImageLoaderHelpers;
import com.gaya.whoami.MainActivity;
import com.gaya.whoami.R;
import com.gaya.whoami.fragments.questions.AskFragment;
import com.gaya.whoami.ioc.Callback;
import com.gaya.whoami.ioc.ILazyLoader;
import com.gaya.whoami.players.Player;
import com.gaya.whoami.questions.Answer;
import com.gaya.whoami.questions.Question;
import com.gaya.whoami.questions.QuestionManager;
import com.gaya.whoami.widget.TimerView;

import java.util.HashMap;
import java.util.Map;

import static com.gaya.whoami.Globals.maskText;
import static com.gaya.whoami.ioc.ServiceLocator.getService;

/**
 * Created by Suri on 11/10/2014.
 */
public class GameFragment extends AskFragment implements TimerView.OnTimerCompleteListener {
    public GameFragment() {
        setNoun("they");
    }


    private Player player;
    final static QuestionManager QUESTION_MANAGER = getService(QuestionManager.class);
    private TimerView timerView;
    private Map<Question, Answer> answerMap = new HashMap<Question, Answer>();
    private TextView playerName;
    private NetworkImageView playerImage;
    private ViewAnimator viewAnimator;
    private ViewGroup complete;
    private ViewGroup nogood;

    public Player getPlayer() {
        return player;
    }

    public GameFragment setPlayer(Player player) {
        this.player = player;
        answerMap.clear();
        updateQuestions(player);
        updatePlayerInfo(player);
        return this;
    }

    private void updatePlayerInfo(Player player) {
        if (playerName != null) {
            if (player == null) {
                playerName.setText("");
                playerImage.setImageUrl(null, null);
            } else {
                playerName.setText(maskText(player.getName()));
                playerImage.setImageUrl(player.getImageUrl(), ImageLoaderHelpers.getImageLoader());
            }
            viewAnimator.setDisplayedChild(0);
        }

    }

    private void updateQuestions(Player player) {
        if (player != null && QUESTION_MANAGER.getReadyState() == ILazyLoader.ReadyState.Ready)
            setQuestions(player.getQuestions(QUESTION_MANAGER.getQuestions()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerName = (TextView) view.findViewById(R.id.profile_name);
        playerImage = (NetworkImageView) view.findViewById(R.id.profile_pic);
        timerView = (TimerView) view.findViewById(R.id.timer);
        timerView.setCompleteListener(this);
        viewAnimator = (ViewAnimator) view.findViewById(R.id.viewAnimator);
        complete = (ViewGroup) view.findViewById(R.id.complete);
        nogood = (ViewGroup) view.findViewById(R.id.noGood);
        View.OnClickListener playAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showFragment(Fragments.FIND_PLAYERS, false);
            }
        };
        complete.findViewById(R.id.playAgain).setOnClickListener(playAgain);
        nogood.findViewById(R.id.playAgain).setOnClickListener(playAgain);
        complete.findViewById(R.id.link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(player.getLink()));
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "Oops, something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
        setPlayer(getPlayer());
        if (QUESTION_MANAGER.getReadyState() == ILazyLoader.ReadyState.Loading) {
            //wait for it
            QUESTION_MANAGER.registerReadyCallback(new Callback<Boolean>() {
                @Override
                public void callback(Boolean result, Throwable error) {
                    updateQuestions(player);
                }
            });
        }
    }


    @Override
    protected boolean setQuestion(Question question) {

        if (super.setQuestion(question)) {
            if(timerView!=null)
            timerView.reset(10);
            return true;
        }
        return false;

    }

    @Override
    public void onAnswer(Question question, Answer answer) {
        super.onAnswer(question, answer);
        answerMap.put(question, answer);

        if (viewAnimator != null && !hasNext()) {
            //game over - lets see what we got
            int good = 0;
            int bad = 0;
            for (Map.Entry<Question, Answer> entry : answerMap.entrySet()) {
                if (player.getAnswer(entry.getKey()) == entry.getValue())
                    good++;
                else
                    bad++;
            }
            if (good >= player.getAnsweredQuestions() * .8) {
                //we are good
                ((TextView) complete.findViewById(R.id.playerName)).setText(player.getName());
                ((NetworkImageView) complete.findViewById(R.id.playerImage)).setImageUrl(player.getImageUrl(), ImageLoaderHelpers.getImageLoader());
                viewAnimator.setDisplayedChild(1);

            } else {
                //nope, failed
                viewAnimator.setDisplayedChild(2);
            }

        }
    }

    @Override
    public void onTimerComplete(TimerView timer1) {
        moveNext();
    }
}
