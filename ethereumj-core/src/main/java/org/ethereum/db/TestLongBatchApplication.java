package org.ethereum.db;

import org.ethereum.datasource.LevelDbDataSource;
import org.ethereum.util.ByteUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestLongBatchApplication {

    private static final String NAME = "database-longbatch";
    private static final File dbFile = new File("database-test/" + NAME);

    public static void main(String[] args) {
        System.out.println("Run");
        LevelDbDataSource dataSource = new LevelDbDataSource(NAME);
        dataSource.destroyDB(dbFile);
        System.out.println("Removed data");
        dataSource.init();

        final int PART_1 = 500_000;
        final int PART_2 = 500_000;
        final int PART_3 = 2_000_000;

        System.out.println("Small test write");
        dataSource.updateBatch(createRandomBatch(10));  // must be persisted small set

        dataSource.updateBatch(createFixedBatch(0, PART_1 + PART_2, true));

        System.out.println("Prepare data");

        final Map<byte[], byte[]> batch1 = createFixedBatch(0, PART_1, true);// for update
        final Map<byte[], byte[]> batch2 = createFixedBatch(PART_1, PART_1 + PART_2, false);  // for delete
        final Map<byte[], byte[]> batch3 = createFixedBatch(PART_1 + PART_2, PART_1 + PART_2 + PART_3, true); // for add

        final Map<byte[], byte[]> batchAll = new HashMap<>();
        batchAll.putAll(batch1);
        batchAll.putAll(batch2);
        batchAll.putAll(batch3);
        System.out.println("Starting long batch " + batchAll.size());

        dataSource.updateBatch(batchAll);

        System.out.println("Batch ended");
        dataSource.close();
    }

    private static Map<byte[], byte[]> createRandomBatch(int batchSize) {
        HashMap<byte[], byte[]> result = new HashMap<>();
        for (int i = 0; i < batchSize; i++) {
            result.put(randomBytes(32), randomBytes(412));
        }
        return result;
    }

    private static Map<byte[], byte[]> createFixedBatch(int from, int to, boolean isRandom) {
        HashMap<byte[], byte[]> result = new HashMap<>();
        for (int i = from; i < to; i++) {
            result.put(ByteUtil.intToBytes(i), isRandom ? randomBytes(412) : null);
        }
        return result;
    }

    public static byte[] randomBytes(int length) {
        byte[] result = new byte[length];
        new Random().nextBytes(result);
        return result;
    }

}
