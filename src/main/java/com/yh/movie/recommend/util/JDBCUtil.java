package com.yh.movie.recommend.util;

import java.sql.Connection;  
import java.sql.SQLException;  
  
import javax.sql.DataSource;  
  
import com.mchange.v2.c3p0.ComboPooledDataSource;  
  
public class JDBCUtil {  
  
    private static DataSource dataSource = null;  
    static{  
        dataSource = new ComboPooledDataSource("mysql");  
    }  
      
    /** 
     * 获取数据库连接 
     * @return 
     */  
    public static Connection getConnection(){  
        Connection conn=null;  
        try {  
             conn=dataSource.getConnection();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return conn;  
    }  
  
      
    /** 
     * 关闭数据库连接 
     * @param conn 
     */  
    public static void closeConn(Connection conn){  
        try {  
            if(conn!=null && !conn.isClosed()){  
                conn.close();  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}  
