package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

import io.keypunchers.xa.models.UpcomingGame;


public class UpcomingGamesLoader extends AsyncTaskLoader<ArrayList<UpcomingGame>> {

    private final String BASE_URL;
    private ArrayList<UpcomingGame> mData = new ArrayList<>();

    public UpcomingGamesLoader(Context context, String url) {
        super(context);
        BASE_URL = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (mData != null)
            super.deliverResult(mData);
        else
            forceLoad();
    }

    @Override
    public void deliverResult(ArrayList<UpcomingGame> data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }

    @Override
    public ArrayList<UpcomingGame> loadInBackground() {
        try {
            Elements elements = Jsoup.connect(BASE_URL)
                    .get()
                    .getElementsByClass("divtext")
                    .eq(0)
                    .select("tr:gt(1)");

            for (Element element : elements) {
                String title = element.select("a").text();
                String date = element.select("td:first-child").text();
                String url = element.select("a").attr("abs:href");

                UpcomingGame game = new UpcomingGame();
                game.setTitle(title);
                game.setReleaseDate(date);
                game.setGamePermalink(url);

                mData.add(game);
            }

            return mData;
        } catch (Exception e) {
            Log.e("Upcoming Games Loader", e.getMessage());
            return null;
        }
    }
}
