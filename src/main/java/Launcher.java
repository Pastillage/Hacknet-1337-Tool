import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Launches the program.
 */
public class Launcher
{
    public static void main(String[] args)
    {
        System.out.println("=========================================");
        System.out.println("=          Hacknet 1337 Tool            =");
        System.out.println("=========================================");
        System.out.println("Remove config.properties if you need to change the directory of your accounts folder.");

        System.out.println("\nSpecify your Accounts folder (Default: Documents/My Games/Hacknet/Accounts)\n");


        if (args.length > 1 && args[1] != null)
        {
            if (args[1].contains("-c"))
            {
                removePropertiesFile();
            }
        }
        File dir = FileBrowser.getINSTANCE().run();

        try
        {
            dir.toPath();
        }
        catch (NullPointerException ex)
        {
            removePropertiesFile();
            System.out.println( "Forcefully killing java...\n" +
                                "(Your computer will say java stopped responding, so don't panic when you see it)");
            return;
        }

        if (args.length > 0)
        {
            runner(args[0], dir);
        }
        else
        {
            System.out.println("Please, specify your filename by adding it as a launch option.");
        }
    }

    /**
     * Removes the properties file, use with care.
     */
    public static void removePropertiesFile()
    {
        File properties = new File("config.properties");
        properties.delete();
        System.out.println("[Warning]\tconfig.properties has been removed!");
    }

    /**
     * Runs every tick of the program to find the IP and copy it to clipboard then return when done.
     * @param accountName The Account Name of the Account you're using.
     */
    public static void runner(String accountName, File dir)
    {
        // Use Hacknet Filename scheme save_{filename}.xml
        String filename = "\\save_" + accountName + ".xml";

        File xml = new File(dir+filename);

        System.out.println("Scanning for your Account File...");
        // infinitly search for the file then copy the ip when it's found.
        while (true)
        {
            if (xml.exists())
            {
                System.out.println("Found: " + xml.toPath());
                Long past = System.currentTimeMillis();
                String ip = findHeartIP(xml);
                System.out.println(System.currentTimeMillis() - past + "ms");

                // Put the ip with the connect command on your clipboard.
                StringSelection selector = new StringSelection("connect " + ip);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selector, selector);

                System.out.println("Put \"connect " + ip + "\" on your clipboard.");
                break;
            }
            System.out.print(".");
        }
    }

    /**
     * Finds the IP of the heart by parsing the xml save data.
     * @param accountXML Input the location of the xml save data.
     * @return Returns the ip of the heart.
     */
    public static String findHeartIP(File accountXML)
    {
        try
        {
            Scanner parser = new Scanner(accountXML);
            while (parser.hasNextLine())
            {
                String curr = parser.nextLine();

                if (curr.contains("Porthack.Heart"))
                {
                    curr = curr.replace("<computer name=\"Porthack.Heart \" ip=\"", "");
                    curr = curr.replace("\" type=\"3\" spec=\"none\" id=\"porthackHeart\"  >", "");
                    //String[] parts = curr.split(" ");
                    System.out.println(curr + " Found as IP!");
                    return curr;
                }

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            removePropertiesFile();
            System.out.println("Launcher.findHeartIP");
            System.out.println("[ERROR]\tSomething went terribly wrong please provide this text to the developer.");
        }
        return null;
    }
}
