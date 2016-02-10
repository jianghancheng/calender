package calender;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import javax.servlet.http.Cookie;import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import net.fortuna.ical4j.data.ParserException;

import org.xml.sax.SAXException;

import fi.jyu.ties532.advanced.locator.LocationNotFoundException;

/**
 * Root resource (exposed at "myresource" path)
 */

@SuppressWarnings("serial")
public class MyResource  extends HttpServlet {
	   public MyResource() {
			super();
		} 
	   protected void doGet(HttpServletRequest req, HttpServletResponse resp)  throws IOException{
	       
			//get the paremeters from req
		      double latitude = Double.parseDouble(req.getParameter("lat"));
		      double longitude = Double.parseDouble(req.getParameter("lng"));
		      Info i = null;
		      String url = null;
		      HttpSession session=req.getSession();
		      String username= (String)session.getAttribute("user");
		      System.out.println("username  "+username);
		      Connection connection = null;
				try {
					connection = HerokuDatabase.getConnection();
				} catch (URISyntaxException e1) {
					throw new Error(e1);
				} catch (SQLException e1) {
					throw new Error(e1);
				}


             String selectSQL ="SELECT url,zone FROM login WHERE name=?";
             PreparedStatement preparedStatement;
			try {
				preparedStatement = connection.prepareStatement(selectSQL);
			} catch (SQLException e1) {
				throw new Error(e1);
			}
             try {
				preparedStatement.setString(1, username);
			} catch (SQLException e1) {
				throw new Error(e1);
			}
             ResultSet rs;
			try {
				rs = preparedStatement.executeQuery();
			} catch (SQLException e1) {
				throw new Error(e1);
			}	
			     try {
					while (rs.next()) {
					url= rs.getString("url");
					System.out.println("url"+url);
					 }
				} catch (SQLException e) {
					throw new Error(e);
				} 
			i = (Info) ClassLocation.getLocation(latitude, longitude,url);
				
				resp.setCharacterEncoding("UTF-8"); 
				
			      // System.out.println("username------"+username);
			   	
       	   resp.getWriter().println( "You have " + i.left + " mins Left\n"
							+ "From: " + i.begAdress + "\nTo: " + i.location);
       	 
       	         
	   }
	}
