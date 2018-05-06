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
//		DBHelper dbhelper = new DBHelper();
//        StringBuffer sql = new StringBuffer();
//        sql.append("INSERT INTO brief (uname, mid,score,comment,commentTime) VALUE (? , ? , ? , ? , ? ) ");
//        //设置 sql values 的值
//        List<String> sqlValues = new ArrayList<>();
//        sqlValues.add(brief.getUname());
//        sqlValues.add(brief.getMid());
//        sqlValues.add(brief.getScore().toString());
//        sqlValues.add(brief.getComment());
//        sqlValues.add(brief.getCommentTime());
//        try{
//            int result = dbhelper.executeUpdate(sql.toString(),sqlValues);
//            return result;
//        }catch(Exception e){
//        	e.printStackTrace();
//        	return -1;
//        }finally{
//            dbhelper.close();
//        }
		
		StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO brief (uname, name, mid,score,comment,commentTime) VALUE (? , ? , ? , ? , ?, ? ) ");
		Connection conn=JDBCUtil.getConnection();  
        try {  
            PreparedStatement stmt=conn.prepareStatement(sql.toString()); 
            stmt.setString(1, brief.getUname());
            stmt.setString(2, brief.getName());
            stmt.setString(3, brief.getMid());
            stmt.setString(4, ""+brief.getScore());
            stmt.setString(5, brief.getComment());
            stmt.setString(6, brief.getCommentTime());
            stmt.execute();  
            JDBCUtil.closeConn(conn);
        } catch (SQLException e) {  
            e.printStackTrace();  
            JDBCUtil.closeConn(conn);
        }  
	}

}
