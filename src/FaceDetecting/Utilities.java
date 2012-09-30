package FaceDetecting;

/**
 * temporary unused, was used to give logs, may be used in future
 * 
 * @author M.Loza
 * @deprecated
 */
public class Utilities {
	public static void log(String log) {
		System.out.println(log);
	}
}

class Logger {
    public static Logger getLogger() {
        return new Logger();
    }

    public void info(String asd) {
        //System.out.println(asd);
    }

    public void error(String asd) {
        //System.out.println(asd);
    }
}
