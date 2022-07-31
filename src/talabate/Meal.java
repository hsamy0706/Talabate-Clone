/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talabate;

import daoImpl.Connect;
import daoImpl.MealsdaoImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Home
 */
public class Meal {

    public boolean isAvaliable=true; //to know whether a specific meal in an order is still avaliable or not
    public int quantity;
    public int mealID;
    public String name, discription, note, ownerUserName;
    public float price;
    public float mealQuantityPrice;
    
    public Meal(){}

    //used when add new meal
    public Meal(String name, String description, float price) {
        

        this.name = name;
        this.discription = description;
        this.price = price;

    }

    //when i want to know info about meal by meal id
    public Meal(int mealID) {
        
        this.mealID = mealID;
        try {
         MealsdaoImpl db=new MealsdaoImpl();
         Meal m;
         m=db.select_meal(mealID);
        this.name = m.name;
        this.discription = m.discription;
        this.price = m.price;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayMealInfo() {
        System.out.println(" name: " + this.name);
        System.out.println("id: " + this.mealID);
        System.out.println(" Discription: " + this.discription);
        System.out.println(" price: " + this.price + " EGP ");

    }

    public void setMealQuantityPrice() {
        mealQuantityPrice = price * quantity;
    }

}
