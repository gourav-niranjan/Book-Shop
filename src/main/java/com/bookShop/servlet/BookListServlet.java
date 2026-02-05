package com.bookShop.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BookList")
public class BookListServlet extends HttpServlet {

	private static final String query = "SELECT ID, BookName, BookEdition, BookPrice FROM bookdata";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//get Printwriter
		PrintWriter pw = resp.getWriter();
		
		//get ContentType
		resp.setContentType("text/html");
		
		
		
		//Load JDBC 
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//Generate a connection
		try {
			Connection c = DriverManager.getConnection("jdbc:mysql:///book", "root", "pass123");
			PreparedStatement ps = c.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			pw.println("<table border='1' align='center'>");
			pw.println("<tr>");
			pw.println("<th> Book ID </th>");
			pw.println("<th> Book Name </th>");
			pw.println("<th> Book Edition </th>");
   			pw.println("<th> Book Price </th>");
   			pw.println("<th> Edit </th>");
   			pw.println("<th> Delete </th>");

   			pw.println("</tr>");
   			while(rs.next()) {
   				pw.println("<tr>");
   				pw.println("<td>" + rs.getInt(1) + "</td>");
   				pw.println("<td>" + rs.getString(2) + "</td>");
   				pw.println("<td>" + rs.getString(3) + "</td>");
   				pw.println("<td>" + rs.getFloat(4) + "</td>");
   				pw.println("<td> <a href= 'editScreen?id=" + rs.getInt(1) + "'>Edit</a> ");
   				pw.println("<td> <a href= 'deleteurl?id=" + rs.getInt(1) + "'>Delete</a> ");

   				pw.println("</tr>");
   				 
   			}
			pw.println("</table>");	
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		pw.println("<a href= \"index.html\">HOME</a>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
