package calender;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.springframework.security.crypto.bcrypt.BCrypt;


public class Login extends HttpServlet {

    public Login() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
       //get the paremeters from req
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        System.out.print(name);
		System.out.print(password);
		Connection connection;
		try {
			connection = HerokuDatabase.getConnection();
		} catch (URISyntaxException e1) {
			throw new Error(e1);
		} catch (SQLException e1) {
			throw new Error(e1);
		}


		   String selectSQL ="SELECT name,password FROM login WHERE name=?";
           PreparedStatement preparedStatement;
			try {
				preparedStatement = connection.prepareStatement(selectSQL);
			} catch (SQLException e1) {
				throw new Error(e1);
			}
           try {
				preparedStatement.setString(1, name);
			} catch (SQLException e1) {
				throw new Error(e1);
			}
           ResultSet rs;
			try {
				rs = preparedStatement.executeQuery();
			} catch (SQLException e1) {
				throw new Error(e1);
			}	

		Map<String, String> data = new HashMap<String, String>();

		try {
			while (rs.next()) {
				String username = rs.getString("name");
				String pass = rs.getString("password");
				data.put(username, pass);
			}
		} catch (SQLException e) {
			throw new Error(e);
		}
		//boolean matched = BCrypt.checkpw(originalPassword, generatedSecuredPasswordHash);
		
		boolean matched = BCrypt.checkpw(password, data.get(name));
		
		if (matched) {
			try {
			 HttpSession session = req.getSession();
                if (session != null) {
					
					session.setAttribute("user", name);
					  session.setMaxInactiveInterval(60);
				}
			} catch (Throwable t) {
				throw new Error(t);
			}
			 resp.sendRedirect("location.html");
             try {
				connection.close();
			} catch (SQLException e) {
				throw new Error(e);
			}
		}

		else {
			resp.sendRedirect("login.html");

		}
	}
		
}