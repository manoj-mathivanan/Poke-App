package org.apache.android.xmpp;

import java.io.IOException;

import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

/**
 * Gather the xmpp settings and create an XMPPConnection
 */
public class SettingsDialog extends Dialog implements android.view.View.OnClickListener {
    private XMPPClient xmppClient;

    public SettingsDialog(XMPPClient xmppClient) {
        super(xmppClient);
        this.xmppClient = xmppClient;
    }

    protected void onStart() {
        super.onStart();
        setContentView(R.layout.settings);
        getWindow().setFlags(4, 4);
        setTitle("XMPP Settings");
        
        
        
        
        //Button ok = (Button) findViewById(R.id.ok);
        //ok.setOnClickListener(this);
    }

    public void okclick(View v) {
        String host = getText(R.id.host);
        String port = getText(R.id.port);
        String service = getText(R.id.service);
        String username = getText(R.id.userid);
        String password = getText(R.id.password);

        // Create a connection
        ConnectionConfiguration connConfig =
                new ConnectionConfiguration(host, Integer.parseInt(port), service);
        XMPPConnection connection = new XMPPConnection(connConfig) {
			
			@Override
			protected void shutdown() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			protected void sendPacketInternal(Packet arg0) throws NotConnectedException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void loginAnonymously() throws XMPPException, SmackException,
					SaslException, IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void login(String arg0, String arg1, String arg2)
					throws XMPPException, SmackException, SaslException, IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isUsingCompression() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isSecureConnection() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isConnected() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAuthenticated() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAnonymous() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String getUser() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getConnectionID() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected void connectInternal() throws SmackException, IOException,
					XMPPException {
				// TODO Auto-generated method stub
				
			}
		};

        try {
            connection.connect();
            Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
        } catch (XMPPException ex) {
            Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
            Log.e("XMPPClient", ex.toString());
            xmppClient.setConnection(null);
        } catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
            connection.login(username, password);
            Log.i("XMPPClient", "Logged in as " + connection.getUser());

            // Set the status to available
            Presence presence = new Presence(Presence.Type.available);
            connection.sendPacket(presence);
            xmppClient.setConnection(connection);
        } catch (XMPPException ex) {
            Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + username);
            Log.e("XMPPClient", ex.toString());
                xmppClient.setConnection(null);
        } catch (SaslException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        dismiss();
    }

    private String getText(int id) {
        EditText widget = (EditText) this.findViewById(id);
        return widget.getText().toString();
    }
}
