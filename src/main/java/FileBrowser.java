import javax.swing.JFileChooser;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * The FileBrowser singleton takes care of opening a filebrowser.
 */
public final class FileBrowser
{
    public static final FileBrowser INSTANCE = new FileBrowser();

    private String dir = "user.home";

    /**
     * Constructor for singleton.
     */
    FileBrowser()
    {

    }

    /**
     * Runs/Opens the file browse window.
     */
    public File run()
    {
        if (propertiesRead())
        {
            File previous = new File(dir);

            if (previous.exists())
            {
                System.out.println("Found: " + previous.toPath().toString());
                return previous;
            }
            System.out.println("Error Accessing Stored Folder.");
        }

        final JFileChooser fileBrowser = new JFileChooser(System.getProperty(dir));
        fileBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileBrowser.setDialogTitle("Select your hacknet savedata (accounts) location");

        try
        {
            fileBrowser.showOpenDialog(fileBrowser);
            File f = fileBrowser.getSelectedFile();
            propertiesWrite(f.toPath().toString());
            System.out.println("Added " + f.toPath().toString() + " to the properties file.");
            return f;
        }
        catch (Exception ex)
        {
            System.out.println("Operation canceled exiting...");
        }

        return null;
    }

    /**
     * Read properties file to get accounts data.
     * @return Returns the found accounts path.
     */
    public boolean propertiesRead()
    {
        Properties properties = new Properties();
        InputStream in = null;

        try
        {
            in = new FileInputStream("config.properties");

            if (in == null)
                return false;

            properties.load(in);
            dir = properties.getProperty("dir");
        }
        catch (FileNotFoundException e)
        {
            return false;
        }
        catch (IOException e)
        {
            return false;
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * Store the selected folder in a properties file.
     * @param dir A string that contains the path to the folder.
     */
    public void propertiesWrite(String dir)
    {
        Properties properties = new Properties();
        OutputStream out = null;

        try
        {
            out = new FileOutputStream("config.properties");
            properties.setProperty("dir", dir);
            properties.store(out, null);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (out != null)
            {
                try
                {

                    out.close();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets the INSTANCE of the singleton.
     */
    public static FileBrowser getINSTANCE()
    {
        return INSTANCE;
    }
}
