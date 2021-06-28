package xyz.realraec.nomeotesttechnique;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.NewsItemViewHolder> {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private final Context context;
    private final ArrayList<NewsItem> newsList;
    private View view;

    public NewsItemAdapter(Context context, ArrayList<NewsItem> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public class NewsItemViewHolder extends RecyclerView.ViewHolder {

        public TextView idView;
        public TextView titleView;
        public TextView secondsSince1970View;
        public TextView descriptionView;
        public ImageView thumbnailView;

        public NewsItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            idView = itemView.findViewById(R.id.text_view_id);
            titleView = itemView.findViewById(R.id.text_view_title);
            secondsSince1970View = itemView.findViewById(R.id.text_view_date);
            descriptionView = itemView.findViewById(R.id.text_view_description);
            thumbnailView = itemView.findViewById(R.id.image_view);
        }
    }


    @NonNull
    @NotNull
    @Override
    public NewsItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.card_news_item, parent, false);
        return new NewsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewsItemViewHolder holder, int position) {
        NewsItem currentItem = newsList.get(position);

        int id = currentItem.getId();
        String title = currentItem.getTitle();
        int secondsSince1970 = currentItem.getSecondsSince1970();
        String description = currentItem.getDescription();
        String thumbnailURL = currentItem.getThumbnail();

        holder.idView.setText("" + id);
        holder.titleView.setText(title);
        holder.descriptionView.setText(description);
        Picasso.get().load(thumbnailURL).fit().into(holder.thumbnailView);

        long tempOld = secondsSince1970 * 1000L;
        long tempNow = System.currentTimeMillis();
        String temp = (String) DateUtils.getRelativeTimeSpanString(tempOld, tempNow, DateUtils.DAY_IN_MILLIS);
        Resources resources = view.getResources();
        // If there is a digit somewhere, it means that it is neither today nor yesterday
        // (The translation is handled by DateUtils)
        // So use dynamic String resource
        if (temp.matches(".*\\d.*")) {
            long diff = tempNow - tempOld;
            temp = resources.getString(R.string.postedwhen_xdays, diff / DAY_MILLIS);
        }
        holder.secondsSince1970View.setText(temp);

    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

}
