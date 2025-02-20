package mypackage;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class CarRentalList extends HttpServlet {
  private static final long serialVersionUID = 1L;


  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String name = req.getParameter("userid");
    String password = req.getParameter("password");

    if (name.equals("admin") && password.equals("admin")) {
      File file = new File("rental.json");
      JSONArray rentals = new JSONArray();

      if (file.exists() && file.length() > 0) {
              try (FileReader reader = new FileReader(file)) {
                  JSONParser parser = new JSONParser();
                  JSONObject obj = (JSONObject) parser.parse(reader);
                  rentals = (JSONArray) obj.get("rentals");
                  if (rentals != null) {
                  for (Object o : rentals) {
                    JSONObject rental = (JSONObject) o;
                    out.println("<html><big>Rating: "+ rental.get("rating") + "</big><br>"+
                                "<big>Engine: "+ rental.get("engine") + "</big><br>" +
                                "<big>Number of days: "+ rental.get("days") + "</big><br>" +
                                "<big>Number of units: "+ rental.get("units") + "</big><br>" +
                                "<big>Discount : "+ rental.get("discount") + "</big><br>" +
                                " <br></html>");
                  }
                  }
              } catch (ParseException e) {
                  e.printStackTrace();
                  rentals = new JSONArray();
              }
          }
          else {
            out.println("<html><big>No rentals</big></html>");
          }
    } else {
      out.println("<html><big>Access denied</big></html>");
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
