package com.webchat.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 日期的工具类
 */
public final class DateUtils {

    public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY = "yyyy";
    public static final String MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";

    public static final String YYYYMMDDHHMMSSSS = "yyyyMMddHHmmssSS";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String YYYYMMDDHH = "yyyyMMddHH";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYMMDD = "yyMMdd";
    public static final String HHMM = "HH:mm";

    public static final String YYYYMMDDHHMM_SPLASH = "yyyy/MM/dd HH:mm";
    public static final String MM_DD_YYYY = "MM/dd/yyyy";

    public static final String YYYYMMDD_CN = "yyyy年MM月dd日";
    public static final String YYYYMMDD_CN_SP = "yyyy 年 MM 月 dd 日";
    public static final String MM_DD_CN = "M月d日";

    public static final String DATE_BEGIN = " 00:00:00";
    public static final String DATE_END = " 23:59:59";

    public static String today;
    public static String yesterday;

    private DateUtils() {
    }

    public static Date getCurrentDate(String... format) {
        Date date = new Date();

        if (format != null && format.length > 0) {
            return getString2Date(getCurrentFormatDate(format[0]));
        }

        return date;
    }

    public static Date getYesterdayDate() {
        return new Date(getCurrentTimeMillis() - 0x5265c00L);
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String getCurrentFormatDate(String formatDate) {
        Date date = getCurrentDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);

        return simpleDateFormat.format(date);
    }

    public static String getCurrentFormatDate() {
        Date date = getCurrentDate();
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
    }

    public static void resetToday() {
        String todayStr = getDate2String("yyyy-MM-dd", getCurrentDate());

        if ((today == null) || !today.equals(todayStr)) {
            today = todayStr;
            yesterday = getDate2String("yyyy-MM-dd", getYesterdayDate());
        }
    }

    public static final String getDate2String(String format, Date date) {
        if (date != null) {
            if (StringUtils.isEmpty(format)) {
                format = YYYY_MM_DD_HH_MM_SS;
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

            return simpleDateFormat.format(date);
        } else {
            return "";
        }
    }

    public static final String getTodayDateString() {

        return new SimpleDateFormat(YYYY_MM_DD).format(new Date());
    }

    public static String getDate2String(Date date) {
        return getDate2String("", date);
    }

    public static String getLong2ShortString(long l, String formatDate) {
        Date date = getLong2Date(l);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);

        return simpleDateFormat.format(date);
    }

    public static Date getString2Date(String format, String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        ParsePosition parseposition = new ParsePosition(0);

        return simpleDateFormat.parse(str, parseposition);
    }

    public static Date getString2Date(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
        ParsePosition parseposition = new ParsePosition(0);
        return simpleDateFormat.parse(str, parseposition);
    }

    public static Date getString2Date(String format, Locale locale, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);

        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getLong2Date(long l) {
        return new Date(l);
    }

    public static int getOffDays(long l) {
        return getOffDays(l, getCurrentTimeMillis());
    }

    public static int getOffDays(long from, long to) {
        return getOffMinutes(from, to) / 1440;
    }

    public static int getOffMinutes(long l) {
        return getOffMinutes(l, getCurrentTimeMillis());
    }

    public static int getOffMinutes(long from, long to) {
        return (int) ((to - from) / 60000L);
    }

    public static String getLastNQuarterBeginDate(int n) {
        return getLastNQuarterBeginDate(n, new SimpleDateFormat("yyyy-MM-dd"));
    }

