import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;

class Test extends Thread{
	//\HKEY_LOCAL_MACHINE\SOFTWARE\WOW6432Node\ctc_ESurfing
	static String readFor(RandomAccessFile pipe, String _for) throws Exception {

	    final ByteArrayOutputStream bao = new ByteArrayOutputStream();
		    
	    do{
	        int charCode = pipe.read();
	        
	        bao.write(charCode);
	        
	        String current = bao.toString(StandardCharsets.UTF_16LE.name());
	        
	        if(current.endsWith("</M>")) {
	        	
	        	Pattern p = Pattern.compile("<[^<>//]*>[^<>]*</+[^<>]*>"); 
	        	Matcher m = p.matcher(current); 
	        	while(m.find()) { 
	        	     System.out.println(m.group()); 
	        	}
	        	
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
			  final RandomAccessFile pipe = new RandomAccessFile("\\\\.\\pipe\\ESurfingClientPipe", "rw");
			  String echoText = "<M><OpS>Portal</OpS><OpT>Portal</OpT><OpC>ClientStart</OpC><P></P></M>";

			  pipe.write(echoText.getBytes(StandardCharsets.UTF_16LE));
			  
			  System.out.println("============>\tClientStartComplete");
			  
			  System.out.println(readFor(pipe, "ClientStartComplete"));
			  
			  
			  System.out.println("============>\tCheckEnv");
			  
			  echoText = "<M><OpS>Portal</OpS><OpT>Portal</OpT><OpC>CheckEnv</OpC><P></P></M>";

			  pipe.write(echoText.getBytes(StandardCharsets.UTF_16LE));
			  
			 
			  
			  System.out.println(readFor(pipe, "TicketResp"));
			} catch (Exception e) {
			  e.printStackTrace();
			}
	}
}
