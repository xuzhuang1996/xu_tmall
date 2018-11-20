package tmall.xu_util;

import java.io.UnsupportedEncodingException;

public class XuEncodeUtil {
	public static String getNewString(String s) {
		try {
			return new String (s.getBytes ("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}
}
