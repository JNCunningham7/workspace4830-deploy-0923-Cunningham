import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SimpleFormSearch")
public class SimpleFormSearch extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public SimpleFormSearch() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String keyword = request.getParameter("keyword");
      search(keyword, response);
   }

   void search(String keyword, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Task List";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      PreparedStatement preparedStatementC = null;
      try {
         DBConnection.getDBConnection();
         connection = DBConnection.connection;

         if (keyword.isEmpty()) {
            String selectSQL = "SELECT * FROM TaskList";
            preparedStatement = connection.prepareStatement(selectSQL);
            String selectSQLC = "SELECT * FROM completedTasks";
            preparedStatementC = connection.prepareStatement(selectSQLC);
         } else {
            String selectSQL = "SELECT * FROM TaskList WHERE TASKNAME LIKE ?";
            String selectSQLC = "SELECT * FROM completedTasks WHERE TASKNAME LIKE ?";
            String theTaskName = keyword + "%";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, theTaskName);
            preparedStatementC = connection.prepareStatement(selectSQLC);
            preparedStatementC.setString(1, theTaskName);
         }
         ResultSet rs = preparedStatement.executeQuery();
         ResultSet rsC = preparedStatementC.executeQuery();

         out.println("<b>In Progress:</b> <br><br>");
         
         while (rs.next()) {
            int id = rs.getInt("id");
            String taskName = rs.getString("TASKNAME").trim();
            String details = rs.getString("DETAILS").trim();
            String dueDate = rs.getString("DUEDATE").trim();
            String status = rs.getString("STATUS").trim();

            if (keyword.isEmpty() || taskName.contains(keyword)) {
               out.println("<b>ID: </b>" + id + ", ");
               out.println("<b>Task: </b>" + taskName + ", ");
               out.println("<b>Details: </b>" + details + ", ");
               out.println("<b>Due Date: </b>" + dueDate + ", ");
               out.println("<b>Status: </b>" + status + "<br>");
            }
         }
         
         out.println("<br><b>Completed:</b> <br><br>");
         
         while (rsC.next()) {
             int id = rsC.getInt("id");
             String taskName = rsC.getString("TASKNAME").trim();
             String details = rsC.getString("DETAILS").trim();
             String dueDate = rsC.getString("DUEDATE").trim();

             if (keyword.isEmpty() || taskName.contains(keyword)) {
                out.println("<b>ID: </b>" + id + ", ");
                out.println("<b>Task: </b>" + taskName + ", ");
                out.println("<b>Details: </b>" + details + ", ");
                out.println("<b>Due Date: </b>" + dueDate + "<br>");
             }
          }
         out.println("<a href=/webproject/simpleFormSearch.html>Search Task List</a> <br>");
         out.println("<a href=/webproject/simpleFormInsert.html>Insert New Task</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
