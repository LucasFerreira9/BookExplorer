package com.example.bookexplorer.util;

import android.os.Looper;
import android.os.Handler;
import android.util.Log;

public class AsyncTask<T> {
    private final String TAG = "AsyncTask";
    private final Handler mainHandler;
    private Thread backgroundThread = null;
    private final DoInBackground<T> doInBackground;
    private final OnFinished<T> onFinished;
    private OnExceptionListener onExceptionListener = null;


    /**
     * Represents the method to be executed in background
     *
     */
    public interface DoInBackground<T>{
        /**
         * execute a task in background.
         *
         * @return <T> taskResult
         * @throws Exception genericException
         */
        T executeInBackground() throws Exception;
    }
    /**
     * Represents the method to be executed in main thread after the background execution
     *
     */
    public interface OnFinished<T>{
        /**
         * execute a task in the main thread with the background task result.
         *
         * @param taskResult the background task result
         */
        void execute(T taskResult);
    }
    /**
     * Represents the method to be executed in main thread if the background task throws an Exception
     *
     */
    public interface OnExceptionListener{
        /**
         * Treats the exception in the main thread
         *
         * @param exception the exception thrown
         */
        void onException(Exception exception);
    }
    /**
     * Represents a method to be executed in the background without an result type.
     *
     */
    public interface SingleTask{
        /**
         * Runs a simple task in background without return result
         *
         */
        void executeInBackground();
    }

    public AsyncTask(DoInBackground<T> doInBackground, OnFinished<T> onFinished){
        this.doInBackground = doInBackground;
        this.onFinished = onFinished;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public AsyncTask(DoInBackground<T> doInBackground, OnFinished<T> onFinished,OnExceptionListener onExceptionListener){
        this(doInBackground,onFinished);
        this.onExceptionListener = onExceptionListener;
    }

    public void setOnExceptionListener(OnExceptionListener onExceptionListener) {
        this.onExceptionListener = onExceptionListener;
    }

    public static void runSingleTask(SingleTask task){
        Thread backgroundThread = new Thread(task::executeInBackground);
        backgroundThread.start();
    }

    public void run(){
        backgroundThread = new Thread(() -> {
            try{
                T taskResult = doInBackground.executeInBackground();
                if (!Thread.currentThread().isInterrupted()) {
                    executeInMainThread(() -> onFinished.execute(taskResult));
                }
            }
            catch (InterruptedException e){
                Log.e(TAG, "Interrupted background execution", e);
            }
            catch (Exception e){
                if(onExceptionListener != null)
                    executeInMainThread(()->onExceptionListener.onException(e));
                else Log.w(TAG,
                        String.format(
                                "Unhandled exception: %s, " +
                                "Maybe you want to set an OnExceptionListener to the AsyncTask",e.getMessage())
                );
            }
        });
        backgroundThread.start();
    }

    /**
     * interrupts the background task thread
     */
    public void cancel() {
        if (backgroundThread != null && backgroundThread.isAlive())
            backgroundThread.interrupt();
    }

    private void executeInMainThread(Runnable runnable){
        mainHandler.post(runnable);
    }
}
