/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talabate;

import daoImpl.OrdersdaoImpl;
import daoImpl.MealsdaoImpl;
import daoImpl.CustomerdaoImpl;
import daoImpl.RestaurantdaoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author home
 */
public class Restaurant extends User {
    RestaurantdaoImpl rest_table=new RestaurantdaoImpl();
    MealsdaoImpl meal_db=new MealsdaoImpl();
    OrdersdaoImpl order_db=new OrdersdaoImpl(); 
    public String name, location;
    public double offer, deliveryFees;
    public int numOfMeals = 0, ordersNum = 0;
    protected boolean valideRestaurant = true;

    private ArrayList<String> deliverPlaces = new ArrayList<>(20);
    private Order orders[] = new Order[50];
    public Meal meals[] = new Meal[50];

    public Restaurant() //Called while registeration
    {

    }

    ////////////////////////////////////////////////////////////////////////////
    public Restaurant(String username, String pass)//if restraunt login to the system
    {
        
        this.setUsername(username);
        this.setPassword(pass);
        try
        {
            Restaurant r=rest_table.get_rest_byRestuser(username);
            this.location = r.location;
            this.offer = r.offer;
            this.name=r.name;
        }catch(SQLException e) {System.out.println(e.getMessage());}
        setMealList();
        setDeliverPlacesList();
        setOrdersArray();
    }

    //if a customer wants to see restraunt's meals by entering restraunt's name
    public Restaurant(String R_name) {
        this.name = R_name;
      
        try {
            Restaurant r=rest_table.get_rest_byRestname(R_name);
            this.setUsername(r.getUsername());
            this.setPassword(r.getPassword());
            this.location = r.location;
            this.offer = r.offer;
            setMealList();
            setDeliverPlacesList(); //to know if this restaurant can deliver to this customer or not
            valideRestaurant = true;
        } catch (SQLException e) {
            valideRestaurant = false;
        }

    }

    @Override
    public void register() {
        String mail, username, password;
        boolean valid = true;
        System.out.println("Please,Enter your name:");
        username = Talabate.input.nextLine();
        this.setUsername(username);

        do {
            System.out.println("Enter password:");
            password = Talabate.input.nextLine();
            this.setPassword(password);
            if (password.contains(" ")) {
                System.out.println("**please enter valid password**");
                valid = false;
            } else {
                valid = true;
            }
        } while (!valid);

        System.out.print("Please,Enter Restaurant name : ");
        name = Talabate.input.nextLine();
        System.out.print("Please,Enter your email : ");
        mail = Talabate.input.nextLine();
        setEmail(mail);
        System.out.print("Please,Enter Restaurant's address : ");
        location = Talabate.input.nextLine();
        System.out.print("Please,Enter Restaurant's offer : ");
        offer = Talabate.input.nextDouble();
        boolean isChecked = false;
        while (!isChecked) {
            if (offer < 1) {
                isChecked = true;
            } else {
                System.out.println("Invalid offer ,Enter decimal number less than 1 value");
                offer = Talabate.input.nextDouble();
            }
        }

        System.out.print("\n" + "\n");
        try {
            rest_table.insert_restaurant(this);
 
        } catch (SQLException ex) {
            System.out.println("This name already exists");
            register();
        }
    }

