import java.util.ArrayList;

public class Depot {
    private String name;
    private ArrayList<Campervan> campervans;

    public Depot(String name) {
        this.name = name;
        this.campervans = new ArrayList();
    }

    public void printDepot() {
        for(int i = 0; i < this.campervans.size(); ++i) {
            ((Campervan)this.campervans.get(i)).printCampervan();
        }

    }

    public String getName() {
        return this.name;
    }

    public void addVehicle(Campervan car) {
        this.campervans.add(car);
    }

    public Boolean checkName(String name) {
        return this.name.equals(name) ? true : false;
    }

    public ArrayList<Campervan> getCampervans() {
        return this.campervans;
    }
}
