import lombok.extern.slf4j.Slf4j;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.BitSet;

/**
 * @author lzn
 * @date 2023/12/09 15:04
 * @description
 */
@Slf4j
public class BytesBody extends UnsafeBuffer {

    private final BitSet bitSet = new BitSet();
    private final int INT_VAL = Integer.BYTES;
    private final int LONG_VAL = Long.BYTES;
    private int bitIdxR;
    private int bitIdxW;
    private int bodyIdxR;
    private int bodyIdxW;

    public BytesBody(final int capacity) {
        this(capacity, true);
    }

    public BytesBody(final int capacity, boolean onHeap) {
        super(onHeap ? ByteBuffer.allocate(capacity + Integer.BYTES) : ByteBuffer.allocateDirect(capacity + Integer.BYTES));
    }

    @Override
    public void wrap(final byte[] bytes) {
        super.wrap(bytes);
        reset();
        BitSetConverter.convert(bitSet, BytesTool.convert(bytes));
    }

    @Override
    public void wrap(final ByteBuffer buffer) {
        super.wrap(buffer);
        reset();
        BitSetConverter.convert(bitSet, buffer.getInt(0));
    }

    /**
     * Put long value to the index position
     *
     * @param value Actual value
     */
    public BytesBody putLong(long value) {
        checkBounds(bodyIdxW);
        setupBitSet(value);
        putBitSet();
        putLong(bodyIdxW, value);
        bodyIdxW += LONG_VAL;
        return this;
    }

    public long getLong() {
        checkBounds(bodyIdxR);
        // No value existing
        if (!bitSet.get(bitIdxR)) {
            return -1;
        }
        bitIdxR++;
        long val = getLong(bodyIdxR);
        bodyIdxR += LONG_VAL;
        return val;
    }

    public BytesBody putString(String value) {
        checkBounds(bodyIdxW);
        setupBitSet(value);
        putBitSet();
        putStringUtf8(bodyIdxW, value);
        bodyIdxW += INT_VAL + value.length();
        return this;
    }

    public String getString() {
        checkBounds(bodyIdxR);
        // No value existing
        if (!bitSet.get(bitIdxR)) {
            return null;
        }
        bitIdxR++;
        String val = getStringUtf8(bodyIdxR);
        bodyIdxR += INT_VAL + val.length();
        return val;
    }

    public void reset() {
        bitIdxR = 0;
        bitIdxW = 0;
        if (bitSet != null) {
            bitSet.clear();
        }
        // bitset + header + body
        bodyIdxR = INT_VAL;
        bodyIdxW = INT_VAL;
    }

    private void checkBounds(int index) {
        if (index < 0 || index > capacity) {
            throw new IndexOutOfBoundsException("The index has been exceeded, index: " + index + ", capacity: " + capacity);
        }
    }

    private void putBitSet() {
        putInt(0, BitSetConverter.convert(bitSet));
    }

    private void setupBitSet(long val) {
        if (val == -1) {
            bitSet.clear(bitIdxW);
            return;
        }

        bitSet.set(bitIdxW++);
    }

    private void setupBitSet(String val) {
        if (val == null || val.length() == 0) {
            bitSet.clear(bitIdxW);
            return;
        }

        bitSet.set(bitIdxW++);
    }
}
