package AnimEngine.mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class FanFragment extends Fragment {
    ArrayList<EditText> list;
    public FanFragment(ArrayList<EditText> list){
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_fan,container,false);

        this.list.add(((TextInputLayout)view.findViewById(R.id.text_input_first_name_sign_up_fan)).getEditText());
        this.list.add(((TextInputLayout)view.findViewById(R.id.text_input_last_name_sign_up_fan)).getEditText());
        this.list.add(((TextInputLayout)view.findViewById(R.id.text_input_email_sign_up_fan)).getEditText());
        this.list.add(((TextInputLayout)view.findViewById(R.id.text_input_password_sign_up_fan)).getEditText());

        return view;
    }
}
