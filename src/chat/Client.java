package chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	String name;
	Socket s;
	Thread receiver;
	Thread emiter;
	ObjectInputStream in;
	ObjectOutputStream out;
	Scanner sc = new Scanner(System.in);
	
	public Client() {
		try {
			s = new Socket("localhost", 2001);
			
			out = new ObjectOutputStream(s.getOutputStream());
		
			System.out.println("Enter your name: ");
			name = sc.next();
			
			receiver = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						try {
							in = new ObjectInputStream(s.getInputStream());
							System.out.println((Message) in.readObject());
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}			
			});
			
			emiter = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						try {
							out = new ObjectOutputStream(s.getOutputStream());
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
		} catch (UnknownHostException e) {
			//e.printStackTrace();
			System.out.println(e.getCause());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
}
