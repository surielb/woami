package com.gaya.whoami.adapters;

import android.content.*;
import android.view.*;
import android.widget.*;
import com.android.volley.toolbox.*;
import com.gaya.whoami.*;
import com.gaya.whoami.questions.*;

import java.util.*;

/**
 * Created by Lenovo-User on 19/08/2014.
 */
public class AnswerAdapter extends BaseAdapter {
    final LayoutInflater layoutInflater;
    private final List<Answer> answers = new ArrayList<Answer>();




    public void setAnswers(Collection<Answer> answerCollection)
    {
        answers.clear();
        if(answerCollection != null)
            answers.addAll(answerCollection);
        if(getCount()==0)
            notifyDataSetInvalidated();
        else
            notifyDataSetChanged();
    }

    public AnswerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(answers== null)
            return 0;
        return answers.size();
    }

    @Override
    public Answer getItem(int i) {
        if(answers== null|| i<0 || i>=answers.size())
        return null;
        return answers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AnswerHolder holder;
        if(convertView != null)
            holder = (AnswerHolder) convertView.getTag();
        else
            holder= new AnswerHolder( layoutInflater.inflate(R.layout.answer,viewGroup,false));
        Answer answer = getItem(position);

        holder.setItem(answer);
        return holder.view;
    }

    static class AnswerHolder {
        final View view;
        final TextView textView;
        final NetworkImageView networkImageView;

        AnswerHolder(View view) {



            this.view =view; /*new FrameLayout(view.getContext()){
                @Override
                public void setActivated(boolean activated) {
                    super.setActivated(activated);    //To change body of overridden methods use File | Settings | File Templates.
                }

                @Override
                public void refreshDrawableState() {
                    super.refreshDrawableState();    //To change body of overridden methods use File | Settings | File Templates.
                }
            };
            ((FrameLayout)this.view).addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);*/
            this.view.setTag(this);
            textView= (TextView) view.findViewById(R.id.text);
            networkImageView = (NetworkImageView) view.findViewById(R.id.thumb);
        }

        AnswerHolder setItem(Answer answer){
            textView.setText(answer.getTitle());
            networkImageView.setImageUrl(answer.getImageUrl(), ImageLoaderHelpers.getImageLoader());
            return this;
        }
    }
}
