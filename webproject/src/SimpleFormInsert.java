
/**
 * @file SimpleFormInsert.java
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormInsert")
public class SimpleFormInsert extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SimpleFormInsert() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String taskName = request.getParameter("TASKNAME");
      String details = request.getParameter("DETAILS");
      String dueDate = request.getParameter("DUEDATE");
      String status = request.getParameter("STATUS");

      Connection connection = null;
      
      
      
      
      String insertSql = " INSERT INTO TaskList (id, TASKNAME, DETAILS, DUEDATE, STATUS) values (default, ?, ?, ?, ?)";
      String insertSqlC = " INSERT INTO completedTasks (id, TASKNAME, DETAILS, DUEDATE) values (default, ?, ?, ?)";

      try {
         DBConnection.getDBConnection();
         connection = DBConnection.connection;
         if (status.compareTo("Complete") == 0) {
        	 	PreparedStatement preparedStmt = connection.prepareStatement(insertSqlC);
        	 	preparedStmt.setString(1, taskName);
        	 	preparedStmt.setString(2, details);
        	 	preparedStmt.setString(3, dueDate);
        	 	preparedStmt.execute();
         }
         else {
         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
         preparedStmt.setString(1, taskName);
         preparedStmt.setString(2, details);
         preparedStmt.setString(3, dueDate);
         preparedStmt.setString(4, status);
         preparedStmt.execute();
         }
         connection.close();
         
      } catch (Exception e) {
         e.printStackTrace();
      }

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      if (status.compareTo("Complete") != 0) {
      
      String title = "Insert New Task";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h2 align=\"center\">" + title + "</h2>\n" + //
            "<ul>\n" + //

            "  <li><b>Task</b>: " + taskName + "\n" + //
            "  <li><b>Details</b>: " + details + "\n" + //
            "  <li><b>Due Date</b>: " + dueDate + "\n" + //
            "  <li><b>Status</b>: " + status + "\n" + //
    		  	"</ul>"+ 
    		  	
			"<form action=\"SimpleFormSearch\" method=\"POST\">" +
  	
				"<input type= \"hidden\" name=\"keyword\"> <br />" +
		
				"<input type=\"submit\" value=\"See Full Task List\" />" +
			"</form>" +
    		  "\n");  
      }
      else {
    	  	String title = "Insert New Task";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
          out.println(docType + //
                "<html>\n" + //
                "<head><title>" + title + "</title></head>\n" + //
                "<body bgcolor=\"#f0f0f0\">\n" + //
                "<h2 align=\"center\">" + title + "</h2>\n" + //
                "<ul>\n" + //

                "  <li><b>Task</b>: " + taskName + "\n" + //
                "  <li><b>Details</b>: " + details + "\n" + //
                "  <li><b>Due Date</b>: " + dueDate + "\n" + //

                "</ul>\n");
      }
      
      out.println("<a href=/webproject/simpleFormSearch.html>Search Task List</a> <br>");
      out.println("<a href=/webproject/simpleFormInsert.html>Insert New Task</a> <br>");
      out.println("</body></html>");
      
   	  }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
