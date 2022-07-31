/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daoImpl;

import dao.Customerdao;
import java.sql.*;
import talabate.Customer;
/**
 *
 * @author Home
 */
public class CustomerdaoImpl implements Customerdao {
    static PreparedStatement p;
    static ResultSet rs;
     private static final String INSERT_CUSTQ="INSERT INTO CUSTOMER VALUES (?,?,?,?,?,?)";
     private static final String SELECT_CUSTQ="SELECT * FROM CUSTOMER WHERE CUSTUSERNAME=TRIM(?) ";
     private static final String SELECT_USER_PASS="SELECT CUSTUSERNAME,CUSTPASS FROM CUSTOMER";

    public CustomerdaoImpl() {
    }
     
     
    @Override
     public void insert_cust(Customer c) throws SQLException
     {
         p=Connect.get_connection().prepareStatement(INSERT_CUSTQ);
            p.setString(1, c.getUsername());
            p.setString(2, c.getPassword());
            p.setString(3, c.region);
            p.setString(4, c.address);
            p.setString(5, c.getEmail());
            p.setString(6, c.mobile);
            p.execute();
         
     }
    @Override
     public Customer read_cust(String username) throws SQLException
     {
         Customer c=new Customer();
         c.setUsername(username);
         p=Connect.get_connection().prepareStatement(SELECT_CUSTQ);
         p.setString(1, username);
         rs=p.executeQuery();
         rs.next();
         c.address=rs.getString("ADDRESS");
         c.mobile=rs.getString("PHONENUMBER");
         c.region=rs.getString("CREGION");
         c.setEmail(rs.getString("CEMAIL"));
         c.setPassword(rs.getString("CUSTPASS"));
         return c;
     }
    @Override
     public boolean check_Cust_loginData(String username,String password) throws SQLException
     {
         boolean found = false;
         p=Connect.get_connection().prepareStatement(SELECT_USER_PASS);
         rs=p.executeQuery();
         while(rs.next())
         {
             if (username.equals(rs.getString("CUSTUSERNAME")) && password.equals(rs.getString("CUSTPASS")))
            {
                found=true;
                break;
            }
         }
         return found;
     }
}
