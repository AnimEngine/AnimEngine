package AnimEngine.mobile;

import android.app.Application;

import AnimEngine.mobile.util.ModelLocator;

public class MyApplication extends Application {
    ModelLocator modelLocator = null;
    @Override
    public void onCreate() {
        super.onCreate();
        modelLocator = ModelLocator.getInstance();
    }

    public ModelLocator getModelLocator() {
        return ModelLocator.getInstance();
    }
}
