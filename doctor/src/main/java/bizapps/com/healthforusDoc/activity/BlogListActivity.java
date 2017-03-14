package bizapps.com.healthforusDoc.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import bizapps.com.healthforusDoc.R;

public class BlogListActivity extends AppCompatActivity implements TextView.OnClickListener{

  int maxLines = 5;
  String more = "...more\n";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_blog_list);
    stockData();
  }

  void stockData() {
    TextView textView1 = (TextView) findViewById(R.id.different_colors_indicate_different_nutrients);
    setLayoutListner(textView1);
    textView1.setOnClickListener(this);
    textView1.setTag(R.id.id2, getString(R.string.different_colors_indicate_different_nutrients));

    TextView textView2 = (TextView) findViewById(R.id.enhance_your_brain_power_with_kapalabhati_yoga);
    setLayoutListner(textView2);
    textView2.setOnClickListener(this);
    textView2.setTag(R.id.id2, getString(R.string.enhance_your_brain_power_with_kapalabhati_yoga));

    TextView textView3 = (TextView) findViewById(R.id.good_sleep_is_a_key_to_happy_life);
    setLayoutListner(textView3);
    textView3.setOnClickListener(this);
    textView3.setTag(R.id.id2, getString(R.string.good_sleep_is_a_key_to_happy_life));

    TextView textView4 = (TextView) findViewById(R.id.what_are_the_benefits_of_barefoot_training);
    setLayoutListner(textView4);
    textView4.setOnClickListener(this);
    textView4.setTag(R.id.id2, getString(R.string.what_are_the_benefits_of_barefoot_training));

    TextView textView5 = (TextView) findViewById(R.id.why_breakfast_is_crucial_for_us);
    setLayoutListner(textView5);
    textView5.setOnClickListener(this);
    textView5.setTag(R.id.id2, getString(R.string.why_breakfast_is_crucial_for_us));
  }

  private void setLayoutListner(final TextView textView) {
    textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
          textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }

        final Layout layout = textView.getLayout();

        // Loop over all the lines and do whatever you need with
        // the width of the line
        int end = 0, line1 = 0;
        for (int i = 0; i < maxLines; i++) {
          if (i == 0) line1 = layout.getLineVisibleEnd(0);
          end += layout.getLineEnd(0);
        }
        textView.setTag(R.id.id1, true); //initially expanded as nothing done to textview
        textView.setTag(R.id.id3, end);
        textView.setTag(R.id.id4, line1);
        toggleAndSetText(textView);
      }
    });
  }

  private void toggleAndSetText(final TextView tv) {
    tv.postDelayed(new Runnable() {
      @Override public void run() {
        boolean isExpanded = (boolean) tv.getTag(R.id.id1);
        String text = (String) tv.getTag(R.id.id2);
        int end = (int) tv.getTag(R.id.id3);
        int line1 = (int) tv.getTag(R.id.id4);
        if (!isExpanded) {
          SpannableString content = new SpannableString(text);
          content.setSpan(new StyleSpan(Typeface.BOLD), 0, line1, 0);
          content.setSpan(new StyleSpan(Typeface.NORMAL), line1 + 1, content.length(), 0);
          tv.setText(content);
        } else {
          SpannableString content = new SpannableString(text.subSequence(0, end - more.length()) + more);
          content.setSpan(new StyleSpan(Typeface.BOLD), 0, line1, 0);
          content.setSpan(new StyleSpan(Typeface.NORMAL), line1 + 1, end - more.length(), 0);
          content.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), end - more.length(), content.length(), 0);
          content.setSpan(new ForegroundColorSpan(Color.GRAY), end - more.length(), content.length(), 0);
          tv.setText(content);
        }
        tv.setTag(R.id.id1, !isExpanded);
      }
    }, 50);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.blog_menu, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    //TODO: share what
    if (item.getItemId() == R.id.menu_share) {
      //Intent sendIntent = new Intent();
      //sendIntent.setAction(Intent.ACTION_SEND);
      //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
      //sendIntent.setType("text/plain");
      //startActivity(sendIntent);
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void onClick(View view) {
    toggleAndSetText((TextView) view);
  }
}
