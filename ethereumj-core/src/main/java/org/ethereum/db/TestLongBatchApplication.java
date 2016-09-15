package org.ethereum.db;

import org.ethereum.datasource.LevelDbDataSource;

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

        System.out.println("Small test write");
        dataSource.updateBatch(createBatch(10));
        System.out.println("Prepare data");
        final int batchSize = 3000000;
        final Map<byte[], byte[]> batch = createBatch(batchSize);


        System.out.println("Starting long batch");
        dataSource.updateBatch(batch);
        System.out.println("Batch ended");


        dataSource.close();
    }

    private static Map<byte[], byte[]> createBatch(int batchSize) {
        HashMap<byte[], byte[]> result = new HashMap<byte[], byte[]>();
        for (int i = 0; i < batchSize; i++) {
            result.put(randomBytes(32), randomBytes(412));
        }
        return result;
    }

    public static byte[] randomBytes(int length) {
        byte[] result = new byte[length];
        new Random().nextBytes(result);
        return result;
    }

}
