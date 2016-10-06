package bizapps.com.healthforusDoc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import android.widget.Toast;
import bizapps.com.healthforusDoc.R;

public class BlogActivity extends BaseActivity implements OnMenuItemClickListener  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog);
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
//		case R.id.item_change_password:
//			Toast.makeText(this, "Comedy Clicked", Toast.LENGTH_SHORT).show();
//			return true;
		case R.id.item_logout:
			getAppSharedPreference().setUserRemembered(false);
			Intent login = new Intent(this, LoginActivity.class);
			login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
			startActivity(login);
			finish();
			return true;
		/*case R.id.item_schedule_date:
			return true;*/
		}
		return false;
	}
}
