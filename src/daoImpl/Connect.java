package daoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//singleton pattern
public class Connect {

    private static Connection con=null;
    private Connect(){}
    public static Connection get_connection() 
    {
        if(con == null)
        {
          try
           {
             con=DriverManager.getConnection("jdbc:derby://localhost:1527/Resturant","Rest","1234");
             System.out.println("connected");
           }
           catch(SQLException e)
           {
            System.out.println(e.getMessage());
           }
        }
 
        return con;
    }
}
