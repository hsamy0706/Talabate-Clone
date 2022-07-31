/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import talabate.Meal;

/**
 *
 * @author Home
 */
public interface Mealsdao {

  

    void delete_meal(int id,String ousername) throws SQLException;

     public void insert_consist_of(int code,Meal m) throws SQLException;
     public ArrayList<Meal> Select_consist_of(int code) throws SQLException;

    int insert_meal(Meal m,String username) throws SQLException;

    Meal select_meal(int id) throws SQLException;

    void update_meal(Meal m) throws SQLException;
    
}
