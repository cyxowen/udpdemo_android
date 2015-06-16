package org.cloudstone.udpreceiver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.widget.TextView;

public class MainActivity extends Activity {

	protected DatagramSocket server;
	private TextView textView1;
	protected String recvStr;
	private boolean isRun = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setMovementMethod(LinkMovementMethod.getInstance());
		init();
	}

	private void init() {
		new Thread() {
			@Override
			public void run() {
				try {
					server = new DatagramSocket(49527);
					while (isRun) {
						byte[] recvBuf = new byte[1024];
						DatagramPacket recvPacket = new DatagramPacket(recvBuf,recvBuf.length);
						server.receive(recvPacket);
						recvStr = new String(recvPacket.getData(), 0,recvPacket.getLength());
						runOnUiThread(new Runnable() {
							public void run() {
								textView1.append(recvStr+"\n");
							}
						});
					}
				} catch (Exception e) {
					
				}
			}
		}.start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRun = false;
		if (server!=null)
			server.close();
	}
}
