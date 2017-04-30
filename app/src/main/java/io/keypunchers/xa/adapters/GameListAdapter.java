package io.keypunchers.xa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import io.keypunchers.xa.R;
import io.keypunchers.xa.app.AchievementsActivity;
import io.keypunchers.xa.models.Game;


public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Game> mData;

    public GameListAdapter(Context context, ArrayList<Game> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.row_browse_games, parent, false);

        return new ViewHolder(view, mData, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Game item = mData.get(position);

        Picasso.with(mContext)
                .load(item.getArtwork())
                .noFade()
                .into(holder.mIvImage);

        holder.mTvTitle.setText(item.getTitle());
        holder.mTvAchCount.setText(String.format(Locale.US, "%s achievements", item.getAchCount()));
        holder.mTvGsCount.setText(String.format(Locale.US, "%s points", item.getGsCount()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mIvImage;
        TextView mTvTitle;
        TextView mTvAchCount;
        TextView mTvGsCount;
        private Context mContext;
        private ArrayList<Game> mData;

        public ViewHolder(View itemView, ArrayList<Game> data, Context context) {
            super(itemView);

            mContext = context;
            mData = data;

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_games_cover);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_games_title);
            mTvAchCount = (TextView) itemView.findViewById(R.id.tv_games_ach);
            mTvGsCount = (TextView) itemView.findViewById(R.id.tv_games_gs_count);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View p1) {
            mContext.startActivity(new Intent(mContext, AchievementsActivity.class).putExtra("game_permalink", mData.get(getAdapterPosition()).getGamePermalink()));
        }
    }
}
