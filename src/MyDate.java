import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyDate {
    private int year;
    private int month;
    private int day;
    private Boolean isLeapYear = null;
    private static final int DAY_ARRAY[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private MyDate(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
    }

    /**
     * 获得类实例
     *
     * @param y
     * @param m
     * @param d
     * @return
     * @throws Exception
     */
    public static MyDate init(int y, int m, int d) throws Exception {
        if (y < 1 || m < 1 || m > 12 || d < 1) {
            throw new Exception("不合法的日期");
        }
        MyDate myDate = new MyDate(y, m, d);
        if (d > myDate.maxDay()) {
            throw new Exception("不合法的日期");
        }
        return myDate;
    }

    /**
     * 修改类实例
     *
     * @param year
     * @param month
     * @param day
     */
    public void set(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        isLeapYear = null;
    }

    /**
     * 对日期进行后移操作
     *
     * @param duration 天数
     * @return 对象年月日将被修改
     */
    public MyDate add(int duration) {
        int sum = 0;
        for (int i = 0; i < month - 1; sum += DAY_ARRAY[i], i++) ;
        if (month > 2 && isLeapYear())
            sum++;
        int tmp = duration + sum + (day - 1);
        return new_year_add(tmp);
    }

    /**
     * 计算日期之间的差值
     *
     * @param myDate
     * @return
     */
    public int sub(MyDate myDate) {
        return this.day_t() - myDate.day_t();
    }

    /**
     * @return
     */
    private int maxDay() {
        if (isLeapYear() && month == 2)
            return 29;
        else
            return DAY_ARRAY[month - 1];
    }

    public boolean isLeapYear() {
        return isLeapYear == null ? isLeapYear = isLeapYear(year) : isLeapYear;
    }

    private static boolean isLeapYear(int year) {
        return (year & 3) == 0 ? (year % 100 != 0 || year % 400 == 0) : false;
    }


    public int day_t() {
        int sum = 0;
        for (int i = 0; i < month - 1; sum += DAY_ARRAY[i], i++) ;
        if (isLeapYear() && month > 2) sum++;
        return 365 * (year - 1) + numOfLeap_t(year) + sum + day - 1;
    }


    public static void main(String[] args) {
        System.out.println("start");
        long time1 = System.currentTimeMillis();
        Calendar a = new GregorianCalendar(1995, 8, 11);
        final int n = 10000000;
        MyDate myDate1 = null;
        try {
            myDate1 = MyDate.init(1995, 9, 11);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //mine
        for (int i = 2015; i <= n; i++) {
            {
                myDate1.set(1995, 9, 11);
                myDate1.add(i);
            }
        }

        long time2 = System.currentTimeMillis();
        System.out.println("我的代码耗时" + (time2 - time1) + "ms");
        time1 = time2;
        //jdk
        for (int i = 2015; i < n; i++) {
            a.set(1995, 8, 11);
            a.add(Calendar.DATE, i);
        }

        time2 = System.currentTimeMillis();
        System.out.println("jdk耗时" + (time2 - time1) + "ms");

    }


    /**
     * 计算这一年的1月1日加上一定天数后的日期
     *
     * @param duration
     * @return
     */
    private MyDate new_year_add(int duration) {
        if (duration < (isLeapYear() ? 366 : 365)) {
            return new_year_add_year_not_change(duration);
        } else {
            if (duration >= 146097) {
                int year2 = this.year + (duration / 146097) * 400;
                int tmp_left_day = duration % 146097;
                this.setYear(year2);
                return this.new_year_add_less_than_400_year(tmp_left_day);
            } else {
                return new_year_add_less_than_400_year(duration);
            }
        }
    }

    private MyDate new_year_add_less_than_400_year(int duration) {
        int year_tmp = year + duration / 365, day_left = duration % 365 - (numOfLeap_t(year_tmp) - numOfLeap_t(year));
        if (day_left < 0) {
            year_tmp -= 1;
            day_left = duration % 365 + 365 - (numOfLeap_t(year_tmp) - numOfLeap_t(year));
        }
        this.set(year_tmp, 1, 1);
       // assert day_left <= 365;
        return this.new_year_add_year_not_change(day_left);
    }

    /**
     * 如果增加天数的天数不会改变年份
     *
     * @param duration 天数
     * @return
     */
    private MyDate new_year_add_year_not_change(int duration) {
        int month,index;
        if (isLeapYear()) {
            int Msum_leap[] = {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335};
            month = getBinaryInsertIndex(duration, Msum_leap);
            index=Msum_leap[month - 1];
        } else {
            int Msum[] = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
            month = getBinaryInsertIndex(duration, Msum);
            index=Msum[month - 1];
        }
        this.setMonth(month);
        this.setDay(1 + (duration - index));
        return this;
    }

    private int getBinaryInsertIndex(int number, int array[]) {
        int start = 0, end = array.length - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (number < array[mid]) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
     //   assert start != 0;
        return start;
    }

    @Override
    public String toString() {
        return this.year + "-" + this.month + "-" + day;
    }

    /**
     * year 和1年之间的闰年的个数，不包含year这一年
     *
     * @param year
     * @return
     */
    private static int numOfLeap_t(int year) {
        return (year - 1) / 4 - (year - 1) / 100 + (year - 1) / 400;
    }

    public void setYear(int year) {
        this.year = year;
        isLeapYear = isLeapYear(year);
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