    private void setMealList() {

        int i=0;
        try {
             
             ArrayList<Integer> id=meal_db.get_RestMIDs(this.getUsername());
             for( i=0 ;i<id.size();i++)
             {
                 meals[i] = new Meal(id.get(i));
             }
          
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        numOfMeals = i;
    }

    private void setDeliverPlacesList() {
       
        try {
            deliverPlaces=rest_table.get_deliveryplaces(this.getUsername());
         
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void homeMenu() throws SQLException {
        // Owner's home
        System.out.println("                          Welcome to Talabate                     ");
        System.out.println("==================================================================");
        System.out.println("Do you want to :");
        System.out.println("1- See Your Meals");
        System.out.println("2- Add a new Meal");
        System.out.println("3- Add an Offer");
        System.out.println("4- See Customers' Orders");
        System.out.println("5- See your deliver Places");
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

    public void home(int answer) throws SQLException {
        switch (answer) {
            case 1:
                this.displayMealList(true);
                this.homeMenu();
                break;

            case 2:
                System.out.println("Please Enter Meal's Name :");
                Talabate.input.nextLine();
                String mName = Talabate.input.nextLine();
                System.out.println("Meal's Description :");
                String mDescription = Talabate.input.nextLine();
                System.out.println("Meal's Price :");
                float mPrice = Talabate.input.nextFloat();
                Meal m = new Meal(mName, mDescription, mPrice);

                this.addMeal(m);
                this.homeMenu();
                break;

            case 3:
                this.editRestOffer();
                this.homeMenu();
                break;

            case 4:
                this.disblayOrders();
                this.homeMenu();
                break;

            case 5:
                this.displayDilverPlaces();
                this.homeMenu();
                break;

            case 6:
                Talabate.firstPage();
                break;
        }

    }

    public boolean displayMealList(boolean isOwner) throws SQLException {
        if (valideRestaurant == false) {
            System.out.println("There is no Restaurant with this name");
            return false;
        }

        if (numOfMeals == 0 && valideRestaurant == true) {
            System.out.println("There is no meals to display");
            return false;
        } else {
            for (int i = 0; i < numOfMeals; i++) {
                meals[i].displayMealInfo();
                System.out.println("==========================================="
                        + "============");
            }
            System.out.print("\n" + "\n");
            if (isOwner) {
                System.out.println("Do you want to:");
                System.out.println("1- Return Home");
                System.out.println("2- Edit a Meal");
                System.out.println("3- Remove a Meal");
                while (true) {
                    int ans = Talabate.input.nextInt();
                    if (ans < 1 || ans > 3) {
                        System.out.println("this is invalid number please enter number between 1 and 3");
                    } else {
                        System.out.print("\n" + "\n");
                        switch (ans) {
                            case 1:
                                this.homeMenu();
                                break;
                            case 2:
                                this.editMeal();
                                break;
                            case 3:
                                this.removeMeal();
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                }
            }

            return true;
        }
    }

    private void editMeal() {
        System.out.println("please enter meal id:");
        int n, index = 0;

        int id;
        id = Talabate.input.nextInt();
        boolean isExisted = false;
        for (int i = 0; i < numOfMeals; i++) {
            if (meals[i].mealID==id) {
                isExisted = true;
                index = i;
                break;
            }
        }

        if (isExisted) {
            boolean isValid = false;
            do {
                System.out.println("1:edit name" + "\n" + "2:edit discription" + "\n" + "3:edit price"
                        + "\n" + "4:edit more than one thing");
                n = Talabate.input.nextInt();
                switch (n) {
                    case 1:
                        System.out.println("Enter new name:");
                        Talabate.input.nextLine();
                        meals[index].name = Talabate.input.nextLine();
                        isValid = true;
                        System.out.println("Done :)");
                        break;
                    case 2:
                        System.out.println("Enter new discription:");
                        Talabate.input.nextLine();
                        meals[index].discription = Talabate.input.nextLine();
                        isValid = true;
                        System.out.println("Done :)");
                        break;
                    case 3:
                        System.out.println("Enter new price:");
                        meals[index].price = Talabate.input.nextFloat();
                        isValid = true;
                        System.out.println("Done :)");
                        break;
                    case 4:
                        System.out.println("Enter new name:");
                        Talabate.input.nextLine();
                        meals[index].name = Talabate.input.nextLine();
                        System.out.println("Enter new discription:");
                        meals[index].discription = Talabate.input.nextLine();
                        System.out.println("Enter new price:");
                        meals[index].price = Talabate.input.nextFloat();
                        isValid = true;
                        System.out.println("Done :)");
                        break;

                    default:
                        System.out.println("invalid input");
                        break;
                }
            } while (!isValid);
            
            try {
                
                meal_db.update_meal(meals[index]);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else if (!isExisted) {
            System.out.println("this id doesn't exist ,do you wan't to try again?y/n");
            String ans = Talabate.input.next();
            if (ans.equals("y")) {
                this.editMeal();
            }

        }
    }

    private void removeMeal() {
        if (numOfMeals == 0) {
            System.out.println("There is no meals to remove");
        } else {
            System.out.println("please enter meal id:");
            int mealId = Talabate.input.nextInt();

            boolean found = false;
            for (int i = 0; i < numOfMeals; i++) {
                if (meals[i].mealID==mealId) {
                    found = true;
                    
                }  if (found) {
                    if (i != numOfMeals - 1) {
                        meals[i] = meals[i + 1];
                    } else {
                        meals[i] = null;
                    }
                }
            }
            if (!found) {
                System.out.println("There is no meal with this id");
            } else {
                try {
                    meal_db.delete_meal(mealId, this.getUsername());
                } catch (SQLException ee) {
                    System.out.println(ee.getMessage());
                }
                numOfMeals--;
            }
        }
    }

    private void addMeal(Meal m) {
        meals[numOfMeals] = m;
        numOfMeals++;

        try {
            m.mealID=meal_db.insert_meal(m, this.getUsername());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void editRestOffer() {
            int answer;
            System.out.println("The current Offer is : " + offer * 100 + " %");//Show Old offer in %

            System.out.println("Do yo want to : ");
            System.out.println("1 - Return Home ");
            System.out.println("2 - Edit Restaurant's Offer");
                        
            while (true) 
            {
                answer = Talabate.input.nextInt();
                if (answer != 2 && answer != 1) {
                    System.out.println("Invalid answer, Please enter a number between 1 and 2");
                } else {
                    if (answer == 1) {
                        return;
                    }
                    break;
                }
            }
            System.out.println("Edit your offer in float number");

            offer = Talabate.input.nextDouble();
            boolean isChecked = false;
            while (!isChecked) {
                if (offer < 1) {
                    isChecked = true;
                } else {
                    System.out.println("Invalid offer ,Enter decimal number less than 1 value");
                    offer = Talabate.input.nextDouble();
                }
            }

           try {

            System.out.println("offer : " + offer);
            rest_table.edit_restOffer(offer, this.getUsername());
            System.out.print("\n" + "\n");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void disblayOrders() {
        if (ordersNum == 0) {
            System.out.println("You do not have any orders yet :(");
        }

        for (int k = 0; k < this.ordersNum; k++) {

            //PRINT CUSTOMER'S INFORMATION
            System.out.println("Customer username of the order   :" + orders[k].customerOfOrder);

            try {
                 CustomerdaoImpl db=new CustomerdaoImpl();
                 Customer c=db.read_cust(orders[k].customerOfOrder);
                System.out.println("Customer's address   :   " +c.address);
                System.out.println("Customer's mobile   :   " + c.mobile);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            System.out.print("\n" + "\n");

            //PRINT ORDER'S INFORMATION
            System.out.println("OrderCode: " + orders[k].orderCode + "\n" + "Date: " + orders[k].orderDate + "\n" + "TotalPrice: " + orders[k].orderTotalPrice);
            System.out.print("\n" + "\n");

            //PRINT MEALS' OF THE ORDER INFORMATION
            for (int j = 0; j < orders[k].orderedMeals.size(); j++) {
                orders[k].orderedMeals.get(j).displayMealInfo();

                System.out.println("\n" + "MealNote: " + orders[k].orderedMeals.get(j).note);
                System.out.println("MealsQuantity: " + orders[k].orderedMeals.get(j).quantity + "\n" + "QuanitityPrice: " + orders[k].orderedMeals.get(j).mealQuantityPrice);
                System.out.print("\n");
                if(!orders[k].orderedMeals.get(j).isAvaliable)
                {
                    System.out.println("This meal isn't avaliable now"+"\n");
                }
            }
            System.out.print("__________________________________________________________________");
            System.out.print("\n" + "\n");
        }
        
    }

    private void displayDilverPlaces() throws SQLException {
        int choice;
        String deliver;
        double fees;
        boolean isCorrect = false;
        if (deliverPlaces.isEmpty()) {
            System.out.println("there is no deliver places to display");
        } else {
            System.out.println(deliverPlaces);
        }
        do {
            System.out.println("\n");
            System.out.println("1: Return Home");
            System.out.println("2: Add deliver place");
            System.out.println("3: Remove deliver place");
            System.out.println("please enter your choice");
            choice = Talabate.input.nextInt();
            switch (choice) {
                case 1:
                    this.homeMenu();
                    break;
                case 2:
                    System.out.println("please enter deliver place:");
                    Talabate.input.nextLine();
                    deliver = Talabate.input.nextLine();

                    System.out.println("please enter dilvery fees:");
                    fees = Talabate.input.nextDouble();

                    addDeliverPlace(deliver, fees);
                    isCorrect = true;
                    break;
                case 3:
                    removeDeliverPlace();
                    isCorrect = true;
                    break;
                default:
                    System.out.println("you enter invalid input please try again");
                    isCorrect = false;
                    break;
            }
        } while (!isCorrect);
    }

    private void addDeliverPlace(String delverplace, double dilveryFees) {
        deliverPlaces.add(delverplace);
        
        try {
            rest_table.insert_deliverplace(this.getUsername(), delverplace, dilveryFees);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeDeliverPlace() {
        System.out.println("Please enter the dilver place which you want to delete");
        Talabate.input.nextLine();
        String deliverPlace = Talabate.input.nextLine();
        boolean found = false;
        for (int i = 0; i < deliverPlaces.size(); i++) {
            if (deliverPlaces.get(i).equals(deliverPlace)) {
                found = true;
                deliverPlaces.remove(i);
                try {
                    rest_table.delete_deliverplace(this.getUsername(), deliverPlace);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }
        }
        if (!found) {
            System.out.println("Invalid data");
            System.out.println("Do you want to \n1- Return home     2-Try again");

            int answer = Talabate.input.nextInt();
            if (answer == 2) {
                removeDeliverPlace();
            }

        }
    }

    //check if this restaurant can deliver to this customer or not
    public boolean delivers(String cust_region) {
        boolean isDeliver = false;
        for (int i = 0; i < deliverPlaces.size(); i++) {
            if (deliverPlaces.get(i).equals(cust_region)) {
                isDeliver = true;
                
                try{
                   this.deliveryFees= rest_table.get_deliveryFees(this.getUsername(), cust_region);
         
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
        }
        return isDeliver;
    }

    public double getOffer() {
        return offer;
    }

    public double getDeliveryFees() {
        return deliveryFees;
    }

    //Nada
    private void setOrdersArray() {
       
        String sql1 = "select * from REST_ORDERS where OUSERNAME =TRIM(?)";
        int i = 0;
        try {
      
             ArrayList<Integer> codes=order_db.get_OrdersCodes(sql1, this.getUsername());
             for(i =0;i<codes.size();i++)
             {
                orders[i] = new Order(codes.get(i)); 
             }
        

            ordersNum = i;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //Nada

}
