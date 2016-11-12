package wl.divers;

public class OSValidator {
	//retourne 1 si windows 
	//retourne 0 si linux
	public static int numOS;
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	
	
	public static int  recupNumOs(){		
		
		if (isWindows()) {
			numOS=1;
		} else if (isMac()) {
			numOS=0;
		} else if (isUnix()) {
			numOS=0;
		} else if (isSolaris()) {
			numOS=0;
		} else {
			numOS=0;
		}
		return numOS;

	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
		
	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

}
