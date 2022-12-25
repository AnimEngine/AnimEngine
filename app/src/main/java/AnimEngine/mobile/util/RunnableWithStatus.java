package AnimEngine.mobile.util;

import java.util.concurrent.Callable;

public class RunnableWithStatus<T> implements Runnable{
    private boolean isFinished = false;
    private T ret;
    private Callable<T> callable;

    public RunnableWithStatus(Callable<T> callable) {
        this.callable = callable;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public T getRet() {
        return ret;
    }

    @Override
    public void run() {
        try {
            ret = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //runnable.run();
        isFinished = true;
    }
}
