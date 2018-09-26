package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import databaseConnetion.DBConnection;
import model.Course;

public class CourseDAL {
	private Connection conn = null;
	private PreparedStatement ps = null;
	
	public CourseDAL() {
	
	}
	// Startar connection, k�r preparedStatement sedan h�mtar Resultset//
private ResultSet runExecuteQuery(String sqlString) throws SQLException {
		conn = DBConnection.getConnection();
		ps = conn.prepareStatement(sqlString);
		ResultSet rs = ps.executeQuery();
		return rs;
}
//Startar connection, k�r preparedStatement sedan executeUpdate//
public void runExecuteUpdate(String sqlString) throws SQLException {
	conn = DBConnection.getConnection();
	ps = conn.prepareStatement(sqlString);
	ps.executeUpdate();
}

// Hitta en kurs och dess information
public Course findCourse(String courseID) throws SQLException {
	String sqlString = "SELECT * FROM Course WHERE courseID = '" + courseID + "';";
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

//l�gg till ny kurs
public void addCourse(String courseID, String courseName, int credit) throws SQLException{
	String sqlString = "INSERT INTO Course VALUES { '" + courseID + "', '" + courseName + "', " + credit + ");";
	runExecuteUpdate(sqlString);
	ps.close();
	conn.close();
}
// Ta bort kurs
public void deleteCourse(String courseID) throws SQLException {
	String sqlString = "DELETE FROM Course WHERE courseID ='" + courseID + "';";
	runExecuteUpdate(sqlString);
	ps.close();
	conn.close();
}
//H�mtar alla kurser
public ArrayList<Course> getAllCourses() throws SQLException {
	ArrayList<Course> allCourses = new ArrayList<Course>();
	String sqlString = "SELECT * FROM Course";
	
	ResultSet rs = runExecuteQuery(sqlString);
	while (rs.next()) {
		String courseID = rs.getString(1);
		String courseName = rs.getString(2);
		int credit = rs.getInt(3);
		Course c = new Course(courseID, courseName, credit);
		allCourses.add(c);
	}
	ps.close();
	conn.close();
	return allCourses;
}
//H�mtar betygsf�rdelningen p� en viss urs
public HashMap<String, String> getGradePercentage(String courseID) throws SQLException {
	HashMap<String, String> map = new HashMap<String, String>();
	
	String sqlString = "select grade, (count(grade)* 100 / (select count(*) from Studied where courseID ='" + courseID
			+ "')) as 'percentage' from Studied where courseID = '" + courseID + "' group by grade";

ResultSet rs = runExecuteQuery(sqlString);
while (rs.next()) {
	String grade = rs.getString(1);
	String percent = rs.getString(2);
	map.put(grade, percent);
}
ps.close();
conn.close();
return map;
}
//H�mtar top 10 h�gsta genomstr�mning

public HashMap<String, String> getFlow() throws SQLException {
	HashMap<String, String> map = new HashMap<String, String>();
	
	String sqlString = "select top 10 upper(courseID) as 'CourseID', (sum(case when grade != 'U' then 1 else 0 end)*100) / count (courseID) as 'Percent Passed' "
			+ "from studied " + "group by courseID " + "order by 'Percent Passed' desc";

	ResultSet rs = runExecuteQuery(sqlString);
	while (rs.next()) {
		String courseID = rs.getString(1);
		String percent = rs.getString(2);
		map.put(courseID, percent);

	}
	return map;
}

}




