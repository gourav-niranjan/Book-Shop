package com.bookShop.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteurl")
public class DeleteServlet extends HttpServlet{
	private static final String query = "DELETE FROM bookdata WHERE ID = ?";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");
		//get Printwriter
		PrintWriter pw = resp.getWriter();
		
		String idStr = req.getParameter("id");
	   
	    if (idStr == null || idStr.trim().isEmpty()) {
	        pw.println("<h3>Invalid input. ID is required.</h3>");
	        return;
	    }

	    int id = Integer.parseInt(idStr);

		
		
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
			int count = ps.executeUpdate();
			if(count==1) {
				pw.println("<h2>Record is Deleted successfully </h2>");
				
			}else {
				pw.println("<h2>Record is not Deleted successfully </h2>");

			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		pw.println("<a href= \"index.html\">HOME</a>");
		pw.println("<br>");
		pw.println("<a href= \"BookList\">Book List</a>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
