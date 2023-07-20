package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author nanwang
 *
 */
public class SerializationUtils {

	private SerializationUtils() {
	}

	/**
	 * Converts the given object to a byte array
	 * 
	 * @param toSave the object to save
	 */
	public static byte[] toByteArray(Object toSave) {

		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(toSave);
			byte[] ret = bos.toByteArray();

			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads an object from the given input stream
	 * 
	 * @param is the input stream to read from
	 * @return the read object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObject(byte[] bytes) {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;

		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			return (T) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
