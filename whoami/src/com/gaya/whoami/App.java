package com.gaya.whoami;

import android.app.Application;
import android.content.Context;
import com.gaya.whoami.entities.ParseAnswer;
import com.gaya.whoami.entities.ParseProfileAnswer;
import com.gaya.whoami.entities.ParseQuestion;
import com.gaya.whoami.entities.ParseUser;
import com.gaya.whoami.events.EventManager;
import com.gaya.whoami.events.IEventManager;
import com.gaya.whoami.ioc.SimpleServiceLocator;
import com.gaya.whoami.questions.ParseQuestionManager;
import com.gaya.whoami.questions.QuestionManager;
import com.gaya.whoami.social.ISocialManager;
import com.gaya.whoami.social.SocialManager;
import com.parse.Parse;
import com.parse.ParseObject;

import static com.gaya.whoami.ioc.ServiceLocator.registerService;

/**
 * Created by Suri on 11/6/2014.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        Parse.enableLocalDatastore(this);

        ImageLoaderHelpers.init(this);
        ParseObject.registerSubclass(ParseUser.class);
        ParseObject.registerSubclass(ParseQuestion.class);
        ParseObject.registerSubclass(ParseAnswer.class);
        ParseObject.registerSubclass(ParseProfileAnswer.class);

        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_app_key));
        //PushService.setDefaultPushCallback(this, MainActivity.class);

        //ParseInstallation.getCurrentInstallation().saveInBackground();
        final Context appContext = this;
        SimpleServiceLocator.init(new Runnable() {
            @Override
            public void run() {
                registerService(Context.class, appContext);
                registerService(IEventManager.class, EventManager.class);
                registerService(ISocialManager.class, SocialManager.class);
                registerService(QuestionManager.class, ParseQuestionManager.class);
            }
        });

    }
}
