package ys.profiler.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlMain {

	public static void main(String[] args) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://ip:3306/test?useUnicode=true&characterEncoding=utf8",
				"user", "password***"); 
		
		conn.createStatement().executeQuery("select * from test");
		
		PreparedStatement ps = conn.prepareStatement("insert into test(name,flag) values(?,?)"); 
		ps.setString(1, "hello"); 
		ps.setInt(2, 666); 
		ps.executeUpdate();
		
		conn.close();
	}

}
