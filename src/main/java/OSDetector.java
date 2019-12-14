package pdfutil;

/**
 * 
 * @author Tuur Neckebroeck
 * SOURCE: https://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
 *
 */
public class OSDetector {

    private static String OS = System.getProperty("os.name").toLowerCase();


    public static boolean isWindows() {

        return (OS.indexOf("win") >= 0);

    }

    public static boolean isMac() {

        return (OS.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

    }

    public static boolean isSolaris() {

        return (OS.indexOf("sunos") >= 0);

    }

    public static String getDesktopPath() {
        String destFile = destFile = System.getProperty("user.home");
        if (OSDetector.isWindows()) {
            destFile += "\\Desktop\\";
        } else {
            destFile += "/Desktop/";
        }
        return destFile;
    }
}
