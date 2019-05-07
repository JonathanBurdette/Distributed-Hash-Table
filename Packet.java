import java.io.*;
/*
 * based on https://stackoverflow.com/questions/2836646/java-serializable-object-to-byte-array
 */
//Edited by Jonathan Burdette

public class Packet implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	int ttl;
	int origPort;
	int hash;
	Command cmd;
	String key;
	String data;
	
	public Packet() {
		
	}

	public Packet(int ttl, int origPort, Command cmd, int hash, String key, String data) {
		this.ttl = ttl;
		this.origPort = origPort;
		this.cmd = cmd;
		this.hash = hash;
		this.key = key;
		this.data = data;
	}
	
	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public int getOrigPort() {
		return origPort;
	}

	public void setOrigPort(int origPort) {
		this.origPort = origPort;
	}

	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	public Command getCmd() {
		return cmd;
	}

	public void setCmd(Command cmd) {
		this.cmd = cmd;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String toString() {
		return ttl+" "+origPort+" "+hash+" "+cmd+" "+key+" "+data;
	}
	
	public byte[] convertToBytes() throws IOException {
		try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(this);
			return bos.toByteArray();
		}
	}
	
	public Packet convertFromBytes(byte bytes[]) throws IOException, ClassNotFoundException {
		try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = new ObjectInputStream(bis)) {
			return (Packet)in.readObject();
		}
	}
}
