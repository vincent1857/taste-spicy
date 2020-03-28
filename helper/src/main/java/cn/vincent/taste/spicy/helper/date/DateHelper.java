/*
 * Copyright (c) 2015 by vincent.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.vincent.taste.spicy.helper.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author vincent
 * @version 1.0 2017/8/20 03:22
 */
public class DateHelper {

    public static final String FULL_PATTERN = "yyyyMMddHHmmss";
    public static final String FULL_PATTERN_SSS = "yyyyMMddHHmmssSSS";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS_SLASH = "yyyy/MM/dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SLASH = "yyyy/MM/dd HH:mm";
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_SLASH = "yyyy/MM/dd";
    public static final String DATE_FORMAT_YYYY_MM_DD_NO_DASH = "yyyyMMdd";
    public static final String DATE_FORMAT_YYYY_MM_DD_CHINESE = "yyyy年MM月dd日";
    public final static String DATE_FORMAT_YYYY_MM = "yyyy-MM";
    public final static String DATE_FORMAT_YYYY_MM_SLASH = "yyyy/MM";
    public final static String DATE_FORMAT_YYYY_MM_NO_DASH = "yyyyMM";
    public final static String DATE_FORMAT_YYYY_MM_CHINESE = "yyyy年MM月";
    public final static String DATE_FORMAT_YYYY = "yyyy";
    public final static String DATE_FORMAT_MM_DD = "MMdd";
    public final static String DATE_FORMAT_MM = "MM";
    public final static String DATE_FORMAT_DD = "dd";

    public static final long SECOND = 1000L;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;

    public static final int MONTH_OF_YEAR = 12;

