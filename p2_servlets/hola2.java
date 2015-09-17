import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class hola2 extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String nombre = req.getParameter("nombre");
    String edad = req.getParameter("edad");

    int edadI = 0;
    try {
     edadI = Integer.parseInt(edad);
    } catch (NumberFormatException ignored) { }
    if (edad != null) out.println("<html><big>Hola Amigo "+ nombre +
                " de " + edad + " " + edadI + ".</big></html>");
    else out.println("<html>Amigo " + nombre + ", falta la edad.</html>");
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
