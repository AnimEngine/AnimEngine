package AnimEngine.mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import AnimEngine.mobile.R;
import AnimEngine.mobile.classes.Comment;


//import AnimEngine.mobile.utils.Anime;

public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.MyViewHolder> {
    //logic
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<Comment> mComments;

    public CommentRVAdapter(Context mContext, LayoutInflater layoutInflater, ArrayList<Comment> mComments, boolean isCreator) {
        this.mContext = mContext;
        this.layoutInflater = layoutInflater;
        this.mComments = mComments;
    }

    @Override
    public int getItemCount() {
        return this.mComments.size();
    }

    @NonNull
    @Override
    public CommentRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInfalter = LayoutInflater.from(mContext);
        view = mInfalter.inflate(R.layout.linear_item_comment,parent,false);
        return new CommentRVAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CommentRVAdapter.MyViewHolder viewHolder, final int position) {
        viewHolder.name.setText(mComments.get(position).getAuthorName());
        viewHolder.body.setText(mComments.get(position).getContent());
        viewHolder.ratingBar.setRating(mComments.get(position).getStars());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView body;
        public RatingBar ratingBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.text_view_name_comment);
            this.body = itemView.findViewById(R.id.text_view_body_comment);
            this.ratingBar = itemView.findViewById(R.id.rating_comment);
        }
    }



}

