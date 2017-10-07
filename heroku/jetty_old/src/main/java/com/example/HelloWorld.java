
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

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
 
public class HelloWorld extends HttpServlet{

	String mobile;
	String Name;
	private static final String GOOGLE_SERVER_KEY = "AIzaSyDr4FrUBiF2hhbz34RCm3uqIDNpRK9JJw8";
	  static final String MESSAGE_KEY = "message";  
	 
	  public HelloWorld() {
	    super();
	  }
	
	@Override
	 protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	            throws ServletException, IOException {
		
		if(req.getPathInfo().contains("dbaccess"))
		{
		JSONObject jsonObject = new JSONObject();
		Connection conn;
		
		
		StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }
		  
		  try {
			  
			  jsonObject = (JSONObject)new JSONParser().parse(jb.toString());
		  
		  } catch (ParseException e) {
		    // crash and burn
		    throw new IOException("Error parsing JSON request string");
		  }
	
		  mobile =(String) jsonObject.get("mobile_number");
		 Name = (String) jsonObject.get("name");
		 JSONObject jsonResponse = new JSONObject();	
		 
		 try {
				//Class.forName("com.mysql.jdbc.Driver");
				
				 conn = DriverManager.getConnection("jdbc:postgresql://ec2-54-204-41-249.compute-1.amazonaws.com:5432/d7cildudq0ugi7?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory","yyzjumcxnlxlfk","xEFGdo550hEKXKhI1DISjE_s4E");
				 if(conn != null)
				  {
					 String SQL = Name;
					 PreparedStatement statement = conn.prepareStatement(SQL);
					 ResultSet result = statement.executeQuery();
					 
					 JSONArray data = new JSONArray();
					data = convert(result);
					 jsonResponse.put("poke", data);
				  }
				 resp.setStatus(200);
				 resp.setContentType("application/json");
				 PrintWriter out = resp.getWriter();
				 out.print(jsonResponse);
				 out.flush();
				 
				 
			}  catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
		}
		
		//Insert during registration
		if(req.getPathInfo().contains("insertPerson"))
		{
		JSONObject jsonObject = new JSONObject();
		Connection conn;
		
		
		StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = req.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }
		  
		  try {
			  
			  jsonObject = (JSONObject)new JSONParser().parse(jb.toString());
		  
		  } catch (ParseException e) {
		    // crash and burn
		    throw new IOException("Error parsing JSON request string");
		  }
	
		  mobile =(String) jsonObject.get("mobile_number");
		 Name = (String) jsonObject.get("name");
		  try {
			//Class.forName("com.mysql.jdbc.Driver");
			
			 conn = DriverManager.getConnection("jdbc:postgresql://ec2-54-204-41-249.compute-1.amazonaws.com:5432/d7cildudq0ugi7?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory","yyzjumcxnlxlfk","xEFGdo550hEKXKhI1DISjE_s4E");
			 if(conn != null)
			  {
				 String SQL = "INSERT INTO POKER (mobile_number,name) VALUES (?,?)";
				 PreparedStatement statement = conn.prepareStatement(SQL);
	    			statement.setString(1,mobile);
	    			statement.setString(2,Name);
	    			statement.executeUpdate();
				  
			  }
			 conn.close();
			 resp.setStatus(201);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contains("duplicate"))
			{
				 resp.setStatus(201);
			}
			e.printStackTrace();
		}
		  
		}
	
		
	// Get the mobile numbers check and send the matching ones
		
