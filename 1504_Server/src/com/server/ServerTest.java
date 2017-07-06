package com.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.google.gson.Gson;

public class ServerTest {
	public static void main(String[] args) {
		startServerSocket();
//		File file=new File("F:\\桌面背景");
//		System.out.println(file.list().length);
//		for(String str:file.list()){
//			System.out.println(str);
//		}
	}

	/**
	 * 启动一个服务
	 */
	private static void startServerSocket() {
		// TODO Auto-generated method stub
		ServerSocket serverSocket=null;
		try {
			//初始化ServerSocket 端口1504
			serverSocket=new ServerSocket(1504);
			System.out.println("服务已启动！！！");
			for(;;){
				Socket socket = serverSocket.accept();
				//处理socket
				parseSocket(socket);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(serverSocket!=null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * 处理socket
	 * @param socket
	 */
	private static void parseSocket(Socket socket) {
		// TODO Auto-generated method stub
		try {
			InputStream in = socket.getInputStream();
			
			//根据InputStream发送内容
			byte [] buff =new byte[1024];
			int len=in.read(buff);
			String dataStr = new String(buff,0,len);
			System.out.println("收到---"+socket.getInetAddress().toString()+"---"+dataStr);
			
			//解析发送内容
			Gson gson=new Gson();
			DataType data= gson.fromJson(dataStr, DataType.class);
			if(data.type==1){
				select(socket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//查询F:\\桌面背景的所有图片 并发送
	private static void select(Socket socket) throws IOException {
		// TODO Auto-generated method stub
		OutputStream os=socket.getOutputStream();
		Gson gson=new Gson();
		File file=new File("F:\\桌面背景");
		if(file.isDirectory()){
			String [] strs=file.list();
			String osStr = gson.toJson(strs);
			System.out.println("发送---"+socket.getInetAddress().toString()+"---"+osStr);
			os.write(osStr.getBytes());
			os.flush();
			
		}
		if(socket!=null&&!socket.isClosed()){
			socket.close();
		}
		
	}
}
