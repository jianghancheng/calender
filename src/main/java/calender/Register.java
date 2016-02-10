package calender;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCrypt;




public class Register extends HttpServlet {

    public Register() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
       //get the paremeters from req
        String name = req.getParameter("name1");
        String originalPassword = req.getParameter("password1");
        String url = req.getParameter("url1");
        String zone = req.getParameter("zone1");
        System.out.print(name);
		System.out.print(originalPassword);
		System.out.print(url);
		System.out.print(zone);
		Connection connection = null;
		try {
			connection = HerokuDatabase.getConnection();
		} catch (URISyntaxException e1) {
			throw new Error(e1);
		} catch (SQLException e1) {
			throw new Error(e1);
		}



//		String sql = "INSERT INTO login(name,password,url,zone)" + " VALUES('"
//				+ name + "','" + password + "','" + url + "','" + zone + "')";
//

		String generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
        String insertTableSQL = "INSERT INTO login"
				+ "(name,password,url,zone) VALUES"
				+ "(?,?,?,?)";
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(insertTableSQL);
		} catch (SQLException e) {
			throw new Error(e);
		}
		
		  try {
			stmt.setString(1, name);
		} catch (SQLException e) {
			throw new Error(e);
		}
		 try {
			stmt.setString(2, generatedSecuredPasswordHash);
		} catch (SQLException e) {
			throw new Error(e);
		}
		  try {
			stmt.setString(3, url);
		} catch (SQLException e) {
			throw new Error(e);
		}
		  try {
			stmt.setString(4, zone);
		} catch (SQLException e) {
			throw new Error(e);
		}
		  System.out.println(stmt.toString());
		 try {
			stmt.execute();
		} catch (SQLException e) {
			throw new Error(e);
		}
		 try {
				connection.close();
			} catch (SQLException e) {
				throw new Error(e);
			}
		 resp.sendRedirect("login.html");
	}
}