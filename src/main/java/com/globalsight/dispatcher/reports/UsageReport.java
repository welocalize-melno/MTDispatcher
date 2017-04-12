package com.globalsight.dispatcher.reports;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UsageReport {
	
	static String filePath = "D:\\Reports\\";
	static String driver = "jdbc:mysql://192.168.1.118:3306/johndeere";
	static String username = "root";
	static String password = "Mfiospi#0214";
	static String todayDate = getCurrentDate();
	static String delimiter = "|";
	
	public static String getCurrentDate(){
		  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  Date date = new Date();
		  return dateFormat.format(date);
	}
	
	public static void generateSummaryReport() throws Exception{
		String filename = filePath+"summaryReport_"+todayDate+".csv";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename, false);
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(driver, username, password);
			stmt = con
					.prepareStatement("select CONCAT(MONTH(DATE),'/',YEAR(DATE)),USER,SUM(CHARS) CHARCOUNT from translate  group by left(DATE,7) DESC,ACCOUNT_ID");
			rs = stmt.executeQuery();
			boolean dataFlag = true;
			while (rs.next()) {
				if (dataFlag) {
					fw.append("MONTH/YEAR");
					fw.append(delimiter);
					fw.append("USER");
					fw.append(delimiter);
					fw.append("CHARS");
					fw.append(delimiter);
					fw.append('\n');
				}
				fw.append(rs.getString(1));
				fw.append(delimiter);
				fw.append(rs.getString(2));
				fw.append(delimiter);
				fw.append(rs.getString(3));
				fw.append('\n');

				dataFlag = false;
			}
			if (dataFlag) {
				fw.append("No data found");
			} 
			System.out.println("Summary report csv File generated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			throw new Exception("Issue while generate summary report");
		} finally {
			try {
				fw.flush();
				if (fw != null)
					fw.close();
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println(e);
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
	public static void generateDetailReport() throws Exception{
		String filename = filePath+"detailReport_"+todayDate+".csv";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename, false);
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(driver, username, password);
			stmt = con
					.prepareStatement("select DATE,USER,DOMAIN,MTCATEGORY,SOURCE_L,TARGET_L,SOURCE,TARGET,CHARS from translate  order by DATE desc,ACCOUNT_ID");
			rs = stmt.executeQuery();
			boolean dataFlag = true;
			while (rs.next()) {
				if (dataFlag) {
					fw.append("DATETIME");
					fw.append(delimiter);
					fw.append("USER");
					fw.append(delimiter);
					fw.append("DOMAIN");
					fw.append(delimiter);
					fw.append("MTCATEGORY");
					fw.append(delimiter);
					fw.append("SOURCE_L");
					fw.append(delimiter);
					fw.append("TARGET_L");
					fw.append(delimiter);
					fw.append("SOURCE");
					fw.append(delimiter);
					fw.append("TARGET");
					fw.append(delimiter);
					fw.append("CHARS");
					fw.append('\n');
				}
				fw.append(rs.getString(1));
				fw.append(delimiter);
				fw.append(rs.getString(2));
				fw.append(delimiter);
				fw.append(rs.getString(3));
				fw.append(delimiter);
				fw.append(rs.getString(4));
				fw.append(delimiter);
				fw.append(rs.getString(5));
				fw.append(delimiter);
				fw.append(rs.getString(6));
				fw.append(delimiter);
				fw.append(rs.getString(7).replace("\n", ""));
				fw.append(delimiter);
				fw.append(rs.getString(8).replace("\n", ""));
				fw.append(delimiter);
				fw.append(rs.getString(9));
				fw.append('\n');

				dataFlag = false;
			}
			if (dataFlag) {
				fw.append("No data found");
			}  
			System.out.println("Detailed report csv File generated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		} finally {
			try {
				fw.flush();
				if (fw != null)
					fw.close();
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println(e);
				throw new Exception("Issue while generate detailed report");
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
	
	public static void main(String[] args) {
		try{
			System.out.println("Report generation start..............");
			System.out.println("\n");
			generateDetailReport();
			System.out.println("\n");
			generateSummaryReport();
			System.out.println("\n");
			System.out.println("Report generation completed..............");
			System.out.println("\n");
			System.out.println("Reports are saved in "+filePath);
			System.out.println("\n");
						
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
