import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;

class Test extends Thread{
	
	static String readFor(RandomAccessFile pipe, String _for) throws Exception {

	    ByteArrayOutputStream bao = new ByteArrayOutputStream();
		    
	    do{
	        int charCode = pipe.read();
	        int charCodeLE = pipe.read();
	        
	          bao.write(charCode);
	          bao.write(charCodeLE);
	    } while(!bao.toString("utf-16le").contains(_for) || !bao.toString("utf-16le").endsWith("</M>"));
	    
	    return bao.toString("utf-16le").replace("</M>", "</M>\n");
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
