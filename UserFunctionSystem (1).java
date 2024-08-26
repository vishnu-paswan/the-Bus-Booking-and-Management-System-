import java.io.*;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class UserFunctionSystem {
    private static int noOfSeat = 30;
    private static String[] bus;
    private static String filePath = "BusDetails.csv";
    private static String ticketDetailsFile = "TicketDetails.csv";
   
    private static Scanner scanner = new Scanner(System.in);

    public void runFile() {
        String busId = "9410";

        UserFunctionSystem user = new UserFunctionSystem();
        try (FileReader file = new FileReader(filePath);
             BufferedReader br = new BufferedReader(file)) {

            user.realTimeSeatView(br, busId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void realTimeSeatView(BufferedReader br, String busId) {
        String line;
        List<String[]> allRows = new ArrayList<>();
        String[] busDetails = new String[8];
        try {
            while ((line = br.readLine()) != null) {
                bus = line.split(",");
                allRows.add(bus);

                if (bus.length > 2 && bus[4].equals(busId)) {
                    int[] busCapacity = new int[noOfSeat];

                    for (int i = 0; i < 8; i++) {
                        busDetails[i] = bus[i];
                    }
                    for (int i = 0; i < busCapacity.length; i++) {
                        busCapacity[i] = Integer.parseInt(bus[i + 8]);
                    }

                    int count = 1;
                    System.out.println("___________________________________________________");
                    System.out.println("Welcome to Singh's Travel. This is bus " + busId);
                    System.out.println("Route : " + busDetails[5] + "[" + busDetails[0] + "] to " + busDetails[6] + "[" + busDetails[2] + "]");
                    System.out.println("Departure Time  : " + busDetails[1] + " | Destination Time : " + busDetails[3]);

                    System.out.println(" |-------------------------------------------------|");
                    System.out.print(" | ");

                    for (int i = 0; i < busCapacity.length; i++) {

                        if (i < 2) {
                            if (busCapacity[i] == 0) {
                                if (i == 0) {
                                    System.out.print(i + 1 + "  Window");
                                    count++;
                                } else {
                                    System.out.println(i + 1 + "  Seat ");
                                    count++;
                                }

                            } else {
                                System.out.print("   Booked");
                                count++;
                            }
                            if(count > 2) {
                            	
                            	System.out.println("              <| Driver |>");
                            	count = 1;
                            }
                            System.out.print(" | ");

                        } else {
                            if (i < 9) {
                                if (busCapacity[i] == 0) {
                                    if (count == 1 || count == 4) {
                                        System.out.print(i + 1 + "  Window");
                                        count++;
                                    } else {
                                        System.out.print(i + 1 + "  Seat  ");
                                        count++;
                                    }

                                } else if (busCapacity[i] == 1) {
                                    System.out.print("   Booked");
                                    count++;
                                }
                                if (count >= 5) {
                                    System.out.println("  |");
                                    count = 1;
                                }
                                System.out.print(" | ");
                            } else {
                                if (busCapacity[i] == 0) {
                                    if (count == 1 || count == 4) {
                                        System.out.print(i + 1 + " Window");
                                        count++;
                                    } else {
                                        System.out.print(i + 1 + " Seat  ");
                                        count++;
                                    }

                                } else if (busCapacity[i] == 1) {
                                    System.out.print("   Booked");
                                    count++;
                                }
                                if (count >= 5) {
                                    System.out.println("  |");

                                    count = 1;
                                }
                                System.out.print(" | ");
                            }
                        }

                    }
                    System.out.println("------------------------------------------------|");
                    System.out.println("Ticket : " + busDetails[7] + " per Seat");

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("___________________________________________________");
        System.out.println("Enter 1 for Seat Booking...");
        int choice = scanner.nextInt();
        if (choice == 1) {
            noOfSeatForBooking(busId, allRows, busDetails);
        }

    }

    public static String CurrentDateTime() {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define a custom date format if needed
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the current date and time using the formatter
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    public static void bookingDetails(int seatNo, String[] busDetails) {
        System.out.println("Please Enter the Passenger Details for Seat " + seatNo);
        scanner.nextLine(); // Consume newline left-over
        System.out.print("Passenger Name   : ");
        String name = scanner.nextLine();
        System.out.print("Passenger Age    : ");
        String age = scanner.nextLine();
        System.out.print("Passenger Gender : ");
        String gender = scanner.nextLine();
        System.out.print("Passenger Phone  : ");
        String phone = scanner.nextLine();
        String date = CurrentDateTime();
        try (FileWriter writer = new FileWriter(ticketDetailsFile, true)) {
            writer.append(busDetails[0] + "," + busDetails[4] + "," + name + "," + age + "," + gender + "," + busDetails[5] + ","
                    + busDetails[0] + "," + busDetails[1] + "," + busDetails[6] + "," + busDetails[2] + "," + busDetails[3]
                    + "," + busDetails[7] + "," + phone + "," + seatNo + "," + date + "\n");
            System.out.println("Passenger details written to CSV file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void noOfSeatForBooking(String busId, List<String[]> allRows, String[] busDetails) {
        System.out.println("Enter number of seats for reservation : ");
        noOfSeat = scanner.nextInt();
        bookSeat(busId, allRows, noOfSeat, busDetails);
    }

    public static void bookSeat(String busId, List<String[]> allRows, int noOfSeat, String[] busDetails) {
        for (int i = 0; i < noOfSeat; i++) {
            boolean isBooked = false;

            System.out.println("Choose Your Seat No. : ");
            int seatNo = scanner.nextInt();

            // Find the row for the specified busId
            int rowIndex = -1;
            for (int j = 0; j < allRows.size(); j++) {
                if (allRows.get(j)[4].equals(busId)) {
                    rowIndex = j;
                    break;
                }
            }

            if (rowIndex == -1) {
                System.out.println("Bus ID not found.");
                return;
            }

            bus = allRows.get(rowIndex);

            if (seatNo < 1 || seatNo > bus.length - 8) {
                System.out.println("Invalid Seat number.");
            } else if (bus[seatNo + 7].equals("1")) {
                System.out.println("Sorry, the Seat number " + seatNo + " is already reserved.");
                System.out.println("Please try another seat or 0 for cancel");
                bookSeat(busId, allRows, noOfSeat - i, busDetails);
                return;
            } else {

                bus[seatNo + 7] = "1";
                isBooked = true;
                allRows.set(rowIndex, bus);
            }

            if (isBooked) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                    for (String[] row : allRows) {
                        bw.write(String.join(",", row));
                        bw.newLine();
                    }
                    bookingDetails(seatNo, busDetails);
                    System.out.println("Seat Booked Successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String dateFormatter() {
    	Scanner sc = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.print("Enter date (dd-MM-yyyy): ");
        String dateString = sc.nextLine();

        try {
            LocalDate date = LocalDate.parse(dateString, formatter);
            sc.close(); 
            return date.toString();
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy.");
            dateFormatter();
            sc.close(); 
            return null; 
        }
		
    }
    public static void printTicket() throws Exception {
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Download Your Bus Ticket");
    	System.out.println("Name : ");
    	String name = sc.nextLine();
    	System.out.println("Phone: ");
    	String phone = sc.nextLine();
    	String date = dateFormatter();
    	
    	
    	String line;
        List<String[]> allRows = new ArrayList<>();
    	String[] passengerDetails = new String[15];
    	String[] allDetails;
        try (FileReader file = new FileReader(filePath);
                BufferedReader br = new BufferedReader(file)){
            while ((line = br.readLine()) != null) {
            	allDetails = line.split(",");
                allRows.add(allDetails);

                if (allDetails.length > 2 && allDetails[2].equals(name) 
                		&& allDetails[12].equals(phone)
                		&& allDetails[0].equals(date)) {
             
                    for (int i = 0; i < 15; i++) {
                    	passengerDetails[i] = allDetails[i];
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        DownloadTicket.generateTicket(passengerDetails);

        
    	
    }

    public static void main(String[] args) throws Exception {
    	UserFunctionSystem user = new UserFunctionSystem();
        //user.runFile();
    	printTicket();
        scanner.close(); 
    }
}
