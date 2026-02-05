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

@WebServlet("/deleteurl")
public class DeleteServlet extends HttpServlet {

    // PostgreSQL-safe column name
    private static final String query =
        "DELETE FROM bookdata WHERE id = ?";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        String idStr = req.getParameter("id");

        if (idStr == null || idStr.trim().isEmpty()) {
            pw.println("<h3>Invalid input. ID is required.</h3>");
            return;
        }

        int id = Integer.parseInt(idStr);

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

            ps.setInt(1, id);

            int count = ps.executeUpdate();

            if (count == 1) {
                pw.println("<h2>Record deleted successfully</h2>");
            } else {
                pw.println("<h2>No record found with given ID</h2>");
            }

            ps.close();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h3>Error deleting record</h3>");
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
