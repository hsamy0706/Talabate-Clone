
package daoImpl;

import dao.Ordersdao;
import java.sql.*;
import java.util.ArrayList;
import talabate.Order;


public class OrdersdaoImpl implements Ordersdao {
    static PreparedStatement p,p_restOrder,p_custOrder;
    static ResultSet rs,rs_restOrder,rs_custOrder;
    private static final String SelectRestOrderQ="select * from REST_ORDERS where ORDERCODE=?";
    private static final String SelectCustOrderQ="select * from CUST_ORDERS where ORDERCODE=?";
   
    private static final String INSERT_RESTORDERQ="insert into REST_ORDERS values (?,?,?) ";
    private static final String INSERT_CUSTORDERQ="INSERT INTO CUST_ORDERS VALUES(?,?,?) ";
    private static final String INSERT_ORDERSQ="INSERT INTO ORDERS(DATE) VALUES(?) ";
    
    private static final String SelectDateQ="Select DATE from ORDERS where CODE=? ";
    private static final String GETLASTCODEQ="Select max(CODE) from ORDERS  ";

    public OrdersdaoImpl() {
    }
    
    @Override
    public int get_lastCode() throws SQLException
    {
        int i=0;
        p=Connect.get_connection().prepareStatement(GETLASTCODEQ);
        rs=p.executeQuery();
        if(rs.next())
        {
            i=rs.getInt(1);
            return i;
        }
        else
            return i;
        
    }
    @Override
    public String get_orderDate(int code) throws SQLException
    {
        p=Connect.get_connection().prepareStatement(SelectDateQ);
        p.setInt(1, code);
        rs=p.executeQuery();
        rs.next();
       String d= rs.getString("DATE");
       return d;
    }
    @Override
    public  ArrayList<Integer> get_OrdersCodes(String query,String username) throws SQLException
    {
        ArrayList<Integer> codes=new ArrayList<>();
        p=Connect.get_connection().prepareStatement(query);
        p.setString(1, username);
        rs=p.executeQuery();
        while(rs.next())
        {
           codes.add(rs.getInt("ORDERCODE"));
        }
       return codes;
    }
    
    @Override
    public Order get_orders_detalis(int code) throws SQLException
    {
        Order o=new Order();
        MealsdaoImpl consistof=new MealsdaoImpl();
        RestaurantdaoImpl rest=new RestaurantdaoImpl();
        p_custOrder=Connect.get_connection().prepareStatement(SelectCustOrderQ);
        p_restOrder=Connect.get_connection().prepareStatement(SelectRestOrderQ);
        p_custOrder.setInt(1, code);
        p_restOrder.setInt(1, code);
        rs_custOrder=p_custOrder.executeQuery();
        rs_restOrder=p_restOrder.executeQuery();
        rs_custOrder.next();
        rs_restOrder.next();
        o.orderCode=code;
        o.customerOfOrder=rs_custOrder.getString("CUSERNAME");
        o.orderTotalPrice=rs_custOrder.getFloat("TOTALPRICE");
        o.isDelivered=rs_restOrder.getBoolean("ISDELIVERED");
        o.ownerNameeOfCurrentBasket=rs_restOrder.getString("OUSERNAME");
        o.restNameOfCurrentBasket=rest.get_restname(o.ownerNameeOfCurrentBasket);
        o.orderDate=this.get_orderDate(code);
        o.orderedMeals=consistof.Select_consist_of(code);
        return o;
        
    }
    
    @Override
    public int insert_order(Order o) throws SQLException
    {
        MealsdaoImpl consist=new MealsdaoImpl();
        p=Connect.get_connection().prepareStatement(INSERT_ORDERSQ, Statement.RETURN_GENERATED_KEYS);
        p.setString(1, o.orderDate);
        p.executeUpdate();
        rs=p.getGeneratedKeys();
        rs.next();
        o.orderCode=rs.getInt(1);
        
        p_custOrder=Connect.get_connection().prepareStatement(INSERT_CUSTORDERQ);
        p_custOrder.setString(1,o.customerOfOrder);
        p_custOrder.setInt(2, o.orderCode);
        p_custOrder.setFloat(3, o.orderTotalPrice);
        p_custOrder.executeUpdate();
         
        p_restOrder=Connect.get_connection().prepareStatement(INSERT_RESTORDERQ);
        p_restOrder.setString(1, o.ownerNameeOfCurrentBasket);
        p_restOrder.setInt(2, o.orderCode);
        p_restOrder.setBoolean(3, o.isDelivered);
        p_restOrder.executeUpdate();
        for(int i=0 ;i< o.orderedMeals.size();i++)
        {
            consist.insert_consist_of(o.orderCode, o.orderedMeals.get(i));
        }
        
        
      return o.orderCode;  
    }
}
