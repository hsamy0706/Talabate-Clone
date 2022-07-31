/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talabate;


import daoImpl.OrdersdaoImpl;
import daoImpl.CustomerdaoImpl;
import daoImpl.RestaurantdaoImpl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Customer extends User {
    CustomerdaoImpl cust_db= new CustomerdaoImpl();
    OrdersdaoImpl order_db=new OrdersdaoImpl();
    RestaurantdaoImpl rest_db= new RestaurantdaoImpl();
    protected Restaurant openedRestaurant;
    protected Order currentOrder = new Order();
    private Order orders[] = new Order[50];
    private int  ordersNum = 0;
    public String address, region,mobile;
  
    public Customer() {
    }

    public Customer(String username, String pass) {
        this.setUsername(username);
        this.setPassword(pass);

        try {
             Customer c=cust_db.read_cust(username);
          
            address = c.address;
            region = c.region;
            mobile = c.mobile;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        setOrdersArray();
    }

    @Override
    public void register() {
        String mail, username, password;
        boolean valid = true;
        Scanner input = new Scanner(System.in);
        System.out.println("Please,Enter your name:");
        username = input.nextLine();
        this.setUsername(username);
        do {
            System.out.println("Enter password:");
            password = input.nextLine();
            this.setPassword(password);
            if (password.contains(" ")) {
                System.out.println("**please enter valid password**");
                valid = false;
            } else {
                valid = true;
            }
        } while (!valid);

        System.out.print("Please,Enter your phone number:");
        mobile = input.nextLine();
        input.nextLine();
        System.out.print("Please,Enter your region : ");
        region = input.nextLine();
        System.out.print("Please,Enter your address : ");
        address = input.nextLine();
        System.out.print("Please,Enter your email : ");
        mail = input.nextLine();
        setEmail(mail);

        System.out.print("\n" + "\n");

        try {
           cust_db.insert_cust(this);
        } catch (SQLException ex) {
            System.out.println("This name already exists\n");

            register();
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    private void setOrdersArray() //called from constructor
    {
        String orderedQuery = "select * from CUST_ORDERS where CUSERNAME =TRIM(?)";

        int i = 0;
        try {
            
            ArrayList<Integer> codes=order_db.get_OrdersCodes(orderedQuery,this.getUsername() );
            for(i=0;i<codes.size();i++)
            {
                orders[i]=new Order(codes.get(i));
            }
            ordersNum = i;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void homeMenu() throws SQLException {
        // Customer's home
        System.out.println("                          Welcome to Talabate                     ");
        System.out.println("==================================================================");
        System.out.println("Do you want to see:");
        System.out.println("1- Search Restaurant");
        System.out.println("2- Talabat's Restaurant's");
        System.out.println("3- Talabat's offers");
        System.out.println("4- Your orders");
        System.out.println("5- Basket");
        System.out.println("6- Logout");
        int answer;

        while (true) {
            answer = Talabate.input.nextInt();
            if (answer > 6 || answer < 1) {
                System.out.println("Invalid answer, Please enter a number between 1 and 5");
            } else {
                break;
            }
        }
        System.out.print("\n" + "\n");
        this.home(answer);
    }

    private void home(int answer) throws SQLException {
        switch (answer) {
            case 1:
                this.searchRestaurant();
                break;
               
            case 2:
                this.displayRestaurants();
                System.out.println("Do you want to ");
                System.out.println("1- Return Home");
                System.out.println("2- Open a Restaurant");

                while (true) {
                    int ans = Talabate.input.nextInt();
                    if (ans != 2 && ans != 1) {
                        System.out.println("Invalid answer, Please enter a number between 1 and 2");
                    } else {
                        if (ans == 1) {
                            this.homeMenu();
                        } else if (ans == 2) {
                            this.searchRestaurant();
                        }
                        break;
                    }

                }
                break;
              
            case 3:
                if (this.displayTalabatOffers() == 1) {
                    this.homeMenu();
                } else {
                    this.searchRestaurant();
                }

                break;
            
            case 4:
                this.disblayOrders();
                this.homeMenu();
                break;
             
            case 5:
                 
                //Returns true if ther order is submitted
                if (currentOrder.displayBasket() == false) {
                    this.homeMenu();
                } 
                //Returns false if the basket is empty or the customer wants to return home
                else {
                    currentOrder.insertBasketToDb();
                    this.updateOrdersArray();
                    this.homeMenu();
                }
                break;
            
            case 6:
                Talabate.firstPage();
                break;
        }

    }

    private void searchRestaurant() throws SQLException {
        System.out.println("Please enter Restaurant's name");
        Talabate.input.nextLine();
        String rest_name = Talabate.input.nextLine();

        System.out.print("\n" + "\n");

        openedRestaurant = new Restaurant(rest_name); // holds all the current Restaurant's info 
        if (openedRestaurant.valideRestaurant == true && openedRestaurant.delivers(this.getRegion())) {
            while (true) {
                
                boolean viewedMeals = openedRestaurant.displayMealList(false);
                if (viewedMeals == false) {
                    this.homeMenu();
                } else if (viewedMeals == true) {
                    System.out.println("Do you want to :");
                    System.out.println("1- make an order       " + "2- Return Home       ");
                    int answer,mealID;
                    boolean found=false;
                    while (true) {
                        answer = Talabate.input.nextInt();
                        if (answer != 2 && answer != 1) {
                            System.out.println("Invalid answer, Please enter a number between 1 and 2");
                        } else {
                            break;
                        }
                    }

                    switch (answer) {
                        case 1:
                            do{
                            System.out.println("Please enter Meal's ID that you want");
                             mealID = Talabate.input.nextInt();
                            for(int i=0;i<openedRestaurant.numOfMeals;i++)
                            {
                                if(mealID==openedRestaurant.meals[i].mealID)
                                {
                                    found =true;
                                    break;
                                }
                            }
                            if(!found)
                                    System.out.println("this meal doesn't exist in this restaurant ,please try again");
                            }while(!found);
                            if (this.currentOrder.creatAnOrder(mealID, this.getUsername()) == true) {
                                this.currentOrder.restFees = this.openedRestaurant.getDeliveryFees();
                                this.currentOrder.restOffer = this.openedRestaurant.getOffer();
                                this.currentOrder.restNameOfCurrentBasket=this.openedRestaurant.name;
                            }
                            this.homeMenu();
                            break;

                        case 2:
                            this.homeMenu();
                            break;
                    }
                }
                System.out.print("\n" + "\n");
            }
        } else {
            System.out.println("There are not any retaurant with this name delivers to " + this.getRegion());
            this.homeMenu();
        }

    }

    public void displayRestaurants() throws SQLException
    {

        boolean isRestExists = false;

        try{
          
                 ArrayList<String> ousername=rest_db.select_OwnerOf_Deliverplace(this.region);
                 for(int i=0;i<ousername.size();i++)
                 {
                     Restaurant r=rest_db.get_rest_byRestuser(ousername.get(i));
                    isRestExists = true;
                     System.out.println("                       " + r.name);
                     System.out.println("Location:   " + r.location);
                     System.out.println("Offer : " + r.offer * 100 + " %");
                     System.out.println("=====================================================================");
                     System.out.print("\n" + "\n");
                 }
       
            if (isRestExists == false) {
                System.out.println("There are no Restaurants yet :(");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int displayTalabatOffers() throws SQLException {


        boolean isOfferExists = false;
        
        ArrayList<Restaurant> offers=rest_db.get_res_withOffers();
        for(int i=0 ;i<offers.size();i++)
        {
             isOfferExists = true;
            System.out.println("RESTURANT'S NAME : " + offers.get(i).name);
            System.out.println("RESTAURANT'S LOCATION: " + offers.get(i).location);
            System.out.println("RESTAURANT'S OFFER: " + offers.get(i).offer * 100 + " %" + "\n");
            System.out.println("=====================================================================");
            System.out.print("\n" + "\n");
        }
    

        if (isOfferExists == false) {
            System.out.println("There are no offers yet :(");
        }

        System.out.println("\n" + "Do you want to" + "\n");
        System.out.println("1- Return home" + "\n" + "2- Open a restaurant " + "\n");
        while (true) {
            int answer = Talabate.input.nextInt();
            if (answer < 1 || answer > 2) {
                System.out.println("Please enter a valid answer" + "\n");
                continue;
            }
            System.out.print("\n" + "\n");
            return answer;
        }
    }

    @Override
    public void disblayOrders() {
        if (ordersNum == 0) {
            System.out.println("You have not ordered anything yet :(");
        }

        for (int k = 0; k < this.ordersNum; k++) {
            
            System.out.println("OrderCode: " + orders[k].orderCode + "\n" + "Restaurant's Name: " + orders[k].restNameOfCurrentBasket);
            System.out.println("Total_price: " + orders[k].orderTotalPrice);
            System.out.println("OrderDate: " + orders[k].orderDate + "\n");
            for (int j = 0; j < orders[k].orderedMeals.size(); j++) {
                orders[k].orderedMeals.get(j).displayMealInfo();

                System.out.println("\n" + "MealNote: " + orders[k].orderedMeals.get(j).note);
                System.out.println("MealsQuantity: " + orders[k].orderedMeals.get(j).quantity + "\n" + "QuanitityPrice: " + orders[k].orderedMeals.get(j).mealQuantityPrice);
                System.out.print("\n" + "\n");
                if(!orders[k].orderedMeals.get(j).isAvaliable)
                {
                    System.out.println("This meal isn't avaliable now"+"\n");
                }
                System.out.println("________");
            }
            System.out.println("=====================================================================");
        }

        System.out.print("\n" + "\n");

    }

    private void updateOrdersArray() {
        this.orders[ordersNum] = this.currentOrder;
        this.ordersNum++;

        this.currentOrder = new Order();

    }

}
