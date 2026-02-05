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

@WebServlet("/editurl")
public class EditServlet extends HttpServlet {

    // PostgreSQL-safe query (lowercase column names)
    private static final String query =
        "UPDATE bookdata SET bookname = ?, bookedition = ?, bookprice = ? WHERE id = ?";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        String idStr = req.getParameter("id");
        String priceStr = req.getParameter("BookPrice");

        if (idStr == null || idStr.trim().isEmpty()
                || priceStr == null || priceStr.trim().isEmpty()) {
            pw.println("<h3>Invalid input. ID and Price are required.</h3>");
            return;
        }

        int id = Integer.parseInt(idStr);
        float bookPrice = Float.parseFloat(priceStr);

        String bookName = req.getParameter("BookName");
        String bookEdition = req.getParameter("BookEdition");

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
            ps.setInt(4, id);

            int count = ps.executeUpdate();

            if (count == 1) {
                pw.println("<h2>Record successfully edited</h2>");
            } else {
                pw.println("<h2>No record found to update</h2>");
            }

            ps.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h3>Error updating record</h3>");
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
