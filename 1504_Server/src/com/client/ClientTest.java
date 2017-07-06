package com.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.server.DataType;

public class ClientTest {
	public static void main(String[] args) {
		try {
			Socket socket=new Socket("172.18.47.77", 1504);
			
			//获得输出流 发送报文
			OutputStream os = socket.getOutputStream();
			DataType dataType=new DataType();
			dataType.type=1;
			Gson gson=new Gson();
			String msg= gson.toJson(dataType);
			os.write(msg.getBytes());
			os.flush();
			
			//获得输入流拿到返回报文
			InputStream in = socket.getInputStream();
			byte [] buff=new byte[1024];
			int len=-1;
			StringBuffer sb=new StringBuffer();
			while((len=in.read(buff))!=-1){
				sb.append(new String(buff,0,len));
			}
			
			//解析
			String [] strs=gson.fromJson(sb.toString(), String[].class); 
			for(String str:strs){
				System.out.println(str);
			}
			
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
