package io.keypunchers.xa.loaders;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import io.keypunchers.xa.misc.Common;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import java.util.Locale;
import android.support.v4.util.Pair;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SubmitArticleCommentLoader extends AsyncTaskLoader<Pair<Boolean, String>> {
    private final String BASE_URL;
	private String mComment;
	private boolean isCached;

    public SubmitArticleCommentLoader(Context context, String url, String comment) {
        super(context);
        BASE_URL = url;
		mComment = comment;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

		if (isCached) {
			super.deliverResult(Pair.create(false, "Cached"));
		} else {
            forceLoad();
		}
    }

    @Override
    public void deliverResult(Pair<Boolean, String> data) {
        if (isStarted()) {
			isCached = true;
            super.deliverResult(data);
        }
    }

    @Override
    public Pair<Boolean, String> loadInBackground() {

        try {
			SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getContext());
			
            String url = Common.BASE_URL + "/forum/login.php";
            String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36";

            Connection.Response response = Jsoup.connect(url).userAgent(userAgent)
				.method(Connection.Method.GET)
				.execute();

            response = Jsoup.connect(url)
				.cookies(response.cookies())
				.data("vb_login_username", mPref.getString("XA_USERNAME", null))
				.data("vb_login_password", mPref.getString("XA_PASSWORD", null))
				.data("do", "login")
				.userAgent(userAgent)
				.method(Connection.Method.POST)
				.followRedirects(true)
				.execute();

            if (response.cookies().get("bbsessionhash") == null) {
                return Pair.create(false, "Please check your credentials.");
            }

            Jsoup.connect("http://www.xboxachievements.com/postComment.php?type=360news")
				.data("newsID", Common.getNewsCommenstId(BASE_URL))
				.data("username", "CS15")
				.data("comment", String.format(Locale.US, "%s%s%sVia XA Android App", mComment, System.getProperty("line.separator"), System.getProperty("line.separator")))
				.data("submit", "Submit")
				.cookies(response.cookies())
				.post();

            return Pair.create(true, null);
        } catch (Exception ex) {
            return Pair.create(false, ex.getMessage());
        }
    }
}
