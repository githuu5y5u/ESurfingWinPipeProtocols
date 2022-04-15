import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Test extends Thread{
	//\HKEY_LOCAL_MACHINE\SOFTWARE\WOW6432Node\ctc_ESurfing
	static Pattern fullValue = Pattern.compile("<[^<>//]*>[^<>]*</+[^<>]*>");
	static Pattern key = Pattern.compile("^[^<>/]*");
	static Pattern keyReplace = Pattern.compile("<[^<>]*>");
	
	static String readFor(RandomAccessFile pipe, String _for) throws Exception {

	    final ByteArrayOutputStream bao = new ByteArrayOutputStream();
		    
	    do {
	        int charCode = pipe.read();
	        
	        bao.write(charCode);
	        
	        String current = bao.toString(StandardCharsets.UTF_16LE.name());
	        
	        if(current.endsWith("</M>")) {
	        	
	        	Matcher m = fullValue.matcher(current); 
	        	while(m.find()) { 
	        		 String result = m.group();
	        		 
	        		 Matcher matcher2 = key.matcher(result.substring(1));
	        		 matcher2.find();
	        		 
	        		 Matcher matcher3 = keyReplace.matcher(result);
	        		 
	        	     System.out.println(result + "\t->\t" + matcher2.group() + "\t->\t" + matcher3.replaceAll(""));
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
