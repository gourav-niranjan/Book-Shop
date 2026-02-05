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

@WebServlet("/editScreen")
public class EditScreenServlet extends HttpServlet {
	private static final String query = "SELECT BookName, BookEdition, BookPrice FROM bookdata WHERE ID = ?";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//get Printwriter
		PrintWriter pw = resp.getWriter();
		
		// get the Id
		int id = Integer.parseInt(req.getParameter("id"));
		//get ContentType
		resp.setContentType("text/html");
		
		
		
		//Load JDBC 
		try {
			Class.forName("org.postgresql.Driver");

		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//Generate a connection
		try {
			String host = System.getenv("DB_HOST");
			String db   = System.getenv("DB_NAME");
			String user = System.getenv("DB_USER");
			String pass = System.getenv("DB_PASSWORD");
			String port = System.getenv("DB_PORT");

			String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

			Connection c = DriverManager.getConnection(url, user, pass);

			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
			    pw.println("<form action='editurl' method='post'>");
			    pw.println("<input type='hidden' name='id' value='" + id + "'>");

			    pw.println("<table align='center'>");

			    pw.println("<tr><td>Book Name</td>");
			    pw.println("<td><input type='text' name='BookName' value='" + rs.getString(1) + "'></td></tr>");

			    pw.println("<tr><td>Book Edition</td>");
			    pw.println("<td><input type='text' name='BookEdition' value='" + rs.getString(2) + "'></td></tr>");

			    pw.println("<tr><td>Book Price</td>");
			    pw.println("<td><input type='text' name='BookPrice' value='" + rs.getFloat(3) + "'></td></tr>");

			    pw.println("<tr><td colspan='2'>");
			    pw.println("<input type='submit' value='Edit'>");
			    pw.println("<input type='reset' value='Cancel'>");
			    pw.println("</td></tr>");

			    pw.println("</table>");
			    pw.println("</form>");
			}


			
			
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
