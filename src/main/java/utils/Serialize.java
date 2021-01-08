package utils;
import java.io.*;

public class Serialize {
    
    // Saving the objects
    public static void save(Object object, String filename) {
        FileOutputStream fileStrm;
        ObjectOutputStream objStrm;

        try {
            fileStrm = new FileOutputStream(filename);
            objStrm = new ObjectOutputStream(fileStrm);
            objStrm.writeObject(object);
            objStrm.close();
            UserInterface.displayScs("The serialized file has been written to '" + filename + "'.");
        }
        catch (FileNotFoundException e) {
            UserInterface.displayError("File not found.");
        } 
        catch (NotSerializableException e) {
            // throw new IllegalArgumentException("Unable to save to file");
            UserInterface.displayError("Class in project not serializable");
        }
        catch (IOException e) {
            // throw new IllegalArgumentException("Unable to save to file");
            UserInterface.displayError("Unable to save to file");
        }
    }

    // Loading the objects
    public static Graph load(String filename) {
        FileInputStream fileStrm;
        ObjectInputStream objStrm;
        Graph inObj = null;

        try {
            fileStrm = new FileInputStream(filename);
            objStrm = new ObjectInputStream(fileStrm);
            inObj = (Graph) objStrm.readObject();
            objStrm.close();
            UserInterface.displayScs("The serialized file '" + filename + "' has been read.");
        } catch (ClassNotFoundException e) {
            UserInterface.displayError("Class not found. " + e.getMessage());
        } catch (Exception e) {
            //throw new IllegalArgumentException("Unable to load object from file");
            UserInterface.displayError(e.getMessage());
        }
        return inObj;
    }
    
}
