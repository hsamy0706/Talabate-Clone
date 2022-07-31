/*
 * To change this license header, choose License Headers input Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template input the editor.
 */
package talabate;

import daoImpl.OrdersdaoImpl;
import daoImpl.MealsdaoImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author NADA
 */
public class Order {

    //protected static int orderStaticCode = 1;
    OrdersdaoImpl order_db=new OrdersdaoImpl();
    MealsdaoImpl meal_db=new MealsdaoImpl();
     public int orderCode;
    public float orderTotalPrice = 0;
    public double restOffer, restFees;
    public ArrayList<Meal> orderedMeals = new ArrayList<>(20);
    public String  orderDate, customerOfOrder;
    public String ownerNameeOfCurrentBasket = null, restNameOfCurrentBasket = null;
    public boolean isDelivered = false;//used in Restruant's class

    ///////////////customer(current order)
    public Order() {
 
    }

    public Order(int code) //set customer and restaurant's order array after logging in
    {
        
        this.orderCode = code;

        try {
            Order o=order_db.get_orders_detalis(code);
            this.orderDate=o.orderDate;
            this.orderTotalPrice=o.orderTotalPrice;
            this.customerOfOrder=o.customerOfOrder;
            this.isDelivered=o.isDelivered;
            this.ownerNameeOfCurrentBasket=o.ownerNameeOfCurrentBasket;
            this.restNameOfCurrentBasket=o.restNameOfCurrentBasket;
            this.orderedMeals=o.orderedMeals;
       

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getNotesOfMeal(Meal meall) {
        System.out.println("Enter the Quantity of meal that you want:");
        meall.quantity = Talabate.input.nextInt();
        System.out.println("If you want any Special Request Enter your note");
        Talabate.input.nextLine();
        meall.note = Talabate.input.nextLine();
        meall.setMealQuantityPrice();
        
        
        boolean existedMeal = false;
        for(int i=0 ;i < orderedMeals.size() ;i++)
        {
            if(orderedMeals.get(i).mealID==meall.mealID)
            {
                orderedMeals.get(i).quantity += meall.quantity;
                orderedMeals.get(i).note += ", NEXT NOTE: " + meall.note;
                orderedMeals.get(i).mealQuantityPrice += meall.mealQuantityPrice;
                existedMeal = true;
            }
        }
        
        if(existedMeal==false)
        orderedMeals.add(meall);

        this.orderTotalPrice += meall.mealQuantityPrice;

        //after adding
        System.out.println("success:)" + "\n" + "\n");
        System.out.print("________________________________________________________");

    }

    public boolean creatAnOrder(int MID, String custUserName) {
        this.customerOfOrder = custUserName;
        Meal meal = new Meal(MID);
        boolean isOrderCreated = true;

        int answer;
        String ownername;
       try{
            ownername=meal_db.get_Ousername_byMID(MID);
            while (true) {
                if (orderedMeals.isEmpty()) {
                    ownerNameeOfCurrentBasket = ownername;
                    getNotesOfMeal(meal);
                    break;
                } else if (orderedMeals.size() > 0) {
                    if (ownerNameeOfCurrentBasket.equals(ownername)) {
                        getNotesOfMeal(meal);
                        break;
                    } else {
                        System.out.println("Invalid ID ,You can't fill your basket from another Restaurant :(");
                        System.out.println("So if you want to buy this meal your basket will be empty\n Enter 1 to buy this meal and 2 to Skip:");
                        answer = Talabate.input.nextInt();

                        if (answer == 1) {
                            orderedMeals.clear();
                            this.orderTotalPrice = 0;

                            System.out.println("Now you have empty basket and you can buy your meal.");
                        } else {
                            System.out.println("Thank you :)");

                            isOrderCreated = false;
                            break;
                        }
                    }
                }

            }
            return isOrderCreated;
        } catch (SQLException ex) {
            System.out.println("You entersd invalid input please try again");
            System.out.println("Enter meal id that you want to add:");

            int meal_id = Talabate.input.nextInt();
            creatAnOrder(meal_id, customerOfOrder);
            return isOrderCreated;
        }
    }

    //Nada
    public boolean displayBasket() throws SQLException {
        if (orderedMeals.isEmpty() || ownerNameeOfCurrentBasket == null) {
            System.out.println("Your Basket is Empty");
            System.out.print("\n" + "\n");
            return false;
        } else {
         
            System.out.println("Restaurant's Name  " + restNameOfCurrentBasket + "\n");
            System.out.println("Restaurant's Offer = " + restOffer * 100 + " % ");
            System.out.println("Delivery Fees = " + restFees + "EGP");
            System.out.println("Order Total Price : " + (this.orderTotalPrice + restFees - (this.orderTotalPrice * restOffer)) + "EGP");
            System.out.print("\n" + "\n");

            for (int i = 0; i < orderedMeals.size(); i++) {
                System.out.println("Meal " + (i + 1) + " : ");
                orderedMeals.get(i).displayMealInfo();

                System.out.println("Meal's quantity: " + orderedMeals.get(i).quantity);
                System.out.println("Meal's note: " + orderedMeals.get(i).note);

                System.out.println("_________________________________________");

            }
            System.out.print("\n" + "\n");

            System.out.println("Do you want to : ");
            System.out.println("1- Order Now      " + "2- Return Home     ");

            int answer = Talabate.input.nextInt();
            while (true) {
                if (answer != 1 && answer != 2) {
                    System.out.println("you enter invalid data please try again");
                    answer = Talabate.input.nextInt();
                } else {
                    break;
                }
            }

            if (answer == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void insertBasketToDb() {
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            this.orderDate = simpleDateFormat.format(date);
            
            this.orderTotalPrice -= (this.orderTotalPrice * restOffer);
            this.orderTotalPrice += restFees;
        try
        {
          this.orderCode=order_db.insert_order(this);
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
