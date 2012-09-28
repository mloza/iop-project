package gui;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	private static PrintWriter out;
	
	public static void create(String destinationPath) throws IOException {
		out = new PrintWriter(new BufferedWriter(new FileWriter(destinationPath)));
	}
	
	public static void log(String msg) {
		if(out != null) {
			out.println(msg);
			out.flush();
		} else {
            System.out.println(msg);
        }
		
	}
	
	public static void logException(Exception e) {
		if(out != null) {
			out.println("GOT EXCEPTION: ");
			e.printStackTrace(out);
			out.println("");
			out.flush();
		}
		
	}
}
