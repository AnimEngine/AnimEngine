package AnimEngine.mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import AnimEngine.mobile.classes.Anime;
import AnimEngine.mobile.R;
import AnimEngine.mobile.classes.Comment;
import AnimEngine.mobile.classes.Fan;
import AnimEngine.mobile.classes.User;
import AnimEngine.mobile.classes.UserAndToken;
import AnimEngine.mobile.models.CreatorModel;
import AnimEngine.mobile.models.FanModel;
import AnimEngine.mobile.models.Model;


//import AnimEngine.mobile.utils.Anime;

public class CatalogRVAdapter extends RecyclerView.Adapter<CatalogRVAdapter.MyViewHolder> {
    //logic
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<Anime> mAnimes;

    boolean isCreator;
    UserAndToken user;
    Boolean is_clicked_like;
    Boolean is_clicked_dislike;
    TextView likes_text;
    TextView dislikes_text;
    EditText description;

    CreatorModel model;
    String token;
    Anime animeObj;
    Anime animeHolder;

    public CatalogRVAdapter(Context mContext, LayoutInflater layoutInflater, ArrayList<Anime> mAnimes, boolean isCreator, UserAndToken user) {
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
        view = mInfalter.inflate(R.layout.cardview_item_anime, parent, false);
        return new CatalogRVAdapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull CatalogRVAdapter.MyViewHolder viewHolder, final int position) {
        Picasso.get().load((this.mAnimes.get(position)).getImageURL()).into(viewHolder.imageButton);

        viewHolder.textView.setText(mAnimes.get(position).getName());

        viewHolder.imageButton.setOnClickListener(v -> {
            final AlertDialog alertDialog = (new AlertDialog.Builder(mContext)).create();
            View view = layoutInflater.inflate(R.layout.dialog_anime_fan, null);

            ((TextView) view.findViewById(R.id.text_view_title_dialog_anime)).setText((mAnimes.get(position)).getName());
            EditText commentText = view.findViewById(R.id.text_edit_comment);

            likes_text = view.findViewById(R.id.text_likes_anime);
            dislikes_text = view.findViewById(R.id.text_dislikes_anime);


            ImageButton like = view.findViewById(R.id.button_like);
            is_clicked_like = false;

            ImageButton dislike = view.findViewById(R.id.button_dislike);
            is_clicked_dislike = false;
            token = user.getToken();
            model = new CreatorModel();

            TextView textView = view.findViewById(R.id.text_view_description_dialog);
            textView.setText((mAnimes.get(position)).getDescription());

            FanModel fanModel = new FanModel();
            RatingBar ratingBar;
            float rating;

            if (isCreator) {

                view.findViewById(R.id.layout_add_comment_dialog).setVisibility(View.GONE);

                view.findViewById(R.id.button_edit_anime_information).setVisibility(View.VISIBLE);

                view.findViewById(R.id.button_edit_anime_information).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog alertDialog1 = (new AlertDialog.Builder(mContext)).create();
                        View view1 = layoutInflater.inflate(R.layout.activity_edit_anime_creator, null);

                        view1.findViewById(R.id.button_edit_anime_information).setOnClickListener(param2View -> alertDialog1.dismiss());
                        alertDialog1.setView(view1);
                        alertDialog1.show();
                        ((TextView) view1.findViewById(R.id.text_edit_name_edit_anime)).setText((mAnimes.get(position)).getName());

                        view1.findViewById(R.id.button_edit_anime_information).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                TextInputLayout min = view1.findViewById(R.id.text_edit_information_edit_anime);
                                description =  min.getEditText();
                                assert description != null;
                                String newDescription = description.getText().toString();
                                if (newDescription.equals("")){
                                    Toast.makeText(mContext, "You can't set to empty description", Toast.LENGTH_SHORT).show();
                                    alertDialog1.hide();

                                }

                                animeObj = new Anime(newDescription);
                                animeHolder = mAnimes.get(viewHolder.getBindingAdapterPosition());
                                animeObj.setName(animeHolder.getName());
                                model.editAnime(animeObj, token);
                                alertDialog1.hide();

                            }
                        });

                    }
                });

                textView.setText((mAnimes.get(position)).getDescription());
            } else {

                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(is_clicked_like){
                            like.setImageResource(R.drawable.icon_like_anime_unfilled);
                            animeHolder = mAnimes.get(viewHolder.getBindingAdapterPosition());
                            int currentLikes = animeHolder.getLikeCounter();
                            int currentDisLikes = animeHolder.getDislikeCounter();

                            currentLikes = currentLikes-1;
                            animeObj = new Anime(currentLikes, currentDisLikes);
                            animeObj.setName(animeHolder.getName());
                            model.editAnime(animeObj, token);

                            animeHolder.setLikeCounter(currentLikes);
                            likes_text.setText(String.format("%d", currentLikes));
                            is_clicked_like = false;
                        }
                        else {
                            if (is_clicked_dislike) {
                                dislike.setImageResource(R.drawable.icon_dislike_anime_unfilled);
                                animeHolder =  mAnimes.get(viewHolder.getBindingAdapterPosition());
                                int currentLikes = animeHolder.getLikeCounter();
                                int currentDislikes = animeHolder.getDislikeCounter();

                                currentDislikes = currentDislikes-1;
                                animeObj = new Anime(currentLikes, currentDislikes);
                                animeObj.setName(animeHolder.getName());
                                model.editAnime(animeObj, token);

                                animeHolder.setDislikeCounter(currentDislikes);
                                dislikes_text.setText(String.format("%d", currentDislikes));
                                is_clicked_dislike = false;
                            }

                            like.setImageResource(R.drawable.icon_like_anime_filled);
                            animeHolder = mAnimes.get(viewHolder.getBindingAdapterPosition());
                            int currentLikes = animeHolder.getLikeCounter();
                            int currentDislikes = animeHolder.getDislikeCounter();

                            currentLikes = currentLikes+1;
                            animeObj = new Anime(currentLikes, currentDislikes);
                            animeObj.setName(animeHolder.getName());
                            model.editAnime(animeObj, token);

                            animeHolder.setLikeCounter(currentLikes);
                            likes_text.setText(String.format("%d", currentLikes));
                            is_clicked_like = true;
                        }
                    }
                });

                dislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(is_clicked_dislike){
                            dislike.setImageResource(R.drawable.icon_dislike_anime_unfilled);
                            animeHolder = mAnimes.get(viewHolder.getBindingAdapterPosition());
                            int currentLikes = animeHolder.getLikeCounter();
                            int currentDislikes = animeHolder.getDislikeCounter();

                            currentDislikes = currentDislikes-1;
                            animeObj = new Anime(currentLikes, currentDislikes);
                            model.editAnime(animeObj, token);

                            animeHolder.setDislikeCounter(currentDislikes);
                            dislikes_text.setText(String.format("%d", currentDislikes));
                            is_clicked_dislike = false;
                        }
                        else {
                            if (is_clicked_like) {
                                like.setImageResource(R.drawable.icon_like_anime_unfilled);
                                animeHolder = mAnimes.get(viewHolder.getBindingAdapterPosition());
                                int currentLikes = animeHolder.getLikeCounter();
                                int currentDislikes = animeHolder.getDislikeCounter();

                                currentLikes = currentLikes-1;
                                animeObj = new Anime(currentLikes, currentDislikes);
                                model.editAnime(animeObj, token);

                                animeHolder.setLikeCounter(currentLikes);
                                likes_text.setText(String.format("%d", currentLikes));
                                is_clicked_like = false;
                            }

                            dislike.setImageResource(R.drawable.icon_dislike_anime_filled);
                            animeHolder = mAnimes.get(viewHolder.getBindingAdapterPosition());
                            int currentLikes = animeHolder.getLikeCounter();
                            int currentDislikes = animeHolder.getDislikeCounter();

                            currentDislikes = currentDislikes+1;
                            animeObj = new Anime(currentLikes, currentDislikes);
                            model.editAnime(animeObj, token);

                            animeHolder.setDislikeCounter(currentDislikes);
                            dislikes_text.setText(String.format("%d", currentDislikes));
                            is_clicked_dislike = true;
                        }
                    }
                });


                TextView commenterName = view.findViewById(R.id.text_view_name_add_comment_dialog);
                Fan fan = ((Fan) user.getUser());
                commenterName.setText(String.format("%s %s", fan.getFName(), fan.getLName()));


                ratingBar = view.findViewById(R.id.rating_commend_add_dialog);
                ratingBar.setNumStars(5);
                rating = ratingBar.getRating();

                ImageButton uploadComment = view.findViewById(R.id.button_send_comment_dialog);
                uploadComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = commentText.getText().toString();

                        Toast.makeText(mContext, "bidning adapter "+viewHolder.getBindingAdapterPosition(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(mContext, "layout pos "+viewHolder.getLayoutPosition(), Toast.LENGTH_SHORT).show();


                        Comment comment = new Comment( (mAnimes.get(viewHolder.getBindingAdapterPosition())).getName(), "", content, rating);
                        fanModel.uploadComment(comment);
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

