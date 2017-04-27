package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.misc.Enums;
import io.keypunchers.xa.models.GameDetails;

public class GameDetailsLoader extends AsyncTaskLoader<GameDetails> {
    private GameDetails mData;
    private String BASE_URL;

    public GameDetailsLoader(Context context, String url) {
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
    public void deliverResult(GameDetails data) {
        mData = data;
        if (isStarted() && mData != null) {
            super.deliverResult(mData);
        }
    }

    @Override
    public GameDetails loadInBackground() {
        try {
            mData = new GameDetails();

            Document document = Jsoup.connect(BASE_URL).get();

            Element game_details_root = document.getElementsByClass("men_h_content")
                    .first()
                    .select("tr")
                    .first();

            String game_banner = null;
            Element banner = document.getElementsByClass("blr_main")
                    .eq(1)
                    .select("img")
                    .first();

            if (banner != null)
                game_banner = banner.attr("abs:src");

            String game_title = document.getElementsByClass("tt")
                    .first()
                    .text()
                    .trim();

            String game_image_url = game_details_root.select("td:eq(0) > img")
                    .attr("abs:src")
                    .replace("/game/", "/achievements/");

            String game_developer = game_details_root.select("td:eq(1) > div:eq(0) > a")
                    .text()
                    .trim();

            String game_publisher = game_details_root.select("td:eq(1) > div:eq(1) > a")
                    .text()
                    .trim();

            ArrayList<String> game_genres = new ArrayList<>();
            Elements genres = game_details_root.select("td:eq(1) > div:eq(3) > a");

            for (Element element : genres)
                game_genres.add(element.text().trim());

            Map<Enums.Country, String> game_release_dates = new HashMap<>();
            Elements release_dates = game_details_root.select("td:eq(1) > div:eq(4) > img");

            for (Element element : release_dates) {
                if (element.attr("alt").equals("US"))
                    game_release_dates.put(Enums.Country.US, element.nextSibling().toString().trim());
                if (element.attr("alt").equals("Europe"))
                    game_release_dates.put(Enums.Country.EUROPE, element.nextSibling().toString().trim());
                if (element.attr("alt").equals("Japan"))
                    game_release_dates.put(Enums.Country.JAPAN, element.nextSibling().toString().trim());
            }

            mData.setTitle(game_title);
            mData.setImageUrl(game_image_url);
            mData.setDeveloper(game_developer);
            mData.setPublisher(game_publisher);
            mData.setGenres(game_genres);
            mData.setReleaseDates(game_release_dates);
            mData.setBanner(Common.imageUrlThumbToMed(game_banner));




            return mData;
        } catch (Exception e) {
            Log.e(GamesListLoader.class.getSimpleName(), e.getMessage());
            return null;
        }
    }
}