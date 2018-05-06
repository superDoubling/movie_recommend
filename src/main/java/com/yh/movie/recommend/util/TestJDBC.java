package com.yh.movie.recommend.util;

import java.sql.Connection;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;

import org.junit.Test;  
  
  
  
public class TestJDBC {  
  
    @Test  
    public void test() {  
        Connection conn=JDBCUtil.getConnection();  
        System.out.println(conn);  
        try {  
            PreparedStatement stmt=conn.prepareStatement("select * from movie");  
            ResultSet re=stmt.executeQuery();  
            while(re.next()){  
                String name=re.getString(2);  
                System.out.println(name);  
                  
            }  
              
              
        } catch (SQLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
      
      
  
} 