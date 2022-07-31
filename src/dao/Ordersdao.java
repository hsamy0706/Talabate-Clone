/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import talabate.Order;

/**
 *
 * @author Home
 */
public interface Ordersdao {

    ArrayList<Integer> get_OrdersCodes(String query, String username) throws SQLException;

    int get_lastCode() throws SQLException;

    String get_orderDate(int code) throws SQLException;

    Order get_orders_detalis(int code) throws SQLException;

    int insert_order(Order o) throws SQLException;
    
}
