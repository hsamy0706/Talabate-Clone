/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import talabate.Restaurant;

/**
 *
 * @author Home
 */
public interface Restaurantdao {
    ArrayList<Restaurant> get_res_withOffers() throws SQLException;
    ArrayList<String> select_OwnerOf_Deliverplace(String deliverplace) throws SQLException;
    boolean check_loginData(String username, String password) throws SQLException;

    void delete_deliverplace(String username, String deliverplace) throws SQLException;

    void edit_restOffer(double offer, String username) throws SQLException;

    double get_deliveryFees(String username, String deliverplace) throws SQLException;

    ArrayList<String> get_deliveryplaces(String username) throws SQLException;

    Restaurant get_rest_byRestname(String rest_name) throws SQLException;

    Restaurant get_rest_byRestuser(String username) throws SQLException;

    String get_restname(String username) throws SQLException;

    void insert_deliverplace(String username, String deliverplace, double fees) throws SQLException;

    void insert_restaurant(Restaurant r) throws SQLException;
    
}
