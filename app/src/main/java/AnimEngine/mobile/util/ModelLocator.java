package AnimEngine.mobile.util;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.*;
import AnimEngine.mobile.models.CreatorModel.*;


public class ModelLocator {

    static InitialContext context = new InitialContext();
    private static volatile ModelLocator INSTANCE = null;

    public Model getModel(String name, List<Pair<String, Object>> args) throws Exception {
        return context.lookup(name, args);
    }

    public static ModelLocator getInstance() {
        if(INSTANCE == null) {
            synchronized (ModelLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ModelLocator();
                }
            }
        }
        return INSTANCE;
    }
}