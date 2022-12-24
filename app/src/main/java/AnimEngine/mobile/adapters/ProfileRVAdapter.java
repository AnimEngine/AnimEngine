package AnimEngine.mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.function.Consumer;
import java.util.function.Function;

import AnimEngine.mobile.R;
import AnimEngine.mobile.classes.Genre;

public class ProfileRVAdapter extends RecyclerView.Adapter<ProfileRVAdapter.MyViewHolder>{
    //logic
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<Pair<String, String>> mFields;

    ArrayList<Pair<String, String>> rollback;
    ArrayList<TextView> textViews;

    boolean isEdited;
    Runnable function;

    public ProfileRVAdapter(Context mContext, LayoutInflater layoutInflater, ArrayList<Pair<String, String>> mFields, Runnable function) {
        this.mContext = mContext;
        this.layoutInflater = layoutInflater;
        this.mFields = mFields;

        this.isEdited = false;
        this.function = function;
        textViews = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return this.mFields.size();
    }

    @NonNull
    @Override
    public ProfileRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInfalter = LayoutInflater.from(mContext);

        view = mInfalter.inflate(R.layout.linear_item_profile,parent,false);

        return new ProfileRVAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ProfileRVAdapter.MyViewHolder viewHolder, final int position) {
        TextView textItemTitle = viewHolder.textItemTitle;
        textItemTitle.setText(mFields.get(position).first);

        TextView textItem = viewHolder.textItem;
        textViews.add(textItem);
        textItem.setVisibility(View.VISIBLE);

        textItem.setText(mFields.get(position).second);

        EditText editTextItem = viewHolder.editTextItem;
        editTextItem.setText("");
        editTextItem.setHint(String.format("Enter new %s",textItemTitle.getText()));
        editTextItem.setVisibility(View.GONE);

        ImageButton edit = viewHolder.edit;
        edit.setVisibility(View.VISIBLE);

        if(Objects.equals(mFields.get(position).first, "Email: "))
            edit.setVisibility(View.GONE);
        else {
            ImageButton check = viewHolder.check;
            ImageButton cancel = viewHolder.cancel;
            check.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);

            edit.setOnClickListener(v -> {
                if(!isEdited) {
                    rollback = new ArrayList<>(mFields);
                    isEdited = true;
                    function.run();
                }
                editMode(textItem, editTextItem, edit, check, cancel);
                editTextItem.setText("");
            });

            check.setOnClickListener(v -> {
                readMode(textItem, editTextItem, edit, check, cancel);

                mFields.set(position, new Pair<>(mFields.get(position).first, String.valueOf(editTextItem.getText())));
                textItem.setText(String.valueOf(editTextItem.getText()));
//                if(!isEdited) {
//                    isEdited = true;
//                    function.run();
//                }
            });

            cancel.setOnClickListener(v -> {
                readMode(textItem, editTextItem, edit, check, cancel);
            });
        }
    }

    private void editMode(TextView textItem, EditText editTextItem, ImageButton edit, ImageButton check, ImageButton cancel){
        edit.setVisibility(View.GONE);

        check.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);

        editTextItem.setVisibility(View.VISIBLE);
        textItem.setVisibility(View.GONE);
    }
    private void readMode(TextView textItem, EditText editTextItem, ImageButton edit, ImageButton check, ImageButton cancel){
        edit.setVisibility(View.VISIBLE);

        check.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

        editTextItem.setVisibility(View.GONE);
        textItem.setVisibility(View.VISIBLE);
    }
    public void rollback(){
        mFields=new ArrayList<>(rollback);
        for (int i = 0; i < mFields.size(); i++) {
            textViews.get(i).setText(mFields.get(i).second);
        }
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textItemTitle;
        public TextView textItem;
        public EditText editTextItem;
        public ImageButton edit, check, cancel;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textItemTitle = itemView.findViewById(R.id.text_title_profile);

            this.textItem = itemView.findViewById(R.id.text_item_profile);
            this.editTextItem = itemView.findViewById(R.id.edit_text_item_profile);

            this.edit = itemView.findViewById(R.id.image_button_edit);
            this.check = itemView.findViewById(R.id.image_button_check);
            this.cancel = itemView.findViewById(R.id.image_button_cancel);
        }
    }

    public ArrayList<Pair<String, String>> getmFields() {
        return mFields;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}
