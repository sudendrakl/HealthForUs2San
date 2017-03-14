package bizapps.com.healthforusDoc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import bizapps.com.healthforusDoc.R;
import bizapps.com.healthforusDoc.utills.Constants;

public class WebActivity extends AppCompatActivity {

  private WebView webView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_web);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    String title = getIntent().getStringExtra(Constants.IntentExtra.TITLE);
    getSupportActionBar().setTitle(
        (TextUtils.isEmpty(title) ? getString(R.string.app_name) : title));

    webView = (WebView) findViewById(R.id.webview);
    setUpWebView();
    String url = getIntent().getStringExtra(Constants.IntentExtra.URL);

    webView.loadUrl(url);
  }

  private void setUpWebView() {
    WebSettings settings = webView.getSettings();
    String appCachePath = this.getCacheDir().getAbsolutePath();

    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setGeolocationEnabled(true);
    settings.setAllowFileAccess(true);
    settings.setAppCachePath(appCachePath);
    settings.setAppCacheEnabled(true);

    webView.setWebViewClient(new WebViewClient() {

      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);

        return true;
      }
    });
  }

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
    if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
      //if Back key pressed and webview can navigate to previous page
      webView.goBack();
      // go back to previous page
      return true;
    } else {
      finish();
      // finish the activity
    }
    return super.onKeyDown(keyCode, event);
  }
}
