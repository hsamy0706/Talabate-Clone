/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImpl;

import dao.Restaurantdao;
import java.sql.*;
import java.util.ArrayList;
import talabate.Restaurant;
/**
 *
 * @author Home
 */
public class RestaurantdaoImpl implements Restaurantdao {
    static PreparedStatement p;
    static ResultSet rs;
    private static final String SELECT_REST_BYusername="select * from RESTURANT where OWNERUSERNAME=TRIM(?)";
    private static final String SELECT_REST_BYrestname="select * from RESTURANT where RESTURANTNAME =TRIM(?)";
    private static final String INSERT_REST="INSERT INTO RESTURANT VALUES(?,?,?,?,?,?)";
    private static final String SELECT_RESTNAME="SELECT RESTURANTNAME FROM RESTURANT where OWNERUSERNAME=TRIM(?)";
    private static final String SELECT_USER_pass= "SELECT OWNERUSERNAME,OWNERPASSWORD FROM RESTURANT "; 
    
    private static final String EDIT_RESTOFFER="update RESTURANT set OFFER=? where OWNERUSERNAME =TRIM(?)";
    private static final String SELECT_RESTOFFER="SELECT LOCATION,OFFER,RESTURANTNAME FROM RESTURANT WHERE OFFER>0.0";
            
    private static final String SELECT_DELIVER="SELECT DELIVERPLACES FROM DELIVERYPLACES WHERE OUSERNAME=TRIM(?) ";
    private static final String SELECT_DELIVERYFEES="SELECT DELIVERYFEES FROM DELIVERYPLACES WHERE OUSERNAME=TRIM(?) AND DELIVERPLACES=TRIM(?)";
    private static final String DELETE_DELIVER="Delete from DELIVERYPLACES where OUSERNAME=TRIM(?) And DELIVERPLACES=TRIM(?)";       
    private static final String INSERT_DELIVERPLACE="INSERT INTO DELIVERYPLACES VALUES(?,?,?) ";
    private static final String Select_OwnerOf_deliverp="select OUSERNAME from DELIVERYPLACES where DELIVERPLACES=TRIM(?)";

    public RestaurantdaoImpl() {
    }
    
    
    @Override
    public Restaurant get_rest_byRestname(String rest_name) throws SQLException
    {
        Restaurant r=new Restaurant();
        r.name=rest_name;
        p=Connect.get_connection().prepareStatement(SELECT_REST_BYrestname);
        p.setString(1, rest_name);
        rs=p.executeQuery();
        if(rs.next())
        {
            r.setUsername(rs.getString("OWNERUSERNAME"));
            //r.setPassword(rs.getString("OWNERPASSWORD"));
           // r.setEmail(rs.getString("OWNEREMAIL"));
            r.location=rs.getString("LOCATION");
            r.offer=rs.getFloat("OFFER");
        }
        return r;

    }
    @Override
    public Restaurant get_rest_byRestuser(String username) throws SQLException
    {
        Restaurant r=new Restaurant();
       p=Connect.get_connection().prepareStatement(SELECT_REST_BYusername);
       p.setString(1, username);
       rs=p.executeQuery();
       rs.next();
       r.setUsername(username);
       r.setPassword(rs.getString("OWNERPASSWORD"));
       r.setEmail(rs.getString("OWNEREMAIL"));
       r.location=rs.getString("LOCATION");
       r.offer=rs.getFloat("OFFER");
       r.name=rs.getString("RESTURANTNAME");
       return r;
    }
    public ArrayList<Restaurant> get_res_withOffers() throws SQLException
    {
     ArrayList<Restaurant> Offers=new ArrayList<>();
     p=Connect.get_connection().prepareStatement(SELECT_RESTOFFER);
     rs=p.executeQuery();
     while(rs.next())
     {
         Restaurant r=new Restaurant();
         r.location=rs.getString("LOCATION");
          r.offer=rs.getFloat("OFFER");
         r.name=rs.getString("RESTURANTNAME");
         Offers.add(r);
     }
     return Offers;
    }
    public ArrayList<String> select_OwnerOf_Deliverplace(String deliverplace) throws SQLException
    {
        ArrayList<String> rest=new ArrayList<>();
        p=Connect.get_connection().prepareStatement(Select_OwnerOf_deliverp);
        p.setString(1, deliverplace);
        rs=p.executeQuery();
        while(rs.next())
        {
            rest.add(rs.getString("OUSERNAME"));
        }
        return rest;
    }
    @Override
    public void insert_restaurant(Restaurant r) throws SQLException
    {
        p=Connect.get_connection().prepareStatement(INSERT_REST);
        p.setString(1, r.getUsername());
        p.setString(2, r.getPassword());
        p.setString(3, r.getEmail());
        p.setString(4, r.location);
        p.setDouble(5, r.offer);
        p.setString(6, r.name);
        p.executeUpdate();
        
    }
    @Override
    public String get_restname(String username) throws SQLException
    {
        p=Connect.get_connection().prepareStatement(SELECT_RESTNAME);
        p.setString(1, username);
        rs=p.executeQuery();
        rs.next();
        return rs.getString("RESTURANTNAME");
    }   
    @Override
    public boolean check_loginData(String username,String password) throws SQLException
    {
        boolean found=false;
        p=Connect.get_connection().prepareStatement(SELECT_USER_pass);
        rs=p.executeQuery();
        while(rs.next())
        {
            if (username.equals(rs.getString("OWNERUSERNAME")) && password.equals(rs.getString("OWNERPASSWORD")))
            {
                found=true;
                break;
            }
        }
        return found;
    }
    @Override
    public void edit_restOffer(double offer,String username) throws SQLException
    {
            p = Connect.get_connection().prepareStatement(EDIT_RESTOFFER);
            p.setDouble(1, offer);
            p.setString(2, username);
            p.executeUpdate();
    }
    
    @Override
    public ArrayList<String> get_deliveryplaces(String username) throws SQLException
    {
        ArrayList<String> d=new ArrayList<>(20);
        p=Connect.get_connection().prepareStatement(SELECT_DELIVER);
        p.setString(1, username);
        rs=p.executeQuery();
        while(rs.next())
        {
         d.add(rs.getString("DELIVERPLACES"));
        }
        return d;
    }
    @Override
    public void delete_deliverplace(String username,String deliverplace) throws SQLException
    {
        p=Connect.get_connection().prepareStatement(DELETE_DELIVER);
        p.setString(1, username);
        p.setString(2, deliverplace);
        p.executeUpdate();
    }
    @Override
    public double get_deliveryFees(String username,String deliverplace) throws SQLException
    {
        p=Connect.get_connection().prepareStatement(SELECT_DELIVERYFEES);
        p.setString(1, username);
        p.setString(2, deliverplace);
        rs=p.executeQuery();
        rs.next();
        return rs.getFloat("DELIVERYFEES");
    }
    @Override
    public void insert_deliverplace(String username,String deliverplace,double fees) throws SQLException
    {
          p = Connect.get_connection().prepareStatement(INSERT_DELIVERPLACE);
            p.setString(1, username);
            p.setString(2, deliverplace);
            p.setDouble(3, fees);
            p.executeUpdate();
    }
}
