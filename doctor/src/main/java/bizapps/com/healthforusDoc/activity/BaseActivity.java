package bizapps.com.healthforusDoc.activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import bizapps.com.healthforusDoc.BZAppApplication;
import bizapps.com.healthforusDoc.utills.PrefManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class BaseActivity extends AppCompatActivity {

  public PrefManager getAppSharedPreference() {
    PrefManager prefs = new PrefManager(getApplicationContext());
    return prefs;
  }

  public String getRealPathFromURI(Context context, Uri contentUri) {
    Cursor cursor = null;
    try {
      String[] proj = { MediaStore.Images.Media.DATA };
      cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }

  protected void showMessage(final String message) {
    BaseActivity.this.runOnUiThread(new Runnable() {
      @Override public void run() {
        Toast.makeText(BZAppApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
      }
    });
  }

  protected void setImage(SimpleDraweeView imageView, String uri) {
    setImage(imageView, Uri.parse(uri));
  }

  protected void setImage(SimpleDraweeView imageView, Uri uri) {
    int width = imageView.getMeasuredWidth(), height = imageView.getMeasuredWidth();
    ImageRequest request =
        ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(new ResizeOptions(width, height)).build();
    DraweeController controller =
        Fresco.newDraweeControllerBuilder().setOldController(imageView.getController()).setImageRequest(request).build();
    imageView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
    imageView.setController(controller);
  }
}
