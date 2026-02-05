package com.bookShop.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    // PostgreSQL-safe column names
    private static final String query =
        "INSERT INTO bookdata (bookname, bookedition, bookprice) VALUES (?, ?, ?)";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        String bookName = req.getParameter("bookName");
        String bookEdition = req.getParameter("bookEdition");
        String priceStr = req.getParameter("bookPrice");

        if (bookName == null || bookEdition == null || priceStr == null ||
            bookName.trim().isEmpty() || bookEdition.trim().isEmpty() || priceStr.trim().isEmpty()) {

            pw.println("<h3>All fields are required</h3>");
            pw.println("<a href='/'>HOME</a>");
            return;
        }

        float bookPrice = Float.parseFloat(priceStr);

        // Load PostgreSQL driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // DB connection
        try {
            String host = System.getenv("DB_HOST");
            String db   = System.getenv("DB_NAME");
            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASSWORD");
            String port = System.getenv("DB_PORT");

            String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

            Connection c = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = c.prepareStatement(query);

            ps.setString(1, bookName);
            ps.setString(2, bookEdition);
            ps.setFloat(3, bookPrice);

            int count = ps.executeUpdate();

            if (count == 1) {
                pw.println("<h3>Record registered successfully</h3>");
            } else {
                pw.println("<h3>Record not registered</h3>");
            }

            ps.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h3>Error registering record</h3>");
        }

        pw.println("<br><a href='/'>HOME</a>");
        pw.println("<br><a href='BookList'>Book List</a>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
