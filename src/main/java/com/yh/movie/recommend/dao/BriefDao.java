package com.yh.movie.recommend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.yh.movie.recommend.util.DBHelper;
import com.yh.movie.recommend.util.JDBCUtil;
import com.yh.movie.recommend.entity.Brief;

public class BriefDao {

	public void saveBrief(Brief brief) {
		
		StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO brief (uname, name, mid,score,comment,commentTime) VALUE (? , ? , ? , ? , ?, ? ) ");
        StringBuffer searchSql = new StringBuffer("select * from brief where uname = ? and mid = ?");
		Connection conn=JDBCUtil.getConnection();  
        try {  
        	PreparedStatement serachStmt=conn.prepareStatement(searchSql.toString()); 
        	serachStmt.setString(1, brief.getUname());
        	serachStmt.setString(2, brief.getMid());
        	ResultSet rs = serachStmt.executeQuery(); 
        	if (rs.next()){
        		
        	} else {
        		PreparedStatement stmt=conn.prepareStatement(sql.toString()); 
                stmt.setString(1, brief.getUname());
                stmt.setString(2, brief.getName());
                stmt.setString(3, brief.getMid());
                stmt.setString(4, ""+brief.getScore());
                stmt.setString(5, brief.getComment());
                stmt.setString(6, brief.getCommentTime());
                stmt.execute();  
        	}
            
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  finally {
        	JDBCUtil.closeConn(conn);
        }
	}

}
