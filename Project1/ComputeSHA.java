import java.io.File;
import java.io.FileInputStream;
import java.security.*;

public class ComputeSHA {
   //Hash using Sha-1 algorithm
   public static StringBuffer hashFunction (byte[] input) {
      StringBuffer hexString = new StringBuffer();
      try {
         MessageDigest md = MessageDigest.getInstance("SHA");
         byte[] hash = md.digest(input);
         for (byte b: hash) {
            //Append zeroes that are truncated
            if ((0xFF & b) < 0x10) {
               hexString.append('0');
            }
            hexString.append(Integer.toHexString(0xFF & b));
         }
      } catch (NoSuchAlgorithmException ex) {
         System.out.println("Provided hash algorithm does not exist!");
         System.exit(1);
      }
      return hexString; 
   }

   public static void main (String[] args) {
      if (args.length > 0) {
         File file = new File(args[0]);
         byte[] bytes = new byte[(int)file.length()];

         try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(bytes);
            fis.close();
            System.out.println(hashFunction(bytes));
         } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
         }
      } else {
         System.out.println("Please provide a file!");
      }
   }
}
