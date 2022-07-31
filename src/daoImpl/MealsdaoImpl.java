/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImpl;

import dao.Mealsdao;
import java.sql.*;
import java.util.ArrayList;
import talabate.Meal;

/**
 *
 * @author Home
 */
public class MealsdaoImpl implements Mealsdao {
    static PreparedStatement p_meal,p_consist,p_restMeal;
    static ResultSet rs_meal,rs_consist,rs_restMeal;
    private static final String DELETEMEALQ="Delete from MEALS where ID=?";
    private static final String SELECTMEALQ="select * from MEALS where ID =?";
    private static final String INSERTMEALQ="insert into MEALS(MEALNAME,DESCRIPTION,PRICE) values(?,?,?)";
    private static final String UPDATEMEALQ="update MEALS set MEALNAME=? ,DESCRIPTION=?,PRICE=? where ID=?";
    
    private static final String  INSERTCONSISTQ="insert into CONSIST_OF values (? , ? , ? , ? , ?,?)";   
    private static final String  SELECTCONSISTQ="select * from CONSIST_OF where ORDERCODE = ?";
    private static final String  UPDATECONSISTQ="UPDATE CONSIST_OF SET ISAVALIABLE=? WHERE MID=?";
    
    private static final String SELECT_RESTMIDQ="SELECT MEALID FROM REST_MEALS WHERE OUSERNAME=TRIM(?)  ";
    private static final String GET_OUSERNAMEQ="SELECT OUSERNAME FROM REST_MEALS WHERE MEALID=? ";
    private static final String INSERT_RESTMEALSQ="INSERT INTO REST_MEALS VALUES(?,?)";
    private static final String DELETE_RESTMEALQ="DELETE FROM REST_MEALS WHERE OUSERNAME=TRIM(?) AND MEALID=?";
    public MealsdaoImpl() {
    }
    
    
    @Override
    public  void delete_meal(int id,String ousername) throws SQLException
    {
       // p_meal=Connect.get_connection().prepareStatement(DELETEMEALQ);
       // p_meal.setInt(1, id);
        
        p_restMeal=Connect.get_connection().prepareStatement(DELETE_RESTMEALQ);
        p_restMeal.setString(1, ousername);
        p_restMeal.setInt(2, id);
         
        p_consist=Connect.get_connection().prepareStatement(UPDATECONSISTQ);
        p_consist.setBoolean(1, false);
        p_consist.setInt(2, id);
        
        p_restMeal.executeUpdate();
        p_consist.executeUpdate();
        //p_meal.executeUpdate();
    }
    
    @Override
    public  int insert_meal(Meal m,String username) throws SQLException
    {
        p_meal=Connect.get_connection().prepareStatement(INSERTMEALQ,Statement.RETURN_GENERATED_KEYS);
        p_meal.setString(1, m.name);
        p_meal.setString(2, m.discription);
        p_meal.setFloat(3, m.price);
        p_meal.executeUpdate();
        rs_meal=p_meal.getGeneratedKeys();
        rs_meal.next();
        m.mealID=rs_meal.getInt(1);
        
        p_restMeal=Connect.get_connection().prepareStatement(INSERT_RESTMEALSQ);
        p_restMeal.setString(1, username);
        p_restMeal.setInt(2, m.mealID);
        p_restMeal.executeUpdate();
        
        return m.mealID;
    }
    @Override
    public void update_meal(Meal m) throws SQLException
    {
         p_meal=Connect.get_connection().prepareStatement(UPDATEMEALQ);
          p_meal.setString(1, m.name);
         p_meal.setString(2, m.discription);
         p_meal.setFloat(3, m.price);
        
         p_meal.setInt(4, m.mealID); 
         p_meal.executeUpdate();
         
    }
    @Override
    public Meal select_meal(int id) throws SQLException
    {
        p_meal=Connect.get_connection().prepareStatement(SELECTMEALQ);
        p_meal.setInt(1, id);
        rs_meal=p_meal.executeQuery();
        rs_meal.next();
        Meal m=new Meal();
        m.name=rs_meal.getString("MEALNAME");
        m.discription=rs_meal.getString("DESCRIPTION");
        m.price=rs_meal.getFloat("PRICE");
        return m;
    }
    
     @Override
    
    public void insert_consist_of(int code,Meal m) throws SQLException
    {
        p_consist=Connect.get_connection().prepareStatement(INSERTCONSISTQ);
        p_consist.setInt(1, m.mealID);
        p_consist.setInt(2, code);
        p_consist.setInt(3, m.quantity);
        p_consist.setString(4, m.note);
        p_consist.setFloat(5, m.mealQuantityPrice);
        p_consist.setBoolean(6, true);
        p_consist.executeUpdate();
        
    }
     @Override
     public ArrayList<Meal> Select_consist_of(int code) throws SQLException
     {
         ArrayList<Meal> ordermeals= new ArrayList<>(20);
         p_consist=Connect.get_connection().prepareStatement(SELECTCONSISTQ);
         p_consist.setInt(1, code);
         rs_consist=p_consist.executeQuery();
         while(rs_consist.next())
         {
         Meal m=this.select_meal(rs_consist.getInt("MID"));
         m.quantity=rs_consist.getInt("QUANTITY");
         m.note=rs_consist.getString("NOTE");
         m.mealQuantityPrice=rs_consist.getFloat("QUANTITYPRICE");
         m.isAvaliable=rs_consist.getBoolean("ISAVALIABLE");
         ordermeals.add(m);
         
         }
        return ordermeals; 
     }
     
     
     public String get_Ousername_byMID(int id) throws SQLException
     {
         p_restMeal=Connect.get_connection().prepareStatement(GET_OUSERNAMEQ);
         p_restMeal.setInt(1, id);
         rs_restMeal=p_restMeal.executeQuery();
         rs_restMeal.next();
         return rs_restMeal.getString("OUSERNAME");
     }
     public ArrayList<Integer> get_RestMIDs(String username) throws SQLException
     {
         ArrayList<Integer> id=new ArrayList<>();
         p_restMeal=Connect.get_connection().prepareStatement(SELECT_RESTMIDQ);
         p_restMeal.setString(1, username);
         rs_restMeal=p_restMeal.executeQuery();
         while(rs_restMeal.next())
         {
             id.add(rs_restMeal.getInt("MEALID"));
         }
         return id;
     }
    
}
