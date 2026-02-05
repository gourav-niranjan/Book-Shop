package com.bookShop.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BookList")
public class BookListServlet extends HttpServlet {

    // PostgreSQL-safe query (lowercase column names)
    private static final String query =
        "SELECT id, bookname, bookedition, bookprice FROM bookdata";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        // Load PostgreSQL driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Create DB connection
        try {
            String host = System.getenv("DB_HOST");
            String db   = System.getenv("DB_NAME");
            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASSWORD");
            String port = System.getenv("DB_PORT");

            String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

            Connection c = DriverManager.getConnection(url, user, pass);

            PreparedStatement ps = c.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            pw.println("<table border='1' align='center'>");
            pw.println("<tr>");
            pw.println("<th>Book ID</th>");
            pw.println("<th>Book Name</th>");
            pw.println("<th>Book Edition</th>");
            pw.println("<th>Book Price</th>");
            pw.println("<th>Edit</th>");
            pw.println("<th>Delete</th>");
            pw.println("</tr>");

            while (rs.next()) {
                pw.println("<tr>");
                pw.println("<td>" + rs.getInt("id") + "</td>");
                pw.println("<td>" + rs.getString("bookname") + "</td>");
                pw.println("<td>" + rs.getString("bookedition") + "</td>");
                pw.println("<td>" + rs.getFloat("bookprice") + "</td>");
                pw.println("<td><a href='editScreen?id=" + rs.getInt("id") + "'>Edit</a></td>");
                pw.println("<td><a href='deleteurl?id=" + rs.getInt("id") + "'>Delete</a></td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h3>Error loading book list</h3>");
        }

        pw.println("<br><a href='/'>HOME</a>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
