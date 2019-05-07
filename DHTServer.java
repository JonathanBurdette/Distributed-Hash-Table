import java.io.*;
import java.net.*;

//Jonathan Burdette

public class DHTServer {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		final int GLOBAL_TABLE_SIZE = 10000;
		
		if(args.length >= 5) {
			try {
				String serverName = args[0];
				int listenPort = Integer.parseInt(args[1]);
				int nextServerPort = Integer.parseInt(args[2]);
				int startRange = Integer.parseInt(args[3]);
				int endRange = Integer.parseInt(args[4]);

				//server is responsible for a slice of the hash table
				Table<String, String> ht = new HashTable<String, String>((endRange-startRange), startRange);
				
				//creates datagram socket at specified port
				DatagramSocket serverSocket = new DatagramSocket(listenPort);
				
				while(true) {
					
					byte[] receiveData = new byte[1024];
					byte[] sendData = new byte[1024];
					byte[] responseData = new byte[1024];
					
					//creates space for received datagram and receives it
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
					
					//instantiate packet from byte array
					Packet rp = new Packet();
					rp = rp.convertFromBytes(receiveData);
					System.out.println(rp.ttl+" "+rp.origPort+" "+rp.cmd+" "+rp.hash+" "+rp.key+" "+rp.data);
					
					//gets IP addr port #, of sender
					InetAddress IPAddress = receivePacket.getAddress();
					int port = receivePacket.getPort();
					
					//stores client's port in rp.origPort
					if(rp.origPort == 0) {
						rp.origPort = port;
					}
					
					String response = "";
					
					if(rp.ttl == 0) {
						
						//discards packet and sends error response to client
						response = "Packet Timeout Error";
						Packet e = new Packet(rp.ttl, rp.origPort, rp.cmd, rp.hash, rp.key, response);
						responseData = e.convertToBytes();
						DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, IPAddress, rp.origPort);
						serverSocket.send(responsePacket);
					} else {
						
		            	//if hash value has not been assigned, create one
		            	if(rp.hash == 0) {
		            		rp.hash = Math.abs(rp.key.hashCode()) % GLOBAL_TABLE_SIZE;
		            	}
		            	
		            	//if server is responsible for this hash value then add or get
		            	if(rp.hash >= startRange && rp.hash <= endRange) {
		            		if(rp.cmd == Command.PUT) {
		            			ht.put(rp.hash, rp.key, rp.data);
		            			response = "Entry added on "+serverName;
		            		} else {
		            			String def = ht.get(rp.hash, rp.key);
		            			if(def != null) {
			            			response = rp.key+": "+def;
		            			} else {
		            				response = "Word not found";
		            			}
		            		}
		            		
		            		//send client result
							Packet r = new Packet(rp.ttl, rp.origPort, rp.cmd, rp.hash, rp.key, response);
							responseData = r.convertToBytes();
							DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, IPAddress, rp.origPort);
							serverSocket.send(responsePacket);
		            	} else {
		            		
		            		//if this is the first server initialize ttl
		            		if(rp.ttl == -1) {
		            			rp.ttl = 3;
		            		}
		            		
		            		//decrements ttl of the Packet object and forwards the Packet to the next server
		            		rp.ttl--;
							Packet sp = new Packet(rp.ttl, rp.origPort, rp.cmd, rp.hash, rp.key, rp.data);
							sendData = sp.convertToBytes();
							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, nextServerPort);
							serverSocket.send(sendPacket);
		            	}
					}
					//end of while loop, loop back and wait for another datagram
				}
			} catch(NumberFormatException e) {
				System.out.println("Invalid input");
			}
		} else {
			System.out.println("To run the program, enter \"DHTServer <name of server> <listen port> <next server port> <start of range> <end of range>\"");
		}
	}
}
