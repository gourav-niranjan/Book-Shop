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

@WebServlet("/editScreen")
public class EditScreenServlet extends HttpServlet {

    // PostgreSQL-safe query
    private static final String query =
        "SELECT bookname, bookedition, bookprice FROM bookdata WHERE id = ?";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            pw.println("<h3>Invalid book ID</h3>");
            return;
        }

        int id = Integer.parseInt(idStr);

        // Load PostgreSQL driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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
                pw.println("<td><input type='text' name='BookName' value='" +
                           rs.getString("bookname") + "'></td></tr>");

                pw.println("<tr><td>Book Edition</td>");
                pw.println("<td><input type='text' name='BookEdition' value='" +
                           rs.getString("bookedition") + "'></td></tr>");

                pw.println("<tr><td>Book Price</td>");
                pw.println("<td><input type='text' name='BookPrice' value='" +
                           rs.getFloat("bookprice") + "'></td></tr>");

                pw.println("<tr><td colspan='2'>");
                pw.println("<input type='submit' value='Edit'>");
                pw.println("<input type='reset' value='Cancel'>");
                pw.println("</td></tr>");

                pw.println("</table>");
                pw.println("</form>");
            } else {
                pw.println("<h3>No book found for given ID</h3>");
            }

            rs.close();
            ps.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h3>Error loading edit screen</h3>");
        }

        pw.println("<br><a href='/'>HOME</a>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
