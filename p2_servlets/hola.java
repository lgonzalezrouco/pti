import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class hola extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    out.println("<html><big>Hola Amigos !</big></html>");
  }
}
