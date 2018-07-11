import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class Campervan {
    private String name;
    private String depot;
    private Boolean manual;
    private ArrayList<LocalDateTime> timeBooked = new ArrayList();

    public Campervan(String name, String depot, Boolean manual) {
        this.name = name;
        this.depot = depot;
        this.manual = manual;
    }

    public Boolean getManual() {
        return this.manual;
    }

    public String getDepot() {
        return this.depot;
    }

    public String getName() {
        return this.name;
    }

    public void newTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.timeBooked.add(startTime);
        this.timeBooked.add(endTime);
    }

    public ArrayList<LocalDateTime> checkTime() {
        return this.timeBooked;
    }

    public void deleteTime(LocalDateTime startTime, LocalDateTime endTime) {
        this.timeBooked.remove(startTime);
        this.timeBooked.remove(endTime);
    }

    public void printCampervan() {
        LocalDateTime currTime = null;
        LocalDateTime timeFloor = null;

        for(int i = 0; i < this.timeBooked.size(); ++i) {
            if (i % 2 == 0) {
                System.out.printf(this.depot + " " + this.name + " ");
            }

            int x;
            int y;
            if (currTime == null) {
                currTime = (LocalDateTime)this.timeBooked.get(i);

                for(x = 0; x < this.timeBooked.size(); ++x) {
                    if (currTime.isAfter((ChronoLocalDateTime)this.timeBooked.get(x))) {
                        currTime = (LocalDateTime)this.timeBooked.get(x);
                    }
                }
            } else {
                timeFloor = currTime;

                for(x = 0; x < this.timeBooked.size(); ++x) {
                    if (timeFloor == currTime) {
                        for(y = 0; y < this.timeBooked.size(); ++y) {
                            if (((LocalDateTime)this.timeBooked.get(y)).isAfter(timeFloor)) {
                                currTime = (LocalDateTime)this.timeBooked.get(y);
                                break;
                            }
                        }
                    }

                    if (currTime.isAfter((ChronoLocalDateTime)this.timeBooked.get(x)) && ((LocalDateTime)this.timeBooked.get(x)).isAfter(timeFloor) && !((LocalDateTime)this.timeBooked.get(x)).isEqual(timeFloor)) {
                        currTime = (LocalDateTime)this.timeBooked.get(x);
                    }
                }
            }

            x = currTime.getHour();
            y = currTime.getDayOfMonth();
            String hourLine;
            if (x < 10) {
                hourLine = "0" + x;
            } else {
                hourLine = "" + x;
            }

            String dayLine;
            if (y < 10) {
                dayLine = "0" + y;
            } else {
                dayLine = "" + y;
            }

            System.out.printf(hourLine + ":00 " + currTime.getMonth().getDisplayName(TextStyle.SHORT, new Locale("en")) + " " + dayLine);
            if (i % 2 == 0) {
                System.out.printf(" ");
            }

            if (i % 2 == 1 && i != this.timeBooked.size() - 1) {
                System.out.println();
            }
        }

        if (!this.timeBooked.isEmpty()) {
            System.out.println();
        }

    }
}
