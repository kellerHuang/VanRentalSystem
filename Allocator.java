import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;

public class Allocator {
    public Allocator() {
    }

    public ArrayList<Booking> newBooking(int bookingID, LocalDateTime startTime, LocalDateTime endTime, int autoRequired, int manualRequired, ArrayList<Booking> existingBookings, ArrayList<Depot> depots) {
        ArrayList<Campervan> assignedCampervans = new ArrayList();
        Booking newBooking = new Booking(bookingID, startTime, endTime, assignedCampervans);
        Boolean bookingRejected = false;

        ArrayList campervanCheck;
        int x;
        for(int i = 0; i < depots.size(); ++i) {
            campervanCheck = ((Depot)depots.get(i)).getCampervans();

            for(x = 0; x < campervanCheck.size(); ++x) {
                ArrayList<LocalDateTime> timesBooked = ((Campervan)campervanCheck.get(x)).checkTime();
                Boolean isRejected = false;

                for(int c = 0; c < timesBooked.size(); c += 2) {
                    if (startTime.isAfter((ChronoLocalDateTime)timesBooked.get(c)) && startTime.isBefore((ChronoLocalDateTime)timesBooked.get(c + 1))) {
                        isRejected = true;
                        break;
                    }

                    if (endTime.isAfter((ChronoLocalDateTime)timesBooked.get(c)) && endTime.isBefore((ChronoLocalDateTime)timesBooked.get(c + 1))) {
                        isRejected = true;
                        break;
                    }

                    if (startTime.isBefore((ChronoLocalDateTime)timesBooked.get(c)) && endTime.isAfter((ChronoLocalDateTime)timesBooked.get(c + 1))) {
                        isRejected = true;
                        break;
                    }

                    if (startTime.isEqual((ChronoLocalDateTime)timesBooked.get(c)) || startTime.isEqual((ChronoLocalDateTime)timesBooked.get(c + 1)) || endTime.isEqual((ChronoLocalDateTime)timesBooked.get(c)) || endTime.isEqual((ChronoLocalDateTime)timesBooked.get(c + 1))) {
                        isRejected = true;
                        break;
                    }
                }

                if (!isRejected.booleanValue()) {
                    if (autoRequired > 0 && !((Campervan)campervanCheck.get(x)).getManual().booleanValue()) {
                        --autoRequired;
                        assignedCampervans.add(campervanCheck.get(x));
                    } else if (manualRequired > 0 && ((Campervan)campervanCheck.get(x)).getManual().booleanValue()) {
                        --manualRequired;
                        assignedCampervans.add(campervanCheck.get(x));
                    }
                }
            }
        }

        if (autoRequired <= 0 && manualRequired <= 0) {
            existingBookings.add(newBooking);
        } else {
            bookingRejected = true;
        }

        if (!bookingRejected.booleanValue()) {
            Booking toUpdate = (Booking)existingBookings.get(existingBookings.size() - 1);
            if (!toUpdate.checkComplete().booleanValue()) {
                campervanCheck = toUpdate.giveCampervans();

                for(x = 0; x < campervanCheck.size(); ++x) {
                    Campervan curr = (Campervan)campervanCheck.get(x);
                    curr.newTime(toUpdate.getStartTime(), toUpdate.getEndTime());
                }

                toUpdate.makeComplete();
            }
        }

        return existingBookings;
    }

    public ArrayList<Booking> changeBooking(int bookingID, LocalDateTime startTime, LocalDateTime endTime, int autoRequired, int manualRequired, ArrayList<Booking> existingBookings, ArrayList<Depot> depots) {
        Booking toChange = null;

        int i;
        for(i = 0; i < existingBookings.size(); ++i) {
            if (((Booking)existingBookings.get(i)).getBookingID() == bookingID) {
                toChange = (Booking)existingBookings.get(i);
                break;
            }
        }

        if (toChange == null) {
            return existingBookings;
        } else {
            Boolean success = false;
            this.deleteBooking(existingBookings, depots, bookingID);
            this.newBooking(bookingID, startTime, endTime, autoRequired, manualRequired, existingBookings, depots);

            for(int x = 0; x < existingBookings.size(); ++x) {
                if (((Booking)existingBookings.get(x)).getBookingID() == bookingID) {
                    success = true;
                    break;
                }
            }

            if (!success.booleanValue()) {
                existingBookings.add(i, toChange);
            }

            return existingBookings;
        }
    }

    public ArrayList<Booking> deleteBooking(ArrayList<Booking> existingBookings, ArrayList<Depot> depots, int deleteNo) {
        for(int x = 0; x < existingBookings.size(); ++x) {
            if (((Booking)existingBookings.get(x)).getBookingID() == deleteNo) {
                Booking toDelete = (Booking)existingBookings.get(x);
                ArrayList<Campervan> campervans = toDelete.giveCampervans();

                for(int i = 0; i < campervans.size(); ++i) {
                    String depotName = ((Campervan)campervans.get(i)).getDepot();

                    for(int c = 0; c < depots.size(); ++c) {
                        if (depotName.equals(((Depot)depots.get(c)).getName())) {
                            ArrayList<Campervan> change = ((Depot)depots.get(c)).getCampervans();

                            for(int b = 0; b < change.size(); ++b) {
                                if (((Campervan)campervans.get(i)).getName().equals(((Campervan)change.get(b)).getName())) {
                                    ((Campervan)change.get(b)).deleteTime(toDelete.getStartTime(), toDelete.getEndTime());
                                }
                            }
                        }
                    }
                }

                existingBookings.remove(x);
                break;
            }
        }

        return existingBookings;
    }
}
