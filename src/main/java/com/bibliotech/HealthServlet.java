package com.bibliotech;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.bibliotech.dao.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/health")
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");

        PrintWriter out = response.getWriter();

        try (Connection connection = DatabaseConnection.get();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT version()")) {

            if (resultSet.next()) {
                out.println("OK · " + resultSet.getString(1));
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("KO");
            e.printStackTrace();
        }
    }
}