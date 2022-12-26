package AnimEngine.mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.Slider;

import java.util.ArrayList;

import AnimEngine.mobile.R;
import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.classes.Genre;

public class CreateRVAdapter extends RecyclerView.Adapter<CreateRVAdapter.MyViewHolder> {
    //logic
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<Genre> mGenres;
    ArrayList<Pair<CheckBox, Slider>> checkBox_Slider_pairs;

    public CreateRVAdapter(Context mContext, LayoutInflater layoutInflater, ArrayList<Genre> mGenres, ArrayList<Pair<CheckBox, Slider>> checkBox_Slider_pairs) {
        this.mContext = mContext;
        this.layoutInflater = layoutInflater;
        this.mGenres = mGenres;
        this.checkBox_Slider_pairs = checkBox_Slider_pairs;
    }

    @Override
    public int getItemCount() {
        return this.mGenres.size();
    }

    @NonNull
    @Override
    public CreateRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInfalter = LayoutInflater.from(mContext);

        view = mInfalter.inflate(R.layout.linear_item_genre,parent,false);

        return new CreateRVAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CreateRVAdapter.MyViewHolder viewHolder, final int position) {
        CheckBox checkBox = viewHolder.checkBox;
        Slider slider = viewHolder.slider;

        checkBox_Slider_pairs.add(new Pair<>(checkBox, slider));

        checkBox.setText(mGenres.get(position).getName());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(mContext, "bidning adapter "+viewHolder.getBindingAdapterPosition()+"\n"+"layout pos "+viewHolder.getLayoutPosition(), Toast.LENGTH_SHORT).show();
                Slider slider1 = checkBox_Slider_pairs.get(viewHolder.getLayoutPosition()).second;
                if(isChecked)
                    slider1.setVisibility(View.VISIBLE);
                else
                    slider1.setVisibility(View.INVISIBLE);
            }
        });

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox;
        public Slider slider;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.checkBox = itemView.findViewById(R.id.checkbox_genre);
            this.slider = itemView.findViewById(R.id.slider_genre);
        }
    }
}
