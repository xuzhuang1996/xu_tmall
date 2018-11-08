package tmall.xu_util;

//主要是用于java.util.Date类与java.sql.Timestamp 类的互相转换。
//MySQL中的日期格式里保存时间信息，必须使用datetime类型的字段，
//而jdbc要获取datetime类型字段的信息，需要采用java.sql.Timestamp来获取，否则只会保留日期信息，而丢失时间信息。
public class DateUtil {
	public static java.sql.Timestamp d2t(java.util.Date d) {
        if (null == d)
            return null;
        return new java.sql.Timestamp(d.getTime());
    }
 
    public static java.util.Date t2d(java.sql.Timestamp t) {
        if (null == t)
            return null;
        return new java.util.Date(t.getTime());
    }
}
