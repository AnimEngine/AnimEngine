package AnimEngine.mobile;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import AnimEngine.mobile.adapters.ProfileRVAdapter;
import AnimEngine.mobile.classes.Creator;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.creatorModel;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, Observer {

    boolean isCreator;
    UserAndToken creator;
    Creator creatorObj;

    creatorModel model;

    UserAndToken fan;
    Fan fanObj;

    RecyclerView recyclerView;
    ProfileRVAdapter adapter;
    ArrayList<Pair<String, String>> keyValuePairs;

    LinearLayout buttonCancelCommitPair;
    Button commit, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        commit = findViewById(R.id.button_commit);
        cancel = findViewById(R.id.button_cancel);
        buttonCancelCommitPair = findViewById(R.id.button_cancel_commit_pair);
        buttonCancelCommitPair.setVisibility(GONE);

        commit.setOnClickListener(this);
        cancel.setOnClickListener(this);

        keyValuePairs = new ArrayList<>();

        model = new creatorModel();
        model.addObserver(this);

        creator = (UserAndToken) getIntent().getSerializableExtra("creator");
        if (creator != null) {
            isCreator = true;
            creatorObj = (Creator) creator.getUser();

            keyValuePairs.add(new Pair<>("Email: ", creator.getUser().getEmail()));
            keyValuePairs.add(new Pair<>("Password: ", creator.getUser().getPassword()));

            keyValuePairs.add(new Pair<>("Studio Name: ", creatorObj.getStudioName()));
            keyValuePairs.add(new Pair<>("Website: ", creatorObj.getWebAddress()));
        }
        else {
            fan = (UserAndToken) getIntent().getSerializableExtra("fan");
            isCreator = false;
            fanObj = (Fan) fan.getUser();

            keyValuePairs.add(new Pair<>("Email: ", fan.getUser().getEmail()));
            keyValuePairs.add(new Pair<>("Password: ", fan.getUser().getPassword()));

            keyValuePairs.add(new Pair<>("First Name: ", fanObj.getFName()));
            keyValuePairs.add(new Pair<>("Last Name: ", fanObj.getLName()));
        }


        recyclerView = findViewById(R.id.recycler_profile);

        adapter =
                new ProfileRVAdapter(ProfileActivity.this, getLayoutInflater(), keyValuePairs, this::showActionButtons);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
    }

    @Override
    public void onClick(View view) {
        if(view == findViewById(R.id.button_commit)) {
            String email = adapter.getmFields().get(0).second;
            String password = adapter.getmFields().get(1).second;




                String field1 = adapter.getmFields().get(2).second;
                String field2 = adapter.getmFields().get(3).second;
            if(isCreator) {
                Creator newCreator = new Creator(email, password, "creator", field1, field2);
                model.editUser(creator.getToken(), "creator", newCreator);
            }else{
                Fan newFan = new Fan(email, password, "fan", field1, field2);
                model.editUser(fan.getToken(), "fan", newFan);
            }



        }

        if(view == findViewById(R.id.button_cancel)) {
            hideActionButtons();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item){
//
//        }

        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        String result = this.model.getResult();

        if(Objects.equals(result, ""))
            return;

        if(result.startsWith("OK")){
            Toast.makeText(getApplicationContext(), "User updated Successfully!", Toast.LENGTH_SHORT).show();
            adapter.setEdited(false);
            hideActionButtons();

        }else {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    public void showActionButtons(){
        buttonCancelCommitPair.setVisibility(View.VISIBLE);
    }

    public void hideActionButtons(){
        buttonCancelCommitPair.setVisibility(GONE);
        if(adapter.isEdited())
            adapter.rollback();
        adapter.setEdited(false);
    }
}