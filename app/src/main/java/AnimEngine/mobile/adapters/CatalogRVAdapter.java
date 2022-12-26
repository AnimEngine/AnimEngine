package AnimEngine.mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.R;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.User;


//import AnimEngine.mobile.utils.Anime;

public class CatalogRVAdapter extends RecyclerView.Adapter<CatalogRVAdapter.MyViewHolder> {
    //logic
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<Anime> mAnimes;

    boolean isCreator;
    User user;

    public CatalogRVAdapter(Context mContext, LayoutInflater layoutInflater, ArrayList<Anime> mAnimes, boolean isCreator, User user) {
        this.mContext = mContext;
        this.layoutInflater = layoutInflater;
        this.mAnimes = mAnimes;
        this.isCreator = isCreator;
        this.user = user;
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

        viewHolder.textView.setText(mAnimes.get(position).getName());
        viewHolder.imageButton.setOnClickListener(v -> {
                final AlertDialog alertDialog = (new AlertDialog.Builder(mContext)).create();
                View view = layoutInflater.inflate(R.layout.dialog_anime_fan, null);

                ((TextView)view.findViewById(R.id.text_view_title_dialog_anime)).setText((mAnimes.get(position)).getName());

                TextView textView = view.findViewById(R.id.text_view_description_dialog);
                textView.setText((mAnimes.get(position)).getDescription());

                if(isCreator){
                    view.findViewById(R.id.layout_add_comment_dialog).setVisibility(View.GONE);
                }
                else{
                    TextView commenterName = view.findViewById(R.id.text_view_name_add_comment_dialog);
                    Fan fan = ((Fan) user);
                    commenterName.setText(String.format("%s %s",fan.getFName(), fan.getLName()));

                    Button uploadComment = view.findViewById(R.id.button_send_comment_dialog);
                    uploadComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

                view.findViewById(R.id.button_close_dialog).setOnClickListener(param2View -> alertDialog.dismiss());
                alertDialog.setView(view);
                alertDialog.show();
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

