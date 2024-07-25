package me.notenoughskill.auroralabs.tasks;

import com.google.common.collect.Lists;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.notenoughskill.auroralabs.AuroraLabsPlugin;
import me.notenoughskill.auroralabs.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class DateWatcherTask implements Runnable {
    boolean ranThisSession;

    public static List<Long> events;

    private Calendar mondayEvent;

    private Calendar tuesdayEvent;

    private Calendar thursdayEvent;

    private Calendar wednsdayEvent;

    private Calendar fridayEvent;

    private Calendar saturdayEvent;

    private Calendar sundayEvent;

    public DateWatcherTask() {
        this.ranThisSession = false;
        events = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        List<LocalDate> days = Lists.newArrayList();
        for (int i = 1; i <= 7; i++) {
            LocalDate localDate1 = localDate.with(TemporalAdjusters.next(DayOfWeek.of(i)));
            days.add(localDate1);
            events.add(Long.valueOf(localDate1.atStartOfDay(ZoneId.of("America/New_York")).toEpochSecond()));
            events.add(Long.valueOf(localDate1.atStartOfDay(ZoneId.of("America/New_York")).toEpochSecond() + TimeUnit.HOURS.toSeconds(12L)));
        }
        LocalDate day = localDate.with(localDate.getDayOfWeek());
        if (!days.contains(day)) {
            events.add(Long.valueOf(day.atStartOfDay(ZoneId.of("America/New_York")).toEpochSecond()));
            events.add(Long.valueOf(day.atStartOfDay(ZoneId.of("America/New_York")).toEpochSecond() + TimeUnit.HOURS.toSeconds(12L)));
        }
    }

    public void run() {
        if (this.ranThisSession)
            return;
        //if (StartingTask.countdown != -1)
            //return;
        Calendar c = Calendar.getInstance();
        for (Long event : events) {
            LocalDate date = Instant.ofEpochSecond(event.longValue()).atZone(ZoneId.of("America/New_York")).toLocalDate();
            if (date.getDayOfWeek() == LocalDate.now().getDayOfWeek() && event.longValue() < System.currentTimeMillis() / 1000L) {
                //Bukkit.getScheduler().runTaskTimer((Plugin) AuroraLabsPlugin.getInstance(), new StartingTask(901), 20L, 20L);
                this.ranThisSession = true;
            }
        }
    }

    public static long getMilliUntilNextLabs() {
        long nearestEvent = System.currentTimeMillis();
        for (Long event : events) {
            long msUntilEvent = event.longValue() - System.currentTimeMillis() / 1000L;
            if (msUntilEvent < nearestEvent && msUntilEvent > 0L)
                nearestEvent = msUntilEvent;
        }
        return nearestEvent;
    }

    public static String getTimeUntilNextLabs() {
        long l = getMilliUntilNextLabs();
        return TimeUtils.getSecondsAsString(l);
    }

    private static Calendar nextDayOfWeek(int dow) {
        Calendar date = Calendar.getInstance();
        int diff = dow - date.get(7);
        if (diff <= 0 && diff != 0)
            diff += 7;
        date.add(5, diff);
        return date;
    }
}
