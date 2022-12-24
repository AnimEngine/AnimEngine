package AnimEngine.mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.R;


//import AnimEngine.mobile.utils.Anime;

public class CatalogRVAdapter extends RecyclerView.Adapter<CatalogRVAdapter.MyViewHolder> {
    //logic
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<Anime> mAnimes;

    public CatalogRVAdapter(Context mContext, LayoutInflater layoutInflater, ArrayList<Anime> mAnimes) {
        this.mContext = mContext;
        this.layoutInflater = layoutInflater;
        this.mAnimes = mAnimes;
    }

    @Override
    public int getItemCount() {
        return this.mAnimes.size();
    }

    @NonNull
    @Override
    public CatalogRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInfalter = LayoutInflater.from(mContext);
        view = mInfalter.inflate(R.layout.cardview_item_anime,parent,false);
        return new CatalogRVAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CatalogRVAdapter.MyViewHolder viewHolder, final int position) {
        Picasso.get().load((this.mAnimes.get(position)).getImageURL()).into(viewHolder.imageButton);
        //Picasso.get().load((this.mAnimes.get(position)).getImageURL()).into(viewHolder.imageButton);
        viewHolder.textView.setText(mAnimes.get(position).getName());
        viewHolder.imageButton.setOnClickListener(v -> {
//                final AlertDialog alertDialog = (new AlertDialog.Builder(mContext)).create();
//                View view = layoutInflater.inflate(R.layout.dialog_manufacturers, null);
//
//                ((TextView)view.findViewById(R.id.TVManudialog_name)).setText((mManufacturers.get(position)).getName());
//
//                TextView textView = view.findViewById(R.id.TVManudialog_desc);
//                textView.setText((mManufacturers.get(position)).getDescription().replace("_n", "\n"));
//
//                view.findViewById(R.id.ButtonManudialog_close).setOnClickListener(new View.OnClickListener() {
//
//                    public void onClick(View param2View) {
//                        alertDialog.dismiss();
//                    }
//                });
//                alertDialog.setView(view);
//                alertDialog.show();
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageButton imageButton;
        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageButton = itemView.findViewById(R.id.IBcardview_anime);
            this.textView = itemView.findViewById(R.id.TVcardview_anime);
        }
    }



}
