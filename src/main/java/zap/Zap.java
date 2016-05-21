package zap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zap {

	private static final String ZIPPEDFILES = "C:\\Users\\rafa\\Documents\\Projects\\zapzapnet\\chats";
	
	private static int runnigIndex = 0;
	
    public static void main(String[] args) {
        try {
            
        	String UNZIPPEDFILES = ZIPPEDFILES + "\\unzip";
        	
        	for (File arq : new File(ZIPPEDFILES).listFiles()) {
				System.out.println("### unzipping: " + arq.getAbsoluteFile().toString());
				unZipIt(arq.getAbsoluteFile().toString(), UNZIPPEDFILES);
			}


			List<Connection> connections = getConnections(UNZIPPEDFILES);

			new NetGenerator().generate(connections);


		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("Acabou!");
    }

	private static List<Connection> getConnections(String UNZIPPEDFILES) throws ParseException {
		int fileCount = new File(UNZIPPEDFILES).listFiles().length;

		int fileIndex = 0;
		List<Connection> connections = new ArrayList<>();
		for (File arq : new File(UNZIPPEDFILES).listFiles()) {
            System.out.println(fileIndex++ + " de " + fileCount + " extraiNomesAfiliados: " + arq.getName());
            List<Message> msgs = extractMsgs(arq);
            double avgTimeBetweenMsgs = getAvgTimeBetweenMsgs(msgs);
            System.out.println("avgTimeBetweenMsgs:" + avgTimeBetweenMsgs);

            System.out.println("size:" + msgs.size());
            for (int i=0; i < msgs.size()-1; i++){

                Message current = msgs.get(i);
                Message next = msgs.get(i + 1);

                long diff = (next.getDate().getTime()/1000) -(current.getDate().getTime()/1000);


                if((!next.getSender().equals(current.getSender())) && ((diff) < (avgTimeBetweenMsgs))) {
                    connections.add(connectionFactory(current, next));
                }
            }
}
		return connections;
	}

	private static Connection connectionFactory(Message current, Message next) {
		Connection c = new Connection();
		c.setSender(next.getSender());
		c.setReceiver(current.getSender());
		c.setData(next.getDate());
		logConnection(c);
		return c;
	}

	private static void logConnection(Connection c) {
		//System.out.println("sender:" + c.getSender() + " receiver:" + c.getReceiver() + " date:" + c.getData());
	}

	private static double getAvgTimeBetweenMsgs(List<Message> msgs) {

		double MAX_DIFF_IGNORE =  60*60*2;
		double lastTimestamp = msgs.get(0).getDate().getTime();
		
		List<Double> diffs = new ArrayList<Double>();
		for (Message message : msgs) {
			double now = message.getDate().getTime();
			double diff = (now - lastTimestamp)/1000;
		//	System.out.println(now + " " + lastTimestamp + " " + diff/1000);
			//System.out.println(new Date((long) now) + " " + new Date((long) lastTimestamp) + " " + diff/1000 );
			if(diff < MAX_DIFF_IGNORE) {
				diffs.add(diff);
			}
			lastTimestamp = now;
		}

		double[] m = new double[diffs.size()];
		for (int i = 0; i < m.length; i++) {
			m[i] = diffs.get(i).doubleValue();  // java 1.4 style
			// or:
			m[i] = diffs.get(i);                // java 1.5+ style (outboxing)
		}
			int middle = m.length/2;
			if (m.length%2 == 1) {
				return m[middle];
			} else {
				return (m[middle-1] + m[middle]) / 2.0;
			}

	}

	private static ArrayList<Message> extractMsgs(File arq) throws ParseException {
		List<String> lines = readStringListFromFile(arq);
		Pattern pattern = Pattern.compile("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{1,2},\\s[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}:");
	    
		ArrayList<Message> msgs = new ArrayList<Message>();
	    
	    for (String line : lines) {
	    	Matcher matcher = pattern.matcher(line);
	 	   	if(matcher.find()) {
	 	   		
	 	   		String aux = line.substring(matcher.end());
	 	   		String sender = "Error";
	 	   		
	 	   		String text = "Error";
	 	   		if(aux.indexOf(":") > 0) {
	 	   			sender = aux.substring(0,aux.indexOf(":"));
	 	   			text = aux.substring(aux.indexOf(":")+1);

					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy, HH:mm:ss:");
					Date date = sdf.parse(matcher.group());

					Message msg = new Message();
					msg.setDate(date);
					msg.setSender(sender);
					msg.setText(text);

					msgs.add(msg);

				}
				else {
					System.out.println("erro. mensagem invalida: "+ line );
				}
	 	   	}
		}
	    
	    
	    //System.out.println(msgs.size());
	    
	    
	    return msgs;
		
	}

	private static String readStringFromFile(File file) {
		BufferedReader reader = null;
		StringBuilder lines = new StringBuilder();
		try {//FileSystems.getDefault().getPath(".", filename ),
			reader  = new BufferedReader(
				    new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line = null;
            while ( (line = reader.readLine()) != null ) {
                lines.append(line);
                lines.append(" @@NEWLINE@@ ");
            }
            
            if(reader != null)
    			reader.close();
		}
	    catch (Exception e) {
			e.printStackTrace();
	    }
		return lines.toString();
	}


	private static List<String> readStringListFromFile(File file) {
		BufferedReader reader = null;
		List<String> lines = new ArrayList<>();
		try {//FileSystems.getDefault().getPath(".", filename ),
			reader  = new BufferedReader(
				    new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line = null;
            while ( (line = reader.readLine()) != null ) {
                lines.add(line);
            }
            
            if(reader != null)
    			reader.close();
		}
	    catch (Exception e) {
			e.printStackTrace();
	    }
		return lines;
	}


	
	
	/**
     * Unzip it
     * @param zipFile input zip file
     * @param outputFolder zip file output folder
     */
    public static void unZipIt(String zipFile, String outputFolder){

     byte[] buffer = new byte[1024];
    	
     try{
    		
    	//create output directory is not exists
    	File folder = new File(outputFolder);
    	if(!folder.exists()){
    		folder.mkdir();
    	}
    		
    	//get the zip file content
    	ZipInputStream zis = 
    		new ZipInputStream(new FileInputStream(zipFile));
    	//get the zipped file list entry
    	ZipEntry ze = zis.getNextEntry();
    		
    	while(ze!=null){
    			
    	   String fileName = ze.getName();
           File newFile = new File(outputFolder + File.separator + fileName);
                
           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
                
            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
              
            FileOutputStream fos = new FileOutputStream(newFile);             

            int len;
            while ((len = zis.read(buffer)) > 0) {
       		fos.write(buffer, 0, len);
            }
        		
            fos.close();   
            ze = zis.getNextEntry();
    	}
    	
        zis.closeEntry();
    	zis.close();
    		
    	System.out.println("Done");
    		
    }catch(IOException ex){
       ex.printStackTrace(); 
    }
   }    
}