    public static String getLastNQuarterBeginDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, n * 3);

        int q = getQuarter(c.get(Calendar.MONTH));

        c.set(Calendar.MONTH, 3 * (q - 1));
        c.set(Calendar.DATE, c.getActualMinimum(Calendar.DATE));
        return format.format(c.getTime());
    }

    public static String getLastNQuarterEndDate(int n) {
        return getLastNQuarterEndDate(n, new SimpleDateFormat("yyyy-MM-dd"));
    }

    private static int getQuarter(int month) {
        if (month <= 2 && month >= 0) {
            return 1;
        }
        if (month <= 5 && month >= 3) {
            return 2;
        }
        if (month <= 8 && month >= 6) {
            return 3;
        }
        if (month <= 11 && month >= 9) {
            return 4;
        }
        throw new IllegalStateException("月份错误");
    }

    public static String getLastNQuarterEndDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, n * 3);

        int q = getQuarter(c.get(Calendar.MONTH));

        c.set(Calendar.MONTH, 2 + 3 * (q - 1));

        c.set(Calendar.DATE, c.getMaximum(Calendar.DAY_OF_MONTH));

        return format.format(c.getTime());
    }

    public static String getLastNMonthBeginDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, n);
        c.set(Calendar.DATE, c.getMinimum(Calendar.DAY_OF_MONTH));
        return format.format(c.getTime());
    }

    public static String getLastNMonthBeginDate(int n) {
        return getLastNMonthBeginDate(n, new SimpleDateFormat("yyyy-MM-dd"));
    }

    public static String getLastNMonthEndDate(int n) {
        return getLastNMonthEndDate(n, new SimpleDateFormat("yyyy-MM-dd"));
    }

    public static String getLastNMonthEndDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, n);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));

        return format.format(c.getTime());
    }

    public static Date getBeforeNDayDate(long n, Date... date) {
        Date tmpDate = null;
        if (date != null && date.length > 0) {
            tmpDate = date[0];
        } else {
            tmpDate = getCurrentDate();
        }

        Long dateTime = tmpDate.getTime() - n * 24 * 60 * 60 * 1000L;

        return new Date(dateTime);
    }

    public static Date getNSeconBefore(long n, Date... date) {
        Date tmpDate = null;
        if (date != null && date.length > 0) {
            tmpDate = date[0];
        } else {
            tmpDate = getCurrentDate();
        }

        Long dateTime = tmpDate.getTime() - n * 1000;
        return new Date(dateTime);
    }

    public static String getLastNDayDate(int n, Date... date) {
        Date tmpDate = null;
        if (date != null && date.length > 0) {
            tmpDate = date[0];
        } else {
            tmpDate = getCurrentDate();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(tmpDate);
        c.add(Calendar.DAY_OF_YEAR, n);

        return getDate2String(c.getTime());
    }

    public static String getLastNDayDate(int n, String date) {
        return getLastNDayDate(n, getString2Date(date));
    }

    public static String getLastNMonthDate(int n, Date... date) {
        Date tmpDate = null;
        if (date != null && date.length > 0) {
            tmpDate = date[0];
        } else {
            tmpDate = getCurrentDate();
        }

        Calendar c = Calendar.getInstance();
        c.setTime(tmpDate);
        c.add(Calendar.MONTH, n);

        return getDate2String(c.getTime());
    }

    public static String getLastNMonthDate(int n, String date) {
        if (StringUtils.isNotBlank(date)) {
            return getLastNMonthDate(n, getString2Date(date));
        } else {
            return getLastNMonthDate(n);
        }
    }

    public static String getLastNYearBeginDate(int n) {
        return getLastNYearBeginDate(n, new SimpleDateFormat("yyyy-MM-dd"));
    }

    public static String getLastNYearBeginDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, n);
        c.set(c.get(Calendar.YEAR), 0, 1);
        return format.format(c.getTime());
    }

    public static String getLastNYearEndDate(int n) {
        return getLastNYearEndDate(n, new SimpleDateFormat("yyyy-MM-dd"));
    }

    public static String getLastNYearEndDate(int n, DateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, n);
        c.set(c.get(Calendar.YEAR), 11, 31);
        return format.format(c.getTime());
    }

    public static String getCurrentWeekDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        String weekDayStr = null;
        switch (weekDay) {
            case 1:
                weekDayStr = "星期日";
                break;
            case 2:
                weekDayStr = "星期一";
                break;
            case 3:
                weekDayStr = "星期二";
                break;
            case 4:
                weekDayStr = "星期 三";
                break;
            case 5:
                weekDayStr = "星期四";
                break;
            case 6:
                weekDayStr = "星期五";
                break;
            case 7:
                weekDayStr = "星期六";
                break;
        }

        return weekDayStr;
    }

    public static String getCurrentTimeDesc() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH");
        int h = Integer.parseInt(format.format(now));
        if (h >= 0 && h <= 6) {
            return "凌晨";
        }
        if (h > 6 && h <= 12) {
            return "上午";
        }
        if (h > 12 && h <= 13) {
            return "中午";
        }
        if (h > 13 && h <= 18) {
            return "下午";
        }
        if (h > 18 && h <= 24) {
            return "晚上";
        }
        return null;
    }

    public static int getCurrentHourOfDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static Date getCurrentYearDate() {
        Calendar date = Calendar.getInstance();
        String year = date.get(Calendar.YEAR) + "-01-01";
        return getString2Date(year);
    }

    public static Date getCurrentYearDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = cal.get(Calendar.YEAR) + "-01-01 00:00:00";
        return getString2Date(YYYY_MM_DD_HH_MM_SS, year);
    }

    public static Date getCurrentYearDate(String yearStr) {
        String year = yearStr + "-01-01 00:00:00";
        return getString2Date(YYYY_MM_DD_HH_MM_SS, year);
    }

    public static Date getNextYearDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1); // 把日期往后增加一年.整数往后推,负数往前移动
        return calendar.getTime();
    }

    public static String getAMPM() {
        int hour = getCurrentHourOfDay();
        String amPM = "";
        if (hour <= 12) {
            amPM = "上午";
        } else if (12 < hour && hour <= 18) {
            amPM = "下午";
        } else if (18 < hour && hour < 24) {
            amPM = "晚上";
        }

        return amPM;
    }

    public static Date formatDate(Date date, String... format) {
        String tmpFormat = YYYY_MM_DD;
        if (format != null && format.length > 0) {
            tmpFormat = format[0];
        }

        String dateStr = getDate2String(tmpFormat, date);

        return getString2Date(tmpFormat, dateStr);
    }

    public static long handleDateTimeForZadd(Date date) {
        if (date == null) {
            throw new RuntimeException("zadd中转换日期不能为空！");
        }
        String tmpFormat = YYYY_MM_DD_HH_MM_SS;
        Date realDate = formatDate(date, tmpFormat);
        return realDate.getTime();
    }

    public static long getDifferDays(Date end, Date begin) {
        long quot = 0;
        quot = end.getTime() - begin.getTime();
        quot = quot / 1000 / 60 / 60 / 24;
        return quot;
    }

    public static String getString2String(String str, String formatSrc, String format) {
        return getDate2String(format, getString2Date(formatSrc, str));
    }

    public static long getBetweenDay(String beginDate, Date... endDate) {
        return getBetweenDay(getString2Date(beginDate), endDate);
    }

    public static long getBetweenDay(Date beginDate, Date... endDate) {
        Date tmpEndDate = getCurrentDate(YYYY_MM_DD);
        if (endDate != null && endDate.length > 0) {
            tmpEndDate = formatDate(endDate[0], YYYY_MM_DD);
        }

        beginDate = formatDate(beginDate, YYYY_MM_DD);
        Calendar cal = Calendar.getInstance();
        cal.setTime(beginDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(tmpEndDate);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);

        return betweenDays;
    }

    public static Date dateAdd(Date srcDate, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);

        c.add(Calendar.DAY_OF_YEAR, amount);
        return c.getTime();
    }

    public static Date dateAddHour(Date srcDate, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);

        c.add(Calendar.HOUR, hour);
        return c.getTime();

    }

    public static Date getTodayZeroTime() {
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Map<String, Date> getCurrentDateRangeMap(Date... date) {
        Map<String, Date> dateMap = new HashMap<String, Date>();
        Date tmpDate = DateUtils.getCurrentDate();
        if (date != null && date.length > 0) {
            tmpDate = date[0];
        }

        String operateTime = DateUtils.getDate2String(DateUtils.YYYY_MM_DD, tmpDate);
        Date beginDate = DateUtils.getString2Date(DateUtils.YYYY_MM_DD_HH_MM_SS, operateTime + DateUtils.DATE_BEGIN);
        Date endDate = DateUtils.getString2Date(DateUtils.YYYY_MM_DD_HH_MM_SS, operateTime + DateUtils.DATE_END);

        dateMap.put("beginDate", beginDate);
        dateMap.put("endDate", endDate);

        return dateMap;
    }

    public static Map<String, Date> getYesterdayRangeMap() {
        Map<String, Date> dateMap = new HashMap<String, Date>();
        Date yesterday = DateUtils.getBeforeNDayDate(1L, DateUtils.getCurrentDate());
        String operateTime = DateUtils.getDate2String(DateUtils.YYYY_MM_DD, yesterday);
        Date beginDate = DateUtils.getString2Date(DateUtils.YYYY_MM_DD_HH_MM_SS, operateTime + DateUtils.DATE_BEGIN);
        Date endDate = DateUtils.getString2Date(DateUtils.YYYY_MM_DD_HH_MM_SS, operateTime + DateUtils.DATE_END);

        dateMap.put("beginDate", beginDate);
        dateMap.put("endDate", endDate);

        return dateMap;
    }

    public static Map<String, Date> getNHourBeforeRangeMap(int hours) {
        if (hours < 0) {
            hours = 1;
        }
        Map<String, Date> dateMap = new HashMap<String, Date>();
        Date beginDate = DateUtils.getNSeconBefore(Long.valueOf(hours * 60 * 60)); // n 小时前
        Date endDate = DateUtils.getCurrentDate();

        dateMap.put("beginDate", beginDate);
        dateMap.put("endDate", endDate);

        return dateMap;
    }

    public static String getDateYear(Date... date) {
        String year = "";
        Date tmpDate = getCurrentDate();
        if ((date != null) && (date.length > 0)) {
            tmpDate = date[0];
        }
        Calendar c = Calendar.getInstance();
        c.setTime(tmpDate);

        year = String.valueOf(c.get(Calendar.YEAR));

        return year;
    }

    public static String getYearByDay(Date now) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(now);
    }

    public static boolean checkParam(Object param) {
        if (null == param || "".equals(param)) {
            return true;
        } else {
            return false;
        }
    }

    public static Date addDays(Date date, int days) {
        if (checkParam(date)) {
            return null;
        }
        if (0 == days) {
            return date;
        }
        Locale locale = Locale.getDefault();
        Calendar calendar = new GregorianCalendar(locale);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static Long getDistanceReduceNow(Date date) {
        if (date == null) {
            return null;
        }
        return date.getTime() - getCurrentTimeMillis();
    }

    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
                ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()),
                ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

    public static Date getCurrentHourTime() {
        return getHourTime(new Date(), 0, "=");
    }

    public static Date getLastHourTime(Date date, int n) {
        return getHourTime(date, n, "-");
    }

    public static Date getNextHourTime(Date date, int n) {
        return getHourTime(date, n, "+");
    }

    public static Date getHourTime(Date date, int n, String direction) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        switch (direction) {
            case "+":
                ca.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY) + n);
                break;
            case "-":
                ca.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY) - n);
                break;
            case "=":
                ca.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY));
                break;
            default:
                ca.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY));
        }

        date = ca.getTime();
        return date;
    }

}
