package AnimEngine.mobile.util;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.CreatorModel;
import AnimEngine.mobile.models.DBAndStorageModel;
import AnimEngine.mobile.models.FanModel;
import AnimEngine.mobile.models.Model;
import AnimEngine.mobile.models.UserModel;

public class InitialContext {
    public static final String USER = "USER";
    public static final String CREATOR = "CREATOR";
    public static final String FAN = "FAN";
    public static final String DBSTORAGE = "DBSTORAGE";

    static ArrayList<String> modelNames = new ArrayList<>(Arrays.asList(CREATOR, FAN, DBSTORAGE));
    static HashMap<String, Supplier<Model>> modelConstructors;

    static {
        modelConstructors = new HashMap<>();
        modelConstructors.put(CREATOR, CreatorModel::new);
        modelConstructors.put(FAN, FanModel::new);
        modelConstructors.put(DBSTORAGE, DBAndStorageModel::new);
    }

    public InitialContext() {
    }

    public Model lookup(String name, List<Pair<String, Object>> args) throws Exception {
        if (name.equalsIgnoreCase(USER)) {
            UserAndToken creator=null;
            UserAndToken fan=null;
            if(args != null) {
                creator = (UserAndToken) args.get(0).second;
                fan = (UserAndToken) args.get(1).second;
            }
            return new UserModel(creator, fan);
        }

        if (modelNames.contains(name))
            return Objects.requireNonNull(modelConstructors.get(name)).get();

        return null;
    }
}
