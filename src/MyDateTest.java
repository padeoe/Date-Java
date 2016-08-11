import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by padeoe on 2016/8/11.
 */
public class MyDateTest extends TestCase {


    public MyDateTest() throws Exception {
    }

    public void testSub() throws Exception {
        System.out.println(MyDate.init(9999,8,11).sub(MyDate.init(2016,2,2)));
        System.out.println(MyDate.init(2016,8,11).add(0));
        MyDate aaa = MyDate.init(1995, 9, 11);
        MyDate bbb = MyDate.init(2013, 6, 7);
        System.out.println(bbb.sub(aaa));
        for (int i = 0; i <= 1000; i++) {
            MyDate myDate = MyDate.init(8800, 9, 11);
            MyDate tmp = MyDate.init(8800 + i, 9, 13);
            int result = tmp.sub(myDate);
            assertEquals(myDate.add(result).toString(), tmp.toString());
        }

    }

    public void testAdd() throws Exception {
        MyDate aaa = MyDate.init(1995, 9, 11);
        Calendar calendar = new GregorianCalendar();
        for (int i = 1; i <= 1000; i++) {
            aaa.set(1995, 9, 11);
            aaa.add(i);
            calendar.set(1995, 8, 11);
            calendar.add(Calendar.DATE, i);
            Date date = calendar.getTime();
            assertEquals(aaa.getYear(), calendar.get(Calendar.YEAR));
            assertEquals(aaa.getMonth(), date.getMonth() + 1);
            assertEquals(aaa.getDay(), date.getDate());

        }
    }

}