import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class hola4 extends HttpServlet implements Runnable {

  int cont = 0;
  int segs = 0;
  Date fechaInicio = new Date(); // Fecha inicio ejecuci¢n
  Thread contadorSegs;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);  // siempre
    contadorSegs = new Thread(this);
    contadorSegs.setPriority(Thread.MIN_PRIORITY);
    contadorSegs.start();   
  }

  public void run() {
    while (true) {
      try {
        contadorSegs.sleep(1000);
      } catch (InterruptedException ignored) { }
    segs ++;
    }
  }

  public void destroy() {
    contadorSegs.stop();
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    String nombre = req.getParameter("nombre");
    cont ++;
    out.println("<html><big>Hola Amigo "+ nombre + "</big><br>"+
                cont + " Accesos y " +
                segs + " segs desde su carga en " +
                fechaInicio + ".</html>");
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}
