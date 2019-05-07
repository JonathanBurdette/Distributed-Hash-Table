import java.io.*;
import java.net.*;
import java.util.Scanner;

//Jonathan Burdette

public class DHTClient {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Scanner keyboard = new Scanner(System.in);
				
		if(args.length >= 1) {
			
			try {
				//port of a server the client will send request to
				int portNumber = Integer.parseInt(args[0]);	
				
				//loops prompt until quit is entered
				boolean quit = false;
				while(quit == false) {
					
					byte[] sendData = new byte[1024];
					byte[] receiveData = new byte[1024];
					
					//creates client socket and translates hostname to IP address
					DatagramSocket clientSocket = new DatagramSocket();		
					InetAddress IPAddress = InetAddress.getByName("localhost");
					
					System.out.print("Enter a command: ");
					String line = keyboard.nextLine();
					
					String[] lineContents = line.split(" ");
					StringBuilder sb = new StringBuilder();

					//interprets commands and sends command to server
					if(lineContents[0].toLowerCase().equals("put")) {
						if(lineContents.length >= 3) { 
							for(int j=2; j<lineContents.length; j++) { 
								sb.append(lineContents[j] + " ");
							}
							String data = sb.toString();
							String key = lineContents[1];
							
							//creates Packet object to send and converts it to byte array
							Packet p = new Packet(-1, 0, Command.PUT, 0, key, data);
							sendData = p.convertToBytes();
							
							//creates datagram and sends it 
							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
							clientSocket.send(sendPacket);
							
							//reads datagram from server and displays response
							DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
							clientSocket.receive(receivePacket);
							Packet rp = new Packet();
							rp = rp.convertFromBytes(receiveData);
							String response = rp.data;
							System.out.println("FROM SERVER: " + response);
							
							clientSocket.close();
						} else {
							System.out.println("Invalid syntax. Enter \"PUT <word> <definition>\"");
						}
						
					} else if(lineContents[0].toLowerCase().equals("get")) {
						if(lineContents.length >= 2) { 
							String key = lineContents[1];
							
							//creates Packet object to send and converts it to byte array
							Packet p = new Packet(-1, 0, Command.GET, 0, key, "");
							sendData = p.convertToBytes();
							
							//creates datagram and sends it 
							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portNumber);
							clientSocket.send(sendPacket);
							
							//reads datagram from server and displays response
							DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
							clientSocket.receive(receivePacket);
							Packet rp = new Packet();
							rp = rp.convertFromBytes(receiveData);
							String response = rp.data;
							System.out.println("FROM SERVER: " + response);
							
							clientSocket.close();
						} else {
							System.out.println("Invalid syntax. Enter \"GET <word>\"");
						}
					} else if(lineContents[0].toLowerCase().equals("quit")) {
						quit = true;
					} else {
						System.out.println("Unknown command");
					}
				}
				keyboard.close();
			} catch(NumberFormatException e) {
				System.out.println("Invalid port number");
			}
		} else {
			System.out.println("To run the program, enter \"DHTClient <server port number>\"");
		}
	}
}
