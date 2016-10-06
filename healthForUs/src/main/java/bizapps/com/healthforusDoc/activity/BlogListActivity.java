package bizapps.com.healthforusDoc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ListView;
import android.widget.TextView;
import bizapps.com.healthforusDoc.R;

public class BlogListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);
        stockData();
    }

    void stockData() {
      TextView textView1 = (TextView) findViewById(R.id.different_colors_indicate_different_nutrients);
      textView1.setText(Html.fromHtml(getString(R.string.different_colors_indicate_different_nutrients)));

      TextView textView2 = (TextView) findViewById(R.id.enhance_your_brain_power_with_kapalabhati_yoga);
      textView2.setText(Html.fromHtml(getString(R.string.enhance_your_brain_power_with_kapalabhati_yoga)));

      TextView textView3 = (TextView) findViewById(R.id.good_sleep_is_a_key_to_happy_life);
      textView3.setText(Html.fromHtml(getString(R.string.good_sleep_is_a_key_to_happy_life)));

      TextView textView4 = (TextView) findViewById(R.id.what_are_the_benefits_of_barefoot_training);
      textView4.setText(Html.fromHtml(getString(R.string.what_are_the_benefits_of_barefoot_training)));

      TextView textView5 = (TextView) findViewById(R.id.why_breakfast_is_crucial_for_us);
      textView5.setText(Html.fromHtml(getString(R.string.why_breakfast_is_crucial_for_us)));
    }

}
