import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadToCommand {
    public ReadToCommand() {
    }

    public ArrayList<Campervan> findCampervans(ArrayList<String> input) {
        ArrayList<Campervan> campervanList = new ArrayList();
        Pattern splitLine = Pattern.compile("Location (\\w+) (\\w+) (Automatic|Manual)");

        for(int i = 0; i < input.size(); ++i) {
            String line = (String)input.get(i);
            if (line.split("#").length != 0) {
                Matcher data = splitLine.matcher(line.split("#")[0]);
                if (data.find()) {
                    Boolean convert;
                    if (data.group(3).equals("Manual")) {
                        convert = true;
                    } else {
                        convert = false;
                    }

                    Campervan newCar = new Campervan(data.group(2), data.group(1), convert);
                    campervanList.add(newCar);
                }
            }
        }

        return campervanList;
    }

    public ArrayList<Depot> compileDepots(ArrayList<Campervan> campervans) {
        ArrayList<Depot> depots = new ArrayList();

        for(int i = 0; i < campervans.size(); ++i) {
            Boolean found = false;

            for(int x = 0; x < depots.size(); ++x) {
                if (((Depot)depots.get(x)).checkName(((Campervan)campervans.get(i)).getDepot()).booleanValue()) {
                    ((Depot)depots.get(x)).addVehicle((Campervan)campervans.get(i));
                    found = true;
                }
            }

            if (!found.booleanValue()) {
                Depot newDepot = new Depot(((Campervan)campervans.get(i)).getDepot());
                newDepot.addVehicle((Campervan)campervans.get(i));
                depots.add(newDepot);
            }
        }

        return depots;
    }

    public ArrayList<Booking> findCommands(ArrayList<String> input, ArrayList<Campervan> avaiableCars, ArrayList<Depot> depots) {
        ArrayList<Booking> finalBookings = new ArrayList();
        Pattern regexRequest = Pattern.compile("(Request|Change) (\\d+) (\\d{2}) (\\w{3}) (\\d{2}) (\\d{2}) (\\w{3}) (\\d{2}) (\\d+) (Automatic|Manual)\\s?(\\d?)\\s?");
        Pattern regexCancel = Pattern.compile("Cancel (\\d+)");
        Pattern regexPrint = Pattern.compile("Print (\\w+)");
        int requestType = false;

        for(int i = 0; i < input.size(); ++i) {
            int requestType = 0;
            String line = (String)input.get(i);
            if (line.split("#").length != 0) {
                String splitLine = line.split("#")[0];
                Allocator bookingManager = new Allocator();
                Matcher requests = regexRequest.matcher(splitLine);
                Matcher cancel = regexCancel.matcher(splitLine);
                Matcher print = regexPrint.matcher(splitLine);
                if (requests.find()) {
                    requestType = 1;
                }

                if (cancel.find()) {
                    requestType = 2;
                }

                if (print.find()) {
                    requestType = 3;
                }

                if (requestType != 0) {
                    if (requestType == 1) {
                        String dates = "";
                        dates = requests.group(3) + requests.group(4) + requests.group(5) + "2017";
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("HHMMMdduuuu");
                        LocalDateTime startDate = LocalDateTime.parse(dates, format);
                        dates = "";
                        dates = requests.group(6) + requests.group(7) + requests.group(8) + "2017";
                        LocalDateTime endDate = LocalDateTime.parse(dates, format);
                        int autoRequired = 0;
                        int manualRequired = 0;
                        if (requests.group(10).equals("Automatic")) {
                            autoRequired = Integer.parseInt(requests.group(9));
                            if (requests.group(11).matches("\\d+")) {
                                manualRequired = Integer.parseInt(requests.group(11));
                            }
                        }

                        if (requests.group(10).equals("Manual")) {
                            manualRequired = Integer.parseInt(requests.group(9));
                            if (requests.group(11).matches("\\d+")) {
                                autoRequired = Integer.parseInt(requests.group(11));
                            }
                        }

                        if (requests.group(1).equals("Change")) {
                            System.out.printf("Change ");
                            finalBookings = bookingManager.changeBooking(Integer.parseInt(requests.group(2)), startDate, endDate, autoRequired, manualRequired, finalBookings, depots);
                            if (finalBookings.size() != 0) {
                                if (!((Booking)finalBookings.get(finalBookings.size() - 1)).checkPrinted().booleanValue()) {
                                    System.out.printf(Integer.parseInt(requests.group(2)) + " ");
                                    ((Booking)finalBookings.get(finalBookings.size() - 1)).printBooking();
                                    ((Booking)finalBookings.get(finalBookings.size() - 1)).makePrinted();
                                } else {
                                    System.out.println("rejected");
                                }
                            } else {
                                System.out.println("rejected");
                            }
                        } else if (requests.group(1).equals("Request")) {
                            finalBookings = bookingManager.newBooking(Integer.parseInt(requests.group(2)), startDate, endDate, autoRequired, manualRequired, finalBookings, depots);
                            if (finalBookings.size() != 0) {
                                if (!((Booking)finalBookings.get(finalBookings.size() - 1)).checkPrinted().booleanValue()) {
                                    System.out.printf("Booking " + Integer.parseInt(requests.group(2)) + " ");
                                    ((Booking)finalBookings.get(finalBookings.size() - 1)).printBooking();
                                    ((Booking)finalBookings.get(finalBookings.size() - 1)).makePrinted();
                                } else {
                                    System.out.println("Booking rejected");
                                }
                            } else {
                                System.out.println("Booking rejected");
                            }
                        }
                    } else {
                        int x;
                        if (requestType == 2) {
                            System.out.printf("Cancel ");
                            x = finalBookings.size();
                            bookingManager.deleteBooking(finalBookings, depots, Integer.parseInt(cancel.group(1)));
                            if (finalBookings.size() == x) {
                                System.out.println("rejected");
                            } else {
                                System.out.println(Integer.parseInt(cancel.group(1)));
                            }
                        } else if (requestType == 3) {
                            for(x = 0; x < depots.size(); ++x) {
                                if (((Depot)depots.get(x)).checkName(print.group(1)).booleanValue()) {
                                    ((Depot)depots.get(x)).printDepot();
                                }
                            }
                        } else {
                            System.out.println(":(");
                        }
                    }
                }
            }
        }

        return finalBookings;
    }
}
