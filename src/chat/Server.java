package chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	String name;
	ServerSocket ss;
	LinkedList<Socket> clients;
	Thread welcomer;
	
	Scanner sc = new Scanner(System.in);
	
	public Server() {
		try {
			ss = new ServerSocket(2001);
			
			clients = new LinkedList<Socket>();
			
			System.out.println("Enter chat room name: ");
			name = sc.next();
			
			welcomer = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (true) {
						try {
							Socket client = ss.accept();
							clients.add(client);
							
							ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
							
							out.writeObject(new Message(name, "Welcome to "+name+" chat room!"));
							out.flush();

							Thread receiver = new Thread(new Runnable() {
								@Override
								public void run() {
									while(true) {
										try {
											ObjectInputStream in = new ObjectInputStream(client.getInputStream());
											Message msg = (Message) in.readObject();
											
											for (Socket cli : clients) {	
												ObjectOutputStream cout = new ObjectOutputStream(cli.getOutputStream());
												cout.writeObject(msg);
												cout.flush();
											}
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}			
							});
							
							Thread emiter = new Thread(new Runnable() {
								@Override
								public void run() {
									while(true) {
										try {
											out.writeObject(new Message(name, sc.next()));
											out.flush();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}			
							});
							
							emiter.start();
							receiver.start();
							
							try {
								emiter.join();
								receiver.join();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			
			welcomer.start();
			
			try {
				welcomer.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
