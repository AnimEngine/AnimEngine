package AnimEngine.mobile.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DB {
    private static FirebaseDatabase db = null;
    private static FirebaseAuth au=null;
    private static boolean ans = false;

    public static FirebaseDatabase getDB() {
        if (db == null) {
            db = FirebaseDatabase.getInstance();
        }
        return db;
    }
    public static FirebaseAuth getAU(){
        if (au == null) {
            au = FirebaseAuth.getInstance();
        }
        return au;
    }



}
