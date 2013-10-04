package com.experiment

import groovyx.javafx.beans.FXBindable

@FXBindable
class Time {
    Integer hours
    Integer minutes
    Integer seconds
    Double hourAngle
    Double minuteAngle
    Double secondAngle

    public Time() {
        // bind the angle properties to the clock time
        hourAngleProperty.bind((hours() * 30.0) + (minutes() * 0.5))
        minuteAngleProperty.bind(minutes() * 6.0)
        secondAngleProperty.bind(seconds() * 6.0)
        // Set the initial clock time
        def calendar = Calendar.instance
        hours = calendar.get(Calendar.HOUR)
        minutes = calendar.get(Calendar.MINUTE)
        seconds = calendar.get(Calendar.SECOND)
    }

    public void addOneSecond() {
        seconds = (seconds + 1) % 60
        if (seconds == 0) {
            minutes = (minutes + 1) % 60
            if (minutes == 0)
                hours = (hours + 1) % 12
        }
    }
}
