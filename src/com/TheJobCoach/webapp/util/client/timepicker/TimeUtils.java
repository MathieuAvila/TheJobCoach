package com.TheJobCoach.webapp.util.client.timepicker;

import java.util.Date;

public class TimeUtils
{
    public static final long DAY_IN_MS = 24L*60L*60L*1000L;
    
    public static final long trimTimeToMidnight(long time) {
        // first trim to midnight
        return time - time % DAY_IN_MS;
    }

    public static final Date utc2date(Long time) {

        // don't accept negative values
        if (time == null || time < 0) return null;
       
        // add the timezone offset
        time += timezoneOffsetMillis(new Date(time));

        return new Date(time);
    }

    public static final Long date2utc(Date date) {

        // use null for a null date
        if (date == null) return null;
       
        long time = date.getTime();
       
        // remove the timezone offset        
        time -= timezoneOffsetMillis(date);
       
        return time;
    }

    public static final Long getValueForToday() {
        return trimTimeToMidnight(date2utc(new Date()));
    }

    @SuppressWarnings("deprecation")
    public static final long timezoneOffsetMillis(Date date) {
        return date.getTimezoneOffset()*60*1000;        
    }


}
