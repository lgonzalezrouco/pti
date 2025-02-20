package mypackage;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray; 
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class CarRentalNew extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unchecked")
  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String rating = req.getParameter("co2_rating");
    String engine = req.getParameter("sub_model_vehicle");
    String days = req.getParameter("dies_lloguer");
    String units = req.getParameter("num_vehicles");
    String disc = req.getParameter("descompte");

    JSONObject rental = new JSONObject();
    rental.put("rating", rating);
    rental.put("engine", engine);
    rental.put("days", days);
    rental.put("units", units);
    rental.put("discount", disc);
    

    out.println("<html><big>Rating: "+ rating + "</big><br>"+
                "<big>Engine: "+ engine + "</big><br>" +
                "<big>Number of days: : "+ days + "</big><br>" +
                "<big>Number of units: "+ units + "</big><br>" +
                "<big>Discount : "+ disc + "</big><br>" +
                " </html>");
    out.close();

    File file = new File("rental.json");
    JSONArray rentals = new JSONArray();

    if (file.exists() && file.length() > 0) {
            try (FileReader reader = new FileReader(file)) {
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(reader);
                rentals = (JSONArray) obj.get("rentals");
                if (rentals == null) rentals = new JSONArray();
            } catch (ParseException e) {
                e.printStackTrace();
                rentals = new JSONArray();
            }
        }

    rentals.add(rental);
    JSONObject newObject = new JSONObject();
    newObject.put("rentals", rentals);

    try (FileWriter fileWriter = new FileWriter("rental.json")) {
            fileWriter.write(newObject.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
  }
  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
