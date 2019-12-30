package utils;

import java.util.Date;

public class DateMath {

    public static int Diff2DatesMin(Date d1, Date d2) {
        return (int)((d1.getTime() - d2.getTime())/1000/60);
    }
}