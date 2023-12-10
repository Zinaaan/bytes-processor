import java.nio.ByteBuffer;

/**
 * @author lzn
 * @date 2023/12/09 15:02
 * @description
 */
public class BytesProcessor {

    public static void main(String[] args) {
        BytesBody bytesBody = new BytesBody(50);
        bytesBody.putLong(111).putLong(222).putString("just").putString("a man");
        bytesBody.wrap(bytesBody.byteArray());
        System.out.println(bytesBody.getLong());
        System.out.println(bytesBody.getLong());
        System.out.println(bytesBody.getString());
        System.out.println(bytesBody.getString());
    }
}
