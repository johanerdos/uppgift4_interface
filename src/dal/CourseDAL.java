package dal;

import java.sql.*;

import databaseConnetion.DBConnection;
import model.Course;

public class CourseDAL {
	private Connection conn = null;
	private PreparedStatement ps = null;
	
	public CourseDAL() {
	
	}
	// Startar connection, kör preparedStatement sedan hämtar Resultset//
private ResultSet runExecuteQuery(String sqlString) throws SQLException {
		conn = DBConnection.getConnection();
		ps = conn.prepareStatement(sqlString);
		ResultSet rs = ps.executeQuery();
		return rs;
}
//Startar connection, kör preparedStatement sedan executeUpdate//
public void runExecuteUpdate(String sqlString) throws SQLException {
	conn = DBConnection.getConnection();
	ps = conn.prepareStatement(sqlString);
	ps.executeUpdate();
}

// Hitta en kurs och dess information
public Course findCourse(String courseID) throws SQLException {
	String sqlString = "select * from Course where courseID = '" + courseID + "';";
	ResultSet rs = runExecuteQuery(sqlString);
	if (rs.next()) {
		courseID = rs.getString(1);
		String courseName = rs.getString(2);
		int credits = rs.getInt(3);
		Course c = new Course(courseID, courseName, credits);
		ps.close();
		conn.close();
		return c;
		
	}
	return null;
}
}


