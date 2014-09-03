package com.gaya.whoami;

/**
 * @author suriel
 *         Date: 10/8/13
 *         Time: 2:48 PM
 */

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * a class for logging of the application
 */
public class Logger {


    // TODO:disable logs if needed
    private static final boolean DISPLAY_LOGS = true;
    /**
     * the format of the message to log , when the first string is the method name , and the second is the input message
     * itself
     */
    private static final String LOG_MESSAGE_FORMAT_WHEN_APPLICATION_TAG_DOESNT_EXIST = "[%s]%s:%s";
    /**
     * the format of the message to log when the logger has an application tag, when the first string is the class name
     * , the second is the method name , and the third is the message itself
     */
    private static final String LOG_MESSAGE_FORMAT_WHEN_APPLICATION_TAG_EXISTS = "[%s]%s.%s:%s";
    private static String _applicationTag = null;

    public enum LogLevel {
        VERBOSE(0), DEBUG(1), INFO(2), WARNING(3), ERROR(4), WTF(5);
        public final int level;

        private LogLevel(int level) {
            this.level = level;
        }
    }

    public static LogLevel MinLevel = LogLevel.VERBOSE;

    /**
     * sets an application tag to be used , so that all log messages will be inside this tag.<br /> if the application
     * tag is null (or if never called to this function) , each log message will have a tag that matches the class of
     * the function that called the log function, <br /> otherwise, all log messages will be inside the application tag
     */
    public static void setApplicationTag(final String applicationTag) {
        _applicationTag = applicationTag;
    }

    public static void e(String message,Object... args) {
        log(LogLevel.ERROR, String.format(message,args), 1, null);
    }

    public static void e(Throwable e) {
        if (e == null) return;
        e(null, e.getClass().getName(), e, 1);
    }

    public static void e(String category, Throwable e) {
        e(category, e.getClass().getName(), e, 1);
    }

    public static void e(String category, String title, Throwable e) {
        e(category, title, e, 1);
    }

    //synchronized to make sure IErrorManager creation happens only once
    public synchronized static void e(String category, String title, Throwable e, int offset) {
        if (!DISPLAY_LOGS || LogLevel.ERROR.level < MinLevel.level)
            return;

        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3 + offset];
        final String fullClassName = stackTraceElement.getClassName();
        final String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        final String fullMessage, logTag;
        final String threadId = String.format("%s:%s", Thread.currentThread().getName(), Thread.currentThread().getId());
        final String currentMethodName = stackTraceElement.getMethodName();
        try {
            if (_applicationTag == null) {
                logTag = simpleClassName;
                fullMessage = String.format(LOG_MESSAGE_FORMAT_WHEN_APPLICATION_TAG_DOESNT_EXIST, threadId,currentMethodName, e.toString());
            } else {
                logTag = _applicationTag;
                fullMessage = String.format(LOG_MESSAGE_FORMAT_WHEN_APPLICATION_TAG_EXISTS, threadId, simpleClassName, currentMethodName, e.toString());
            }
            if(e != null)
                e.printStackTrace();
            Log.e(logTag, fullMessage, e);
            /*IErrorManager errorManager = ServiceLocator.getService(IErrorManager.class);
            errorManager.report(category, title, e);*/
        } catch (Throwable innerEx) {

        }
    }

    public static void w(String message) {
        log(LogLevel.WARNING, message, 1, null);
    }

    public static void d(String message) {
        log(LogLevel.DEBUG, message, 1, null);
    }

    public static void d(String format, Object... args) {
        if (!DISPLAY_LOGS || LogLevel.DEBUG.level < MinLevel.level)
            return;

        log(LogLevel.DEBUG, String.format(format, args), 1, null);
    }

    public static void i(String message) {
        log(LogLevel.INFO, message, 1, null);
    }

    public static void w(Throwable e) {
        log(LogLevel.WARNING, e);
    }

    @Deprecated
    public static void log(final LogLevel logLevel, final String message) {
        log(logLevel, message, 1, null);
    }

    /**
     * writes a message to the log , using the specified log level
     */
    private static void log(final LogLevel logLevel, final String message, int offset, JSONObject args) {
        if (!DISPLAY_LOGS || logLevel.level < MinLevel.level)
            return;
        final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements[3 + offset].getClassName().equals("com.conduit.echo.logging.LogHelper"))
            offset++;

        final StackTraceElement stackTraceElement = stackTraceElements[3 + offset];

        final String fullClassName = stackTraceElement.getClassName();
        final String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
        final String fullMessage, logTag;
        final String currentMethodName = stackTraceElement.getMethodName();
        final String threadId = String.format("%s:%s", Thread.currentThread().getName(), Thread.currentThread().getId());
        if (_applicationTag == null) {
            logTag = simpleClassName;
            fullMessage = String.format(LOG_MESSAGE_FORMAT_WHEN_APPLICATION_TAG_DOESNT_EXIST, threadId, currentMethodName, message);
        } else {
            logTag = _applicationTag;
            fullMessage = String.format(LOG_MESSAGE_FORMAT_WHEN_APPLICATION_TAG_EXISTS, threadId, simpleClassName, currentMethodName, message);
        }

        switch (logLevel) {
            case DEBUG:

                Log.d(logTag, fullMessage);
                break;
            case WTF:
                Log.wtf(logTag, fullMessage);
                break;
            case ERROR:

                Log.e(logTag, fullMessage);
                break;
            case INFO:
                Log.i(logTag, fullMessage);
                break;
            case VERBOSE:
                Log.v(logTag, fullMessage);
                break;
            case WARNING:
                Log.w(logTag, fullMessage);
                break;
        }

        /*if (logLevel == LogLevel.ERROR || logLevel == LogLevel.WTF) {
            try {
                if (args == null) {
                    args = new JSONObject();
                }

                args.put("message", fullMessage);

                // Add stack trace
                IReportManager manager = ServiceLocator.getService(IReportManager.class);
                if (manager != null)
                    manager.reportAction(new ActionReport("errorLog", args));
            } catch (JSONException e) {
            }
        }*/
    }

    @Deprecated
    /**
     * use direct methods e,w,i,d instead
     */
    public static void log(final LogLevel logLevel, final Throwable ex) {
        log(logLevel, ex, 1);
    }

    /**
     * writes a message to the log , using the specified log level
     */
    public static void log(final LogLevel logLevel, final Throwable ex, int level) {
        if (ex == null || !DISPLAY_LOGS || logLevel.level < MinLevel.level)
            return;
        StringBuilder sb = new StringBuilder();
        if (ex != null) {
            for (StackTraceElement element : ex.getStackTrace()) {
                sb.append(element.toString());
            }
        }
        JSONObject args = new JSONObject();
        try {
            args.put("trace", sb.toString());
        } catch (JSONException e) {
        }

        log(logLevel, ex.toString(), level + 1, args);
        ex.printStackTrace();
    }
}
