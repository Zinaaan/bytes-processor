import java.util.BitSet;

/**
 * @author lzn
 * @date 2023/12/09 15:30
 * @description
 */
public class BitSetConverter {

    public static BitSet convert(int val){
        BitSet bitSet = new BitSet();
        int index = 0;
        while (val != 0) {
            if ((val & 1) == 1) {
                bitSet.set(index);
            }
            val >>>= 1;
            index++;
        }
        return bitSet;
    }

    public static void convert(BitSet bitSet, int val){
        int index = 0;
        while (val != 0) {
            if ((val & 1) == 1) {
                bitSet.set(index);
            }
            val >>>= 1;
            index++;
        }
    }

    public static int convert(BitSet bitSet){
        int intValue = 0;
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                intValue |= (1 << i);
            }
        }
        return intValue;
    }
}
