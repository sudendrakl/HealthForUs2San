package bizapps.com.healthforusDoc.utills;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;

public class CustomMultipartRequest extends Request<String> {

	  private HttpEntity mHttpEntity;
	  private MultipartEntityBuilder builder = MultipartEntityBuilder.create();

	  private final Response.Listener<String> mListener;
	  private final Map<String, String> mKeyValue;
	  private final File[] fileArray;
	  private final File mFile;

	  public CustomMultipartRequest(String url, Map<String, String> params, Response.Listener<String> listener, File[] fileArray, File mFile,Response.ErrorListener errorListener) {
	    super(Method.POST, url, errorListener);

	    mListener = listener;
	    mKeyValue = params;
	    this.fileArray = fileArray;
		  this.mFile = mFile;
	    buildMultipartEntity();
	  }

	  private void buildMultipartEntity() {

	    for (String entry : mKeyValue.keySet()) {
	      try {
			builder.addPart(entry, new StringBody(mKeyValue.get(entry).toString()));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
		  String mString="";
		  int i = 1;
	    for(File imgFile : fileArray){
	    	if(imgFile != null) {
				mString = String.valueOf(i);
				builder.addPart("photo_of_hospital" + mString, new FileBody(Utility.saveBitmapToFile(imgFile)));

				i++;
			}
	    }

		  if(mFile!=null){
			  builder.addPart("certificate_file", new FileBody(Utility.saveBitmapToFile(mFile)));
		  }
	    
	    mHttpEntity = builder.build();
	  }

	  @Override
	  public String getBodyContentType() {
	    return mHttpEntity.getContentType().getValue();
	  }

	  @Override
	  public byte[] getBody() throws AuthFailureError {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    try {
	      mHttpEntity.writeTo(bos);
	    } catch (IOException e) {
	      VolleyLog.e("IOException writing to ByteArrayOutputStream");
	    }
	    return bos.toByteArray();
	  }

	  @Override
	  protected Response<String> parseNetworkResponse(NetworkResponse response) {
		  String jsonString = "";
		  try {
			jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return Response.success(jsonString, getCacheEntry());
	  }

	  @Override
	  protected void deliverResponse(String response) {
	    mListener.onResponse(response);
	  }
	} 