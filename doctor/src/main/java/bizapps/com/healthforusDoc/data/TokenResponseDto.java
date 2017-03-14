package bizapps.com.healthforusDoc.data;

import lombok.Data;
import lombok.ToString;

/**
 * Created by sudendra.kamble on 22/09/16.
 */
@Data
@ToString
public class TokenResponseDto {
  boolean success;
  String token;
  String message;
  int code = -1;
}
