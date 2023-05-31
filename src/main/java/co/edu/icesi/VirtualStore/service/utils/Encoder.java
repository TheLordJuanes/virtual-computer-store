package co.edu.icesi.VirtualStore.service.utils;


import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

//Adapted from https://medium.com/@kasunpdh/how-to-store-passwords-securely-with-pbkdf2-204487f14e84
public class Encoder {

    public static final int iterations = 10000;
    public static final int keyLength = 512;
    public static byte[] hashPassword( final char[] password, final byte[] salt ) {

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            return key.getEncoded( );
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }
}
