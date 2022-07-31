/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.SQLException;
import talabate.Customer;

/**
 *
 * @author Home
 */
public interface Customerdao {

    boolean check_Cust_loginData(String username, String password) throws SQLException;

    void insert_cust(Customer c) throws SQLException;

    Customer read_cust(String username) throws SQLException;
    
}
