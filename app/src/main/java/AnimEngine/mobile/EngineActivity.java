package AnimEngine.mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import AnimEngine.mobile.adapters.SectionsPagerAdapter;
import AnimEngine.mobile.classes.UserAndToken;

public class EngineActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    UserAndToken fan;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_page);

        bottomNavigationView.setOnItemSelectedListener(this);

        fan = (UserAndToken) getIntent().getSerializableExtra("fan");

        findViewById(R.id.button_show_information).setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if(view == findViewById(R.id.button_show_information)){
            // Create the object of AlertDialog Builder class
            AlertDialog.Builder builder = new AlertDialog.Builder(EngineActivity.this);

            // Set Alert Title
            builder.setTitle("Description");

            // Set the message show for the Alert time
            builder.setMessage("Several hundred years ago, humans were nearly exterminated by giants. Giants are typically several stories tall, seem to have no intelligence, devour human beings and, worst of all, seem to do it for the pleasure rather than as a food source. A small percentage of humanity survived by walling themselves in a city protected by extremely high walls, even taller than the biggest of giants. Flash forward to the present and the city has not seen a giant in over 100 years. Teenage boy Eren and his foster sister Mikasa witness something horrific as the city walls are destroyed by a super giant that appears out of thin air. As the smaller giants flood the city, the two kids watch in horror as their mother is eaten alive. Eren vows that he will murder every single giant and take revenge for all of mankind.");


            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            builder.setCancelable(false);

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton("close", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

            // Create the Alert dialog
            AlertDialog alertDialog = builder.create();
            // Show the Alert Dialog box
            alertDialog.show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        int curr = item.getItemId();

        if (R.id.profile_page == curr) {
            intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("fan", fan);
            startActivity(intent);
            finish();

            return true;
        }

        if (R.id.catalog_page == curr) {
            intent = new Intent(this, CatalogActivity.class);
            intent.putExtra("fan", fan);
            startActivity(intent);
            finish();

            return true;
        }


        return false;
    }
}