if(req.getPathInfo().contains("getThePokers"))
             
              {
                     JSONObject jsonObject = new JSONObject();
                     JSONObject jsonResponse = new JSONObject();
                     Connection conn;    
                     ResultSet result = null;
                     ResultSet result_countsent = null;
                     ResultSet result_countreceive = null;
                     PrintWriter out = resp.getWriter();
                     JSONArray finaldata = new JSONArray();
                     JSONArray temporary = new JSONArray();
                     HashMap<String,Integer> count_sent = new HashMap<String,Integer>();
                     HashMap<String,Integer> count_receive = new HashMap<String,Integer>();
                    
                     StringBuffer jb = new StringBuffer();
                       String line = null;
                       try {
                         BufferedReader reader = req.getReader();
                         while ((line = reader.readLine()) != null)
                           jb.append(line);
                       } catch (Exception e) { /*report an error*/ }
                      
                       try {
                            
                           //  Class.forName("com.mysql.jdbc.Driver");
                                  conn = DriverManager.getConnection("jdbc:postgresql://ec2-54-204-41-249.compute-1.amazonaws.com:5432/d7cildudq0ugi7?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory","yyzjumcxnlxlfk","xEFGdo550hEKXKhI1DISjE_s4E");
                             jsonObject = (JSONObject)new JSONParser().parse(jb.toString());
                             org.json.simple.JSONArray mob = (org.json.simple.JSONArray) jsonObject.get("mobile_number");
                          
                             int size = mob.size();
                            ArrayList<String> array = new ArrayList<String>();
                            
                             for (int i = 0; i < size; i++) {
                                    String x =  (String) mob.get(i);
                                   
                                    if(conn!=null)
                                         {
                                 
                                   
                                    String SQL = "SELECT MOBILE_NUMBER FROM POKER WHERE MOBILE_NUMBER = ?";
                                    PreparedStatement statement = conn.prepareStatement(SQL);
                                   
                                    statement.setString(1,x);
                                   
                                    result = statement.executeQuery(); 
                                         }
                                   
                                   if(result.next())
                                  {
                                           String r= result.getString("mobile_number");
                                           array.add(r);
                          
                                   }
                                    
                                    
                       }
                             String mobilenumber = req.getHeader("mobilenumber");
                            
                            
                                    
                                    for(int d=0;d<array.size();d++)
                                    {
                                          
                                           if(conn!=null)
                                           {
                                          
                                           String SQL4="SELECT pokee,count FROM POKED WHERE poker = ? AND pokee=?";
                                           PreparedStatement statement4 = conn.prepareStatement(SQL4);
                                           statement4.setString(1,mobilenumber);
                                           statement4.setString(2,array.get(d));
                                           result_countsent = statement4.executeQuery();
                                           
                                    }
                                   
                                    if(result_countsent.next())
                                    {
                                           count_sent.put(result_countsent.getString("pokee"),result_countsent.getInt("count"));
                                          
                                    }
                             }
                          
                                   
                                    for(int e=0;e<array.size();e++)
                                    {
                                          
                                           if(conn!=null)
                                           {
                                          
                                           String SQL5="SELECT poker,count FROM POKED WHERE poker = ? AND pokee=?";
                                           PreparedStatement statement5 = conn.prepareStatement(SQL5);
                                           statement5.setString(1,array.get(e));
                                           statement5.setString(2,mobilenumber);
                                           result_countreceive = statement5.executeQuery();
                                           
                                    }
                                   
                                    if(result_countreceive.next())
                                    {
                                           count_receive.put(result_countreceive.getString("poker"),result_countreceive.getInt("count"));
                                          
                                    }
                             }
                            
                           System.out.println(count_sent);
                           System.out.println(count_receive);
 
                          
                           for(int f=0;f<array.size();f++)
                           {
                                  JSONObject x = new JSONObject();
                                  x.put("mobile", array.get(f));
                                  if(count_sent.containsKey(array.get(f)))
                                  {
                                         x.put("count_sent",count_sent.get(array.get(f)));
                                        
                                  }
                                  else
                                  {
                                         x.put("count_sent",0);
                                  }
                                  if(count_receive.containsKey(array.get(f)))
                                  {
                                         x.put("count_receive",count_receive.get(array.get(f)));
                                  }
                                  else{
                                         x.put("count_receive",0);
                                  }
                                  finaldata.put(x);
                                 
                           }
                                 
                                   jsonResponse.put("count",finaldata);
                                        
                                          
                           out.print(jsonResponse);
                             resp.setStatus(201);
                                  resp.setContentType("application/json");
                            
                                   out.flush();
                                  conn.close();
                            
                       }
                       catch (ParseException e) {
                         // crash and burn
                         throw new IOException("Error parsing JSON request string");
                       } catch (SQLException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                     }
                     }
                     
		//Receiving Poke
		
		if(req.getPathInfo().contains("sendPoke"))
		{
			Connection conn;
			JSONObject jsonObject = new JSONObject();
			ResultSet result = null;
			ResultSet result2 = null;
			StringBuffer jb = new StringBuffer();
			  String line = null;
			  try {
			    BufferedReader reader = req.getReader();
			    while ((line = reader.readLine()) != null)
			      jb.append(line);
			  } catch (Exception e) { /*report an error*/ }
			  
			  try {
				  //Class.forName("com.mysql.jdbc.Driver");
				 conn = DriverManager.getConnection("jdbc:postgresql://ec2-54-204-41-249.compute-1.amazonaws.com:5432/d7cildudq0ugi7?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory","yyzjumcxnlxlfk","xEFGdo550hEKXKhI1DISjE_s4E");
				  jsonObject = (JSONObject)new JSONParser().parse(jb.toString());
				  
				  
				  
				  String m1 =(String) jsonObject.get("mobile_number1");
				  String m2 =(String) jsonObject.get("mobile_number2");
				  
				  if(conn!=null)
					{
				  String SQL = "SELECT COUNT FROM POKED WHERE POKER= ? AND POKEE=?";
				  PreparedStatement statement = conn.prepareStatement(SQL);
				  
				  statement.setString(1,m1);
				  statement.setString(2,m2);
				  result = statement.executeQuery();  
				  
				 if(result.next())
				 {
					 int r= result.getInt("COUNT");
					 
					 String SQL1 = "update poked set count=? WHERE POKER = ? AND POKEE=?";
					  PreparedStatement statement1 = conn.prepareStatement(SQL1);
					  
					  statement1.setInt(1,(r+1));
					  statement1.setString(2,m1);
					  statement1.setString(3,m2);
					  statement1.executeUpdate();  
			 
				 } else{
					 
					 String SQL2 = "INSERT INTO POKED values(?,?,?) ";
					  PreparedStatement statement2 = conn.prepareStatement(SQL2);
					  statement2.setString(1,m1);
					  statement2.setString(2,m2);
					  statement2.setInt(3,1);
					   statement2.executeUpdate();  
					 
				 }
				  
					}
				  Result gcm = null;
				  
				    
				  String SQL3 = "SELECT reg FROM gcm WHERE mobile_number=?";
				  PreparedStatement statement3 = conn.prepareStatement(SQL3);
				  
				  statement3.setString(1,m2);
				  result2 = statement3.executeQuery();
				  String reg = null;
				  if(result2.next())
					 {
				  
					  reg= result2.getString("reg");
					 }
				  
				    // GCM RedgId of Android device to send push notification
				    String regId = reg;
				    
				      try {	  
						  
				        String userMessage = m1;
				        Sender sender = new Sender(GOOGLE_SERVER_KEY);
				        Message message = new Message.Builder().delayWhileIdle(true).addData(MESSAGE_KEY, userMessage).build();
				        System.out.println("regId: " + regId);
				        gcm = sender.send(message, regId, 1);
				        req.setAttribute("pushStatus", gcm.toString());
				        System.out.print(result.toString());
				      } catch (Exception ioe) {
				 
				     // req.getRequestDispatcher("index.jsp").forward(req, resp);
				    }
				  
				  conn.close();
			  
			  } catch (ParseException e) {
			    // crash and burn
			    throw new IOException("Error parsing JSON request string");
			  }  catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			 
		}
			
		
	//Receive gcm registration ID
		
		
		if(req.getPathInfo().contains("sendGCMRegId"))
		{
			JSONObject jsonObject = new JSONObject();
			Connection conn;
			
			StringBuffer jb = new StringBuffer();
			  String line = null;
			  try {
			    BufferedReader reader = req.getReader();
			    while ((line = reader.readLine()) != null)
			      jb.append(line);
			  } catch (Exception e) { /*report an error*/ }
			  
			  try {
				  
				  jsonObject = (JSONObject)new JSONParser().parse(jb.toString());
				  
				  String mob =(String) jsonObject.get("mobile_number");
				  String regId =(String) jsonObject.get("reg");
				  //Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:postgresql://ec2-54-204-41-249.compute-1.amazonaws.com:5432/d7cildudq0ugi7?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory","yyzjumcxnlxlfk","xEFGdo550hEKXKhI1DISjE_s4E");
				  
				 if(conn != null)
				  {
				  String SQL6 = "DELETE FROM gcm where mobile_number= ?";
                                  PreparedStatement statement6 = conn.prepareStatement(SQL6);
                                  statement6.setString(1,mob);
                                  statement6.executeUpdate();
					 String SQL = "INSERT INTO gcm(mobile_number,reg) VALUES (?,?)";
					 PreparedStatement statement = conn.prepareStatement(SQL);
		    			statement.setString(1,mob);
		    			statement.setString(2,regId);
		    			statement.executeUpdate();
					  
				  }
				  conn.close();
				  resp.setStatus(201);
			  } catch (ParseException e) {
			    // crash and burn
			    throw new IOException("Error parsing JSON request string");
			  } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
	}
	
	 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws ServletException, IOException {
		
		 Connection conn;
		 JSONObject jsonResponse = new JSONObject();	
		 
		 try {
				//Class.forName("com.mysql.jdbc.Driver");
				
				 conn = DriverManager.getConnection("jdbc:postgresql://ec2-54-204-41-249.compute-1.amazonaws.com:5432/d7cildudq0ugi7?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory","yyzjumcxnlxlfk","xEFGdo550hEKXKhI1DISjE_s4E");
				 if(conn != null)
				  {
					 String SQL = "SELECT * FROM POKER";
					 PreparedStatement statement = conn.prepareStatement(SQL);
					 ResultSet result = statement.executeQuery();
					 
					 JSONArray data = new JSONArray();
					data = convert(result);
					 jsonResponse.put("poke", data);
				  }
				 resp.setStatus(200);
				 resp.setContentType("application/json");
				 PrintWriter out = resp.getWriter();
				 out.print(jsonResponse);
				 out.flush();
				 
				 
			}  catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  
		 
		 
		 
		 
	 }
	
	 public static JSONArray convert( ResultSet rs )
			    throws SQLException, JSONException
			  {
			    JSONArray json = new JSONArray();
			    ResultSetMetaData rsmd = rs.getMetaData();

			    while(rs.next()) {
			      int numColumns = rsmd.getColumnCount();
			      JSONObject obj = new JSONObject();

			      for (int i=1; i<numColumns+1; i++) {
			        String column_name = rsmd.getColumnName(i);

			        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
			         obj.put(column_name, rs.getArray(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
			         obj.put(column_name, rs.getInt(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
			         obj.put(column_name, rs.getBoolean(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
			         obj.put(column_name, rs.getBlob(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
			         obj.put(column_name, rs.getDouble(column_name)); 
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
			         obj.put(column_name, rs.getFloat(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
			         obj.put(column_name, rs.getInt(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
			         obj.put(column_name, rs.getNString(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
			         obj.put(column_name, rs.getString(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
			         obj.put(column_name, rs.getInt(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
			         obj.put(column_name, rs.getInt(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
			         obj.put(column_name, rs.getDate(column_name));
			        }
			        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
			        obj.put(column_name, rs.getTimestamp(column_name));   
			        }
			        else{
			         obj.put(column_name, rs.getObject(column_name));
			        }
			      }

			      json.put(obj);
			      
			    }
			    return json;
			  }
		
	
	public static void main(String[]args) throws Exception {
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/service");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new HelloWorld()),"/*");
        server.start();
        server.join();
    }
}
