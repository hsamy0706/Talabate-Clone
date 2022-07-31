package talabate;


import java.sql.SQLException;
import java.util.Scanner;

public class Talabate {

    
    static String restQuery, custQuery;
    static Scanner input = new Scanner(System.in);
    static Restaurant owner;
    static Customer customer;

    public static void firstPage() throws SQLException {
        int choice;
        System.out.println("press 1 to login.\npress 2 to register.");
        choice = input.nextInt();
        if (choice == 1) {
            User.login();
        } else if (choice == 2) {
            boolean Iscorrect = true;
            do {

                System.out.println("press 1 to register as a customer.\npress 2 to register as a restaurant's owner.");
                choice = input.nextInt();
                input.nextLine();
                switch (choice) {
                    case 1:
                        customer = new Customer();
                        customer.register();
                        customer.homeMenu();
                        Iscorrect = true;
                        break;
                    case 2:
                        owner = new Restaurant();
                        owner.register();
                        owner.homeMenu();
                        Iscorrect = true;
                        break;
                    default:
                        System.out.println("Warning!!\nmake the correct choice or you will exit the program\n");
                        Iscorrect = false;
                        break;
                }

                System.out.print("\n" + "\n");
            } while (!Iscorrect);
        } else {
            System.out.println("Warning!!\nmake the correct choice or you will exit the program\n");
            firstPage();
        }

    }

    public static void main(String[] args) throws SQLException {

        System.out.print("\t\t\t\t\t\tTalabat clone application\n\t\t\t\t\t\t*************************\n");
        firstPage();

    }

}
