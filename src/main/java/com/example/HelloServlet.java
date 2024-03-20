package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

@WebServlet("/cars/*")
public class HelloServlet extends HttpServlet {
    private final CarService carService = new CarService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Car> cars = carService.getAllCars();
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), cars);
        } else {
            String[] parts = pathInfo.split("/");
            if (parts.length == 2) {
                int id = Integer.parseInt(parts[1]);
                Car car = carService.getById(id);
                if (car != null) {
                    resp.setContentType("application/json");
                    mapper.writeValue(resp.getWriter(), car);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (BufferedReader reader = req.getReader()) {
            String json = reader.readLine();
            Car car = mapper.readValue(json, Car.class);
            carService.save(car);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            String[] parts = pathInfo.split("/");
            if (parts.length == 2) {
                int id = Integer.parseInt(parts[1]);
                Car car = carService.getById(id);
                if (car != null) {
                    carService.deleteById(id);
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    public void processRequest(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            reader.readLine();

            String response = "<html><body><h1>Hello, World!</h1></body></html>";

            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: text/html");
            writer.println("Content-Length: " + response.length());
            writer.println();
            writer.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
