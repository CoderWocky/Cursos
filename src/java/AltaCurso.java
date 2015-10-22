/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 *
 * @author alumno
 */
@WebServlet(urlPatterns = {"/AltaCurso"})
public class AltaCurso extends HttpServlet {

  private void enviarRespuesta(HttpServletResponse response, String mensaje) 
      throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter(); 

    out.println("<html>");
    out.println("<head>");
    out.println("<title>Resultado del Alta</title>");
    out.println("<meta charset='UTF-8'>");
    out.println("</head>");
    out.println("<body>");
    out.println(String.format("<p>%s</p>", mensaje));
    out.println("</body>");
    out.println("</html>");
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws IOException,ServletException {
    
    String nombre = request.getParameter("nombre");
    String apellido1 = request.getParameter("apellido1");
    String apellido2 = request.getParameter("apellido2");
    String domicilio = request.getParameter("domicilio");
    String ciudad = request.getParameter("ciudad");
    String sexo = request.getParameter("sexo");
    String[] listaSistemas = request.getParameterValues("sistemas");
    String sistemas = "";
    if (listaSistemas != null) {
      for (int i = 0; i < listaSistemas.length; i++) {
        if (i != 0) sistemas += ", ";
        sistemas += (listaSistemas[i]);
      }
    }
    String comentario = request.getParameter("comentario");
    
    try {
      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      Connection conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", 
          "system", "javaoracle");

      CallableStatement proc = conexion.prepareCall("{call AltaCurso(?, ?, ?, ?, ?, ?, ?, ?)}");
      proc.setString(1, nombre);
      proc.setString(2, apellido1);
      proc.setString(3, apellido2);
      proc.setString(4, domicilio); 
      proc.setString(5, ciudad);
      proc.setString(6, sexo);
      proc.setString(7, sistemas);
      proc.setString(8, comentario);
      proc.executeUpdate();
      
      enviarRespuesta(response, "Se ha insertado el registro");
      
    } catch (SQLException ex) {
      enviarRespuesta(response, "Se ha producido un error al insertar el registro");
    }
    
  }
}
