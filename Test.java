import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;

class Test extends Thread{
	//\HKEY_LOCAL_MACHINE\SOFTWARE\WOW6432Node\ctc_ESurfing
	static String readFor(RandomAccessFile pipe, String _for) throws Exception {

	    ByteArrayOutputStream bao = new ByteArrayOutputStream();
		    
	    do{
	        int charCode = pipe.read();
	        
	        bao.write(charCode);
	        
	        String current = bao.toString("utf-16le");
	        
	        if(current.endsWith("</M>")) {
	        	System.out.println(current);
	        	
	        	if(!current.contains(_for))
	        		bao.reset();
	        	else
	        		return current;
	        }
	        
	    	
	    } while(true);
	}

	
	public static void main(String[] m) throws Exception {
		try {
			  RandomAccessFile pipe = new RandomAccessFile("\\\\.\\pipe\\ESurfingClientPipe", "rw");
			  String echoText = "<M><OpS>Portal</OpS><OpT>Portal</OpT><OpC>ClientStart</OpC><P></P></M>";
			  
			  pipe.write(echoText.getBytes("utf-16le"));
			  
			  //It will blocks after close.
			  
			  System.out.println(readFor(pipe, "ClientStartComplete"));
			} catch (Exception e) {
			  e.printStackTrace();
			}
	}
}
