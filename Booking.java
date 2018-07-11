import java.time.LocalDateTime;
import java.util.ArrayList;

public class Booking {
    private int bookingID;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ArrayList<Campervan> assignedCampervans;
    private boolean complete;
    private boolean printed;

    public Booking(int bookingID, LocalDateTime startDate, LocalDateTime endDate, ArrayList<Campervan> assignedCampervans) {
        this.bookingID = bookingID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assignedCampervans = assignedCampervans;
        this.complete = false;
        this.printed = false;
    }

    public ArrayList<Campervan> giveCampervans() {
        return this.assignedCampervans;
    }

    public int getBookingID() {
        return this.bookingID;
    }

    public LocalDateTime getStartTime() {
        return this.startDate;
    }

    public LocalDateTime getEndTime() {
        return this.endDate;
    }

    public void printBooking() {
        String currDepot = "";

        for(int i = 0; i < this.assignedCampervans.size(); ++i) {
            if (!((Campervan)this.assignedCampervans.get(i)).getDepot().equals(currDepot)) {
                if (currDepot != "") {
                    System.out.printf("; ");
                }

                System.out.printf(((Campervan)this.assignedCampervans.get(i)).getDepot() + " ");
                currDepot = ((Campervan)this.assignedCampervans.get(i)).getDepot();
            }

            System.out.printf(((Campervan)this.assignedCampervans.get(i)).getName());
            if (i + 1 != this.assignedCampervans.size() && ((Campervan)this.assignedCampervans.get(i)).getDepot().equals(((Campervan)this.assignedCampervans.get(i + 1)).getDepot())) {
                System.out.printf(", ");
            }
        }

        System.out.println();
    }

    public Boolean checkComplete() {
        return this.complete;
    }

    public void makeComplete() {
        this.complete = true;
    }

    public Boolean checkPrinted() {
        return this.printed;
    }

    public void makePrinted() {
        this.printed = true;
    }
}