    /**
     * 获取Calendar对象
     *
     * @param date        日期
     * @param hourOfDay   时
     * @param minute      分
     * @param second      秒
     * @param millisecond 毫秒
     * @return 时间
     */
    public static Calendar getCalendar(Date date, int hourOfDay, int minute, int second, int millisecond) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millisecond);
        return cal;
    }

    /**
     * 获取指定日期的开始时间，如2016-01-01 00:00:00
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getStartTime(Date date) {
        return getCalendar(date, 0, 0, 0, 0).getTime();
    }

    /**
     * 获取指定日期下一天的开始时间，如2016-01-02 00:00:00
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getNextStartTime(Date date) {
        return getCalendar(date, 24, 0, 0, 0).getTime();
    }

    /**
     * 获取指定日期前一天的开始时间，如2016-01-01 00:00:00
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getPrevStartTime(Date date) {
        Calendar calendar = getCalendar(date, 0, 0, 0, 0);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 获取所属月份的开始时间
     *
     * @param year  年份
     * @param month 月份
     * @return 时间
     */
    public static Date getMonthStartTime(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期所属月份的开始时间
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getMonthStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        return getCalendar(calendar.getTime(), 0, 0, 0, 0).getTime();
    }

    /**
     * 获取今天的开始时间，如2016-01-01 00:00:00
     *
     * @return 时间
     */
    public static Date getTodayStartTime() {
        return getStartTime(new Date());
    }

    /**
     * 获取明天的开始时间，如2016-01-02 00:00:00
     *
     * @return 时间
     */
    public static Date getTomorrowStartTime() {
        return getNextStartTime(new Date());
    }

    /**
     * 获取昨天的开始时间，如2016-01-02 00:00:00
     *
     * @return 时间
     */
    public static Date getYesterdayStartTime() {
        return getPrevStartTime(new Date());
    }

    /**
     * 获取指定日期的结束时间，如2016-01-01 23:59:59
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getEndTime(Date date) {
        return getCalendar(date, 23, 59, 59, 999).getTime();
    }

    /**
     * 获取指定日期下一天的结束时间，如2016-01-02 23:59:59
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getNextEndTime(Date date) {
        Date nextStartTime = getNextStartTime(date);
        return getEndTime(nextStartTime);
    }

    /**
     * 获取指定日期前一天的结束时间，如2016-01-01 23:59:59
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getPrevEndTime(Date date) {
        Date prevStartTime = getPrevStartTime(date);
        return getEndTime(prevStartTime);
    }

    /**
     * 获取所属月份的结束时间
     *
     * @param year  年份
     * @param month 月份
     * @return 时间
     */
    public static Date getMonthEndTime(int year, int month) {
        int nextMonth = month + 1;
        if (nextMonth > MONTH_OF_YEAR) {
            year = year + 1;
            nextMonth = nextMonth - MONTH_OF_YEAR;
        }
        Date nextMonthDate = getMonthStartTime(year, nextMonth);
        long monthEndTime = nextMonthDate.getTime() - 1;

        return new Date(monthEndTime);
    }

    /**
     * 获取指定日期所属月份的结束时间
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date getMonthEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int value = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, value);
        return getCalendar(calendar.getTime(), 23, 59, 59, 999).getTime();
    }

    /**
     * 获取今天的结束时间，如2016-01-01 23:59:59
     *
     * @return 时间
     */
    public static Date getTodayEndTime() {
        return getEndTime(new Date());
    }

    /**
     * 获取明天的结束时间，如2016-01-02 23:59:59
     *
     * @return 时间
     */
    public static Date getTomorrowEndTime() {
        return getNextEndTime(new Date());
    }

    /**
     * 获取昨天的结束时间，如2016-01-02 23:59:59
     *
     * @return 时间
     */
    public static Date getYesterdayEndTime() {
        return getPrevEndTime(new Date());
    }

    /**
     * 新增
     *
     * @param date          基准时间
     * @param calendarField 新增类型
     * @param amount        数值
     * @return 时间
     */
    public static Date add(Date date, int calendarField, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, amount);
        return cal.getTime();
    }

    /**
     * 新增多少年的时间
     *
     * @param date  基准时间
     * @param years 年数
     * @return 时间
     */
    public static Date addYears(Date date, int years) {
        return add(date, Calendar.YEAR, years);
    }

    /**
     * 新增多少月的时间
     *
     * @param date   基准时间
     * @param months 月数
     * @return 时间
     */
    public static Date addMonths(Date date, int months) {
        return add(date, Calendar.MONTH, months);
    }

    /**
     * 新增多少星期的时间
     *
     * @param date  基准时间
     * @param weeks 星期数
     * @return 时间
     */
    public static Date addWeeks(Date date, int weeks) {
        return add(date, Calendar.WEEK_OF_YEAR, weeks);
    }

    /**
     * 新增多少天的时间
     *
     * @param date 基准时间
     * @param days 月数
     * @return 时间
     */
    public static Date addDays(Date date, int days) {
        return add(date, Calendar.DAY_OF_MONTH, days);
    }

    /**
     * 新增多少小时的时间
     *
     * @param date  基准时间
     * @param hours 小时数
     * @return 时间
     */
    public static Date addHours(Date date, int hours) {
        return add(date, Calendar.HOUR_OF_DAY, hours);
    }

    /**
     * 新增多少分钟的时间
     *
     * @param date    基准时间
     * @param minutes 分钟数
     * @return 时间
     */
    public static Date addMinutes(Date date, int minutes) {
        return add(date, Calendar.MINUTE, minutes);
    }

    /**
     * 新增多少秒的时间
     *
     * @param date    基准时间
     * @param seconds 秒数
     * @return 时间
     */
    public static Date addSeconds(Date date, int seconds) {
        return add(date, Calendar.SECOND, seconds);
    }

    /**
     * 新增多少毫秒的时间
     *
     * @param date        基准时间
     * @param millseconds 毫秒数
     * @return 时间
     */
    public static Date addMillseconds(Date date, int millseconds) {
        return add(date, Calendar.MILLISECOND, millseconds);
    }

    /**
     * 舍去时
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date truncateDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 舍去分
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date truncateHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 舍去秒
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date truncateMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 舍去毫秒
     *
     * @param date 基准时间
     * @return 时间
     */
    public static Date truncateSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定时间的这一周第一天开始时间
     *
     * @param date 时间
     * @return 时间
     */
    public static Date getFirstOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int d;
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // 所在周开始日期
        return cal.getTime();
    }

    /**
     * 获取指定时间的这一周最后一天开始时间
     *
     * @param date 时间
     * @return 时间
     */
    public static Date getLastOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        int d = cal.get(Calendar.DAY_OF_WEEK);

        // 默认是以周日为第一天的
        if (d == 1) {
            d = 8;
        }

        // 所在周结束日期
        cal.add(Calendar.DATE, 7 - d + 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取上周的结束时间
     *
     * @return 时间
     */
    public static Date getLastWeekSunday() {
        Calendar date = Calendar.getInstance(Locale.CHINA);
        // 将每周第一天设为星期一，默认是星期天
        date.setFirstDayOfWeek(Calendar.MONDAY);
        // 周数减一，即上周
        date.add(Calendar.WEEK_OF_MONTH, -1);
        // 日子设为星期天
        date.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 59);
        date.set(Calendar.SECOND, 59);
        date.set(Calendar.MILLISECOND, 999);
        return date.getTime();
    }

    /**
     * 获取上周的开始时间
     *
     * @return 时间
     */
    public static Date getLastWeekMonday() {
        Calendar date = Calendar.getInstance(Locale.CHINA);
        // 将每周第一天设为星期一，默认是星期天
        date.setFirstDayOfWeek(Calendar.MONDAY);
        // 周数减一，即上周
        date.add(Calendar.WEEK_OF_MONTH, -1);
        // 日子设为星期一
        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }

    /**
     * 解析时间字符串
     *
     * @param date   时间字符串
     * @param format 格式化
     * @return 时间
     * @throws ParseException 异常
     */
    public static Date parse(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }

    /**
     * 解析时间字符串
     *
     * @param date   时间字符串
     * @param format 格式化
     * @return 时间
     */
    public static Date parseSilently(String date, String format) {
        try {
            return parse(date, format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析时间字符串, 默认格式yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间字符串
     * @return 时间
     */
    public static Date parse(String date) throws ParseException {
        return parse(date, DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 解析时间字符串, 默认格式yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间字符串
     * @return 时间
     */
    public static Date parseSilently(String date) {
        return parseSilently(date, DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定时间当天剩余的毫秒数
     *
     * @param date 基准时间
     * @return 毫秒数
     */
    public static long getRemainingMillsecondsOfDay(Date date) {
        return getEndTime(date).getTime() - date.getTime();
    }

    /**
     * 获取当天剩余时间，以毫秒为单位
     *
     * @return 毫秒数
     */
    public static long getTodayRemainingMillsecondsOfDay() {
        return getRemainingMillsecondsOfDay(new Date());
    }

    /**
     * 返回2个日期之前相差的天数
     *
     * @param start 开始时间(字符串型2017-01-01)
     * @param end   结束时间(字符串型2017-01-03)
     * @return 天数
     */
    public static long getDateInterval(String start, String end) throws ParseException {
        if (start == null || start.trim().length() == 0 || end == null || end.trim().length() == 0) {
            return -1L;
        }
        Date startDate = parse(start, DATE_FORMAT_YYYY_MM_DD);
        Date endDate = parse(end, DATE_FORMAT_YYYY_MM_DD);
        return (long) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24.0) + 0.5);
    }

    /**
     * 返回2个日期之前相差的天数
     *
     * @param start 开始时间(时间型)
     * @param end   结束时间(时间型)
     * @return 天数
     */
    public static long getDateInterval(Date start, Date end) {
        if (null == start || null == end) {
            return -1L;
        }
        Date startDate = getCalendar(start, 0, 0, 0, 0).getTime();
        Date endDate = getCalendar(end, 0, 0, 0, 0).getTime();
        return (long) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24.0) + 0.5);
    }

    /**
     * 计算两个时间相差的秒数
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 秒数
     */
    public static int getSecondInterval(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / 1000);
    }

    /**
     * 时间转换字符串
     *
     * @param date   时间
     * @param format 格式化
     * @return 时间字符串
     */
    public static String format(Date date, String format) {
        if (format == null || format.trim().length() == 0) {
            format = DATE_FORMAT_YYYY_MM_DD_HH_MM_SS;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date == null) {
            date = new Date();
        }
        return sdf.format(date);
    }

    /**
     * 时间转换字符串
     *
     * @param date 时间
     * @return 时间字符串
     */
    public static String format(Date date) {
        return format(date, null);
    }

    /**
     * 比较是否同一天
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return true/false
     */
    public static boolean isSameDay(long time1, long time2) {
        Calendar todayC = Calendar.getInstance(Locale.CHINA);
        todayC.setTimeInMillis(time1);
        int todayYear = todayC.get(Calendar.YEAR);
        int todayMonth = todayC.get(Calendar.MONTH) + 1;
        int todayDay = todayC.get(Calendar.DAY_OF_MONTH);

        Calendar compareTime = Calendar.getInstance();
        compareTime.setTimeInMillis(time2);
        int year = compareTime.get(Calendar.YEAR);
        int month = compareTime.get(Calendar.MONTH) + 1;
        int day = compareTime.get(Calendar.DAY_OF_MONTH);

        return year == todayYear && month == todayMonth && day == todayDay;
    }
}
