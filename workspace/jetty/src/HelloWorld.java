import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javapns.Push;
import javapns.notification.Payload;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotifications;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class HelloWorld extends HttpServlet {

	String mobile;
	String Name;
	public static Connection conn;
	private static final String GOOGLE_SERVER_KEY = "AIzaSyDr4FrUBiF2hhbz34RCm3uqIDNpRK9JJw8";
	static final String MESSAGE_KEY = "message";

	public HelloWorld() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		//if gcm gives message as not registered then try to handle it somehow
		
		try{


			if (req.getPathInfo().contains("dbaccess")) {
				JSONObject jsonObject = new JSONObject();
				StringBuffer jb = new StringBuffer();
				String line = null;
				BufferedReader reader = req.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
				jsonObject = (JSONObject) new JSONParser().parse(jb.toString());
				Name = (String) jsonObject.get("sql");
				JSONObject jsonResponse = new JSONObject();

				if(conn == null||conn.isClosed())
					connectdb();

				String SQL = Name;
				PreparedStatement statement = conn.prepareStatement(SQL);
				ResultSet result = statement.executeQuery();

				JSONArray data = new JSONArray();
				data = convert(result);
				jsonResponse.put("response", data);

				resp.setStatus(200);
				resp.setContentType("application/json");
				PrintWriter out = resp.getWriter();
				out.print(jsonResponse);
				out.flush();

			}

			// Insert during registration
			if (req.getPathInfo().contains("insertPerson")) {
				JSONObject jsonObject = new JSONObject();

				StringBuffer jb = new StringBuffer();
				String line = null;

				BufferedReader reader = req.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);

				jsonObject = (JSONObject) new JSONParser().parse(jb.toString());



				mobile = (String) jsonObject.get("mobile_number");
				Name = (String) jsonObject.get("name");
				String Type = (String) jsonObject.get("type");
				if(Type.equalsIgnoreCase("ios")||Type.equalsIgnoreCase("android"))
				{
					
				try {

					if(conn == null||conn.isClosed())
						connectdb();

					String SQL = "INSERT INTO POKER (mobile_number,name,device_type,push_id) VALUES (?,?,?,?)";
					PreparedStatement statement = conn.prepareStatement(SQL);
					statement.setString(1, mobile);
					statement.setString(2, Name);
					statement.setString(3, Type);
					statement.setString(4, "1234");
					statement.executeUpdate();


					resp.setStatus(201);
				} catch (Exception e) {
					if (e.getMessage().contains("duplicate")) {
						resp.setStatus(201);
					}
					//e.printStackTrace();
				}
				}
				else
				{
					resp.getWriter().write("set proper type");
					resp.setStatus(200);
				}

			}

			// Get the mobile numbers check and send the matching ones

			if (req.getPathInfo().contains("getThePokers"))

			{
				JSONObject jsonObject = new JSONObject();
				JSONObject jsonResponse = new JSONObject();
				ResultSet result = null;
				ResultSet result_countsent = null;
				ResultSet result_countreceive = null;
				PrintWriter out = resp.getWriter();
				JSONArray finaldata = new JSONArray();
				JSONArray temporary = new JSONArray();
				HashMap<String, Integer> count_sent = new HashMap<String, Integer>();
				HashMap<String, Integer> count_receive = new HashMap<String, Integer>();

				StringBuffer jb = new StringBuffer();
				String line = null;
					BufferedReader reader = req.getReader();
					while ((line = reader.readLine()) != null)
						jb.append(line);

				
					jsonObject = (JSONObject) new JSONParser().parse(jb.toString());
					org.json.simple.JSONArray mob = (org.json.simple.JSONArray) jsonObject
							.get("mobile_number");

					int size = mob.size();
					ArrayList<String> array = new ArrayList<String>();

					for (int i = 0; i < size; i++) {
						String x = (String) mob.get(i);

						if(conn == null||conn.isClosed())
							connectdb();

							String SQL = "SELECT MOBILE_NUMBER FROM POKER WHERE MOBILE_NUMBER = ?";
							PreparedStatement statement = conn
									.prepareStatement(SQL);

							statement.setString(1, x);

							result = statement.executeQuery();
						

						if (result.next()) {
							String r = result.getString("mobile_number");
							array.add(r);

						}

					}
					String mobilenumber = req.getHeader("mobilenumber");

					for (int d = 0; d < array.size(); d++) {

						if(conn == null||conn.isClosed())
							connectdb();

							String SQL4 = "SELECT pokee,count FROM POKED WHERE poker = ? AND pokee=?";
							PreparedStatement statement4 = conn
									.prepareStatement(SQL4);
							statement4.setString(1, mobilenumber);
							statement4.setString(2, array.get(d));
							result_countsent = statement4.executeQuery();

						

						if (result_countsent.next()) {
							count_sent.put(result_countsent.getString("pokee"),
									result_countsent.getInt("count"));

						}
					}

					for (int e = 0; e < array.size(); e++) {

						if(conn == null||conn.isClosed())
							connectdb();

							String SQL5 = "SELECT poker,count FROM POKED WHERE poker = ? AND pokee=?";
							PreparedStatement statement5 = conn
									.prepareStatement(SQL5);
							statement5.setString(1, array.get(e));
							statement5.setString(2, mobilenumber);
							result_countreceive = statement5.executeQuery();

						

						if (result_countreceive.next()) {
							count_receive.put(
									result_countreceive.getString("poker"),
									result_countreceive.getInt("count"));

						}
					}

					System.out.println(count_sent);
					System.out.println(count_receive);

					for (int f = 0; f < array.size(); f++) {
						JSONObject x = new JSONObject();
						x.put("mobile", array.get(f));
						if (count_sent.containsKey(array.get(f))) {
							x.put("count_sent", count_sent.get(array.get(f)));

						} else {
							x.put("count_sent", 0);
						}
						if (count_receive.containsKey(array.get(f))) {
							x.put("count_receive", count_receive.get(array.get(f)));
						} else {
							x.put("count_receive", 0);
						}
						finaldata.put(x);

					}

					jsonResponse.put("count", finaldata);

					out.print(jsonResponse);
					resp.setStatus(201);
					resp.setContentType("application/json");

					out.flush();

				
			}
			
			//message
			if (req.getPathInfo().contains("sendMessage")) {
				String line;
				ResultSet result = null;
				String m1=null;
				String m2=null;
				String msg=null;
				Result gcm = null;
				JSONObject jsonObject = new JSONObject();
				StringBuffer jb = new StringBuffer();
				BufferedReader reader = req.getReader();
				while ((line = reader.readLine()) != null)
					jb.append(line);
			

			try {
				// Class.forName("com.mysql.jdbc.Driver");
				
				jsonObject = (JSONObject) new JSONParser().parse(jb.toString());
				
				m1 = (String) jsonObject.get("mobile_number1");
				m2 = (String) jsonObject.get("mobile_number2");
				msg = (String) jsonObject.get("message");
				
				if(conn == null||conn.isClosed())
					connectdb();
				
				String SQL3 = "SELECT push_id,device_type FROM poker WHERE mobile_number=?";
				PreparedStatement statement3 = conn.prepareStatement(SQL3);

				statement3.setString(1, m2);
				result = statement3.executeQuery();
				String reg = null;
				String type=null;
				if (result.next()) {

					reg = result.getString("push_id");
					type = result.getString("device_type");
				}

				// GCM RedgId of Android device to send push notification
				String regId = reg;
					String userMessage = "{\"messager\":\""+m1+"\",\"message\":\""+msg+"\"}";
					
					if(type.equalsIgnoreCase("android"))
					{
					Sender sender = new Sender(GOOGLE_SERVER_KEY);
					Message message = new Message.Builder()
					.delayWhileIdle(false)
					.addData(MESSAGE_KEY, userMessage).build();
					gcm = sender.send(message, regId, 1);
					req.setAttribute("pushStatus", gcm.toString());
					}
					else
					{
						//ios
						String json = "{\"aps\" : {\"content-available\" : 1},\"update\":" + userMessage +"}";
						PushNotificationPayload pl = new PushNotificationPayload(json);
						pl.addAlert("");
						/*
						//pl.addCustomDictionary("content-available", "1");
						pl.addCustomDictionary("poker", m1);
						pl.addCustomDictionary("sent", csent);
						pl.addCustomDictionary("recv", crecv);*/
						 PushedNotifications pn = Push.payload(pl, "iphone_dev.p12", "mobile", false, 1,regId);
					}
				
				
				}
				catch(Exception e)
				{
					resp.getWriter().write(e.getMessage());
					resp.setStatus(400);
				}
			}

			// Receiving Poke

			if (req.getPathInfo().contains("sendPoke")) {
				
				JSONObject jsonObject = new JSONObject();
				JSONObject jsonObject1 = new JSONObject();
				ResultSet result = null;
				ResultSet result2 = null;
				StringBuffer jb = new StringBuffer();
				String line = null;
				String m1=null;
				String m2=null;
				String pokes=null;
				String csent = null;
				String crecv = null;
					BufferedReader reader = req.getReader();
					while ((line = reader.readLine()) != null)
						jb.append(line);
				

				try {
					// Class.forName("com.mysql.jdbc.Driver");
					
					jsonObject1 = (JSONObject) new JSONParser().parse(jb.toString());
					org.json.simple.JSONArray poke = (org.json.simple.JSONArray) jsonObject1.get("poke");
					
					for(int i=0;i<poke.size();i++)
					{
						jsonObject = (JSONObject) poke.get(i);
					m1 = (String) jsonObject.get("mobile_number1");
					m2 = (String) jsonObject.get("mobile_number2");
					pokes = (String) jsonObject.get("pokes");
					if(conn == null||conn.isClosed())
						connectdb();
					
						String SQL = "SELECT COUNT FROM POKED WHERE POKER= ? AND POKEE=?";
						PreparedStatement statement = conn.prepareStatement(SQL);

						statement.setString(1, m1);
						statement.setString(2, m2);
						result = statement.executeQuery();

						if (result.next()) {
							Integer r = result.getInt("COUNT")+ Integer.parseInt(pokes);
							csent = r.toString();
							String SQL1 = "update poked set count=? WHERE POKER = ? AND POKEE=?";
							PreparedStatement statement1 = conn
									.prepareStatement(SQL1);

							statement1.setInt(1, (r));
							statement1.setString(2, m1);
							statement1.setString(3, m2);
							statement1.executeUpdate();

						} else {

							String SQL2 = "INSERT INTO POKED values(?,?,?) ";
							PreparedStatement statement2 = conn
									.prepareStatement(SQL2);
							statement2.setString(1, m1);
							statement2.setString(2, m2);
							statement2.setInt(3, Integer.parseInt(pokes));
							statement2.executeUpdate();
							csent=pokes;

						}
						
						SQL = "SELECT COUNT FROM POKED WHERE POKER= ? AND POKEE=?";
						statement = conn.prepareStatement(SQL);

						statement.setString(1, m2);
						statement.setString(2, m1);
						result = statement.executeQuery();

						if (result.next()) {
							Integer r = result.getInt("COUNT");
						crecv = r.toString();
						}
						else
						{
							crecv="0";
						}
					
						Result gcm = null;
						String type=null;

						String SQL3 = "SELECT push_id,device_type FROM poker WHERE mobile_number=?";
						PreparedStatement statement3 = conn.prepareStatement(SQL3);

						statement3.setString(1, m2);
						result2 = statement3.executeQuery();
						String reg = null;
						if (result2.next()) {

							reg = result2.getString("push_id");
							type = result2.getString("device_type");
						}

						// GCM RedgId of Android device to send push notification
						String regId = reg;

				

							String userMessage = "{\"poker\":\""+m1+"\",\"sent\":\""+csent+"\",\"recv\":\""+crecv+"\"}";
							
							if(type.equalsIgnoreCase("android"))
							{
							Sender sender = new Sender(GOOGLE_SERVER_KEY);
							Message message = new Message.Builder()
							.delayWhileIdle(false)
							.addData(MESSAGE_KEY, userMessage).build();
							gcm = sender.send(message, regId, 1);
							req.setAttribute("pushStatus", gcm.toString());
							}
							else
							{
								//ios
								String json = "{\"aps\" : {\"content-available\" : 1},\"update\":" + userMessage +"}";
								PushNotificationPayload pl = new PushNotificationPayload(json);
								pl.addAlert("");
								/*
								//pl.addCustomDictionary("content-available", "1");
								pl.addCustomDictionary("poker", m1);
								pl.addCustomDictionary("sent", csent);
								pl.addCustomDictionary("recv", crecv);*/
								 PushedNotifications pn = Push.payload(pl, "iphone_dev.p12", "mobile", false, 1,regId);
								 
								 System.out.println(pn.elementAt(0));
							}
						
						
					}
					
					

					

				}  catch (Exception e) {
					// TODO Auto-generated catch block
					resp.getWriter().write(e.getMessage());
					resp.setStatus(400);
				}

			}

			// Receive gcm registration ID

			if (req.getPathInfo().contains("sendRegId")) {
				JSONObject jsonObject = new JSONObject();
			

				StringBuffer jb = new StringBuffer();
				String line = null;
				try {
					BufferedReader reader = req.getReader();
					while ((line = reader.readLine()) != null)
						jb.append(line);
				} catch (Exception e) { /* report an error */
				}

				try {

					jsonObject = (JSONObject) new JSONParser().parse(jb.toString());

					String mob = (String) jsonObject.get("mobile_number");
					String regId = (String) jsonObject.get("reg");
					// Class.forName("com.mysql.jdbc.Driver");
					if(conn == null||conn.isClosed())
						connectdb();
					
						String SQL = "update poker set push_id = ? where mobile_number = ?";
						PreparedStatement statement = conn.prepareStatement(SQL);
						statement.setString(1, regId);
						statement.setString(2, mob);
						statement.executeUpdate();

					
				
					resp.setStatus(201);
				} catch (ParseException e) {
					// crash and burn
					throw new IOException("Error parsing JSON request string");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					resp.getWriter().write(e.getMessage());
					resp.setStatus(400);
				}

			}
		}catch (Exception e) { 
			resp.getWriter().write(e.getMessage());
			resp.setStatus(400);
		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		JSONObject jsonResponse = new JSONObject();

		try {
			// Class.forName("com.mysql.jdbc.Driver");

			
			if(conn == null||conn.isClosed())
				connectdb();
				String SQL = "SELECT * FROM POKER";
				PreparedStatement statement = conn.prepareStatement(SQL);
				ResultSet result = statement.executeQuery();

				JSONArray data = new JSONArray();
				data = convert(result);
				jsonResponse.put("poke", data);
			
			resp.setStatus(200);
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			out.print(jsonResponse);
			out.flush();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			resp.getWriter().write(e.getMessage());
			resp.setStatus(400);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			resp.getWriter().write(e.getMessage());
			resp.setStatus(400);
		}

	}

	public static JSONArray convert(ResultSet rs) throws SQLException,
	JSONException {
		JSONArray json = new JSONArray();
		ResultSetMetaData rsmd = rs.getMetaData();

		while (rs.next()) {
			int numColumns = rsmd.getColumnCount();
			JSONObject obj = new JSONObject();

			for (int i = 1; i < numColumns + 1; i++) {
				String column_name = rsmd.getColumnName(i);

				if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
					obj.put(column_name, rs.getArray(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
					obj.put(column_name, rs.getBoolean(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
					obj.put(column_name, rs.getBlob(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
					obj.put(column_name, rs.getDouble(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
					obj.put(column_name, rs.getFloat(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
					obj.put(column_name, rs.getNString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
					obj.put(column_name, rs.getString(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
					obj.put(column_name, rs.getInt(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
					obj.put(column_name, rs.getDate(column_name));
				} else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
					obj.put(column_name, rs.getTimestamp(column_name));
				} else {
					obj.put(column_name, rs.getObject(column_name));
				}
			}

			json.put(obj);

		}
		return json;
	}

	public static void connectdb() throws SQLException
	{
		
		conn = DriverManager.getConnection(
				"jdbc:postgresql://ec2-54-204-41-249.compute-1.amazonaws.com:5432/d7cildudq0ugi7?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
				"yyzjumcxnlxlfk", "xEFGdo550hEKXKhI1DISjE_s4E");
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));
		//Server server = new Server(8084);
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setSecurityHandler(basicAuth("pokerapp", "prick", "What the F*** are you trying to do?"));
		context.setContextPath("/service");
		server.setHandler(context);
		context.addServlet(new ServletHolder(new HelloWorld()), "/*");
		if(conn == null||conn.isClosed())
			connectdb();
		server.start();
		server.join();
	}
	
	private static final SecurityHandler basicAuth(String username, String password, String realm) {

    	HashLoginService l = new HashLoginService();
        l.putUser(username, Credential.getCredential(password), new String[] {"user"});
        l.setName(realm);
        
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"user"});
        constraint.setAuthenticate(true);
         
        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/*");
        ConstraintMapping cm1[] = {cm};
        
        ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
        csh.setAuthenticator(new BasicAuthenticator());
        csh.setRealmName("myrealm");
        csh.setConstraintMappings(cm1);
        csh.setLoginService(l);
        
        return csh;
    	
    }
	
}
