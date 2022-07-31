/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talabate;

import daoImpl.Connect;
import daoImpl.CustomerdaoImpl;
import daoImpl.RestaurantdaoImpl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static talabate.Talabate.input;

/**
 *
 * @author home
 */
public abstract class User {

    private String username;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String e_mail) {
        this.email = e_mail;
    }

    public String getEmail() {
        return email;
    }
    

    public static void login() throws SQLException {
        String username;
        String password;
        CustomerdaoImpl ctable=new CustomerdaoImpl();
        RestaurantdaoImpl rtable=new RestaurantdaoImpl();
        boolean exist = false;

        System.out.print(" Enter user name =>");
        input.nextLine();
        username = input.nextLine();

        System.out.print(" Enter password => ");
        password = input.nextLine();
       
        try {
             exist=rtable.check_loginData(username, password);
            if(exist)
            {
             System.out.println("Exists");
             Restaurant owner = new Restaurant(username, password);
             owner.homeMenu();
            }
          
            if (!exist) 
            {
                exist=ctable.check_Cust_loginData(username, password);
                if(exist)
                {
                   System.out.println("Exists");
                   Customer customer = new Customer(username, password);
                   customer.homeMenu();
                }
               

                else 
                {
                    System.out.println("you entered invalid data please try again \n");
                    Talabate.firstPage();
                }
            }

        } catch (SQLException ex) {
            System.out.println("you entered invalid data please try again \n");
            Talabate.firstPage();
        }

    }

    public abstract void register();

    public abstract void disblayOrders();

}
