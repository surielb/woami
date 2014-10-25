package com.gaya.whoami.database;

import android.provider.BaseColumns;

public final class AnswersContract {

    public AnswersContract() {}

    /* Inner class that defines the table contents */
    public static abstract class AnswerEntry implements BaseColumns {
        public static final String TABLE_NAME = "answers";
        public static final String COLUMN_NAME_PLAYER_ID = "player_id";
        public static final String COLUMN_NAME_QUESTION_ID = "question_id";
        public static final String COLUMN_NAME_ANSWER_ID = "answer_id";


    }
}