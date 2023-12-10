/**
 * @author lzn
 * @date 2023/12/09 18:45
 * @description
 */
public class BytesTool {

    public static int convert(byte[] byteArray) {
        if (byteArray.length < Integer.BYTES) {
            throw new IllegalArgumentException("Byte array must have length greater than Integer.BYTES");
        }

        int result = 0;
        for (int i = 0; i < Integer.BYTES; i++) {
            result |= (byteArray[i] & 0xFF) << (8 * i);
        }
        return result;
    }
}
