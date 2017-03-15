package bizapps.com.healthforusDoc.utills;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import bizapps.com.healthforusDoc.BZAppApplication;
import com.facebook.imageutils.BitmapUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by venkatat on 4/25/2016.
 */
public class Utility {
  private static long MAX_IMG_SIZE = 1024 * 1024;

  public static void hideKeyboard(Activity context) {
    try {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    } catch (Exception e) {
    }
  }

  public static void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      // pre-condition
      return;
    }

    int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      if (listItem instanceof ViewGroup) {
        listItem.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      }
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.setLayoutParams(params);
  }

  public static void uploadImage(String url, HashMap<String, String> formData, HashMap<String, File> files,
      okhttp3.Callback callback) {
    MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
    if (formData != null && formData.size() > 0) {
      for (String key : formData.keySet()) {
        requestBodyBuilder.addFormDataPart(key, formData.get(key));
      }
    }
    if (files != null && files.size() > 0) {
      for (String key : files.keySet()) {
        Bitmap sourceBitmap = BitmapFactory.decodeFile(files.get(key).getAbsolutePath());
        int size = BitmapUtil.getSizeInBytes(sourceBitmap);
        float factor = size / MAX_IMG_SIZE;
        if (factor > 1) {
          sourceBitmap = Bitmap.createScaledBitmap(sourceBitmap, (int) (sourceBitmap.getWidth() / Math.sqrt(factor)),
              (int) (sourceBitmap.getHeight() / Math.sqrt(factor)), true);
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        requestBodyBuilder.addFormDataPart(key, files.get(key).getName(),
            RequestBody.create(MultipartBody.FORM, stream.toByteArray()));
      }
    }
    RequestBody requestBody = requestBodyBuilder.build();
    Request request = new Request.Builder().url(url).post(requestBody).build();
    BZAppApplication.getInstance().getOkHttpClient().newCall(request).enqueue(callback);
  }
}
