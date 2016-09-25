package bizapps.com.healthforusDoc.data;

import lombok.Data;
import lombok.ToString;

/**
 * Created by sudendra.kamble on 22/09/16.
 */

@Data
@ToString
public class GcmUpdateResponseDto {
  boolean status;
  String response;
  String error;
  int code = -1;
}
