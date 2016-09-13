package org.ethereum.db;

import org.ethereum.datasource.LevelDbDataSource;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.Options;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.ethereum.TestUtils.randomBytes;
import static org.fusesource.leveldbjni.JniDBFactory.factory;

@Ignore
public class LevelDbConsistencyTest {

    private static final String NAME = "database-longbatch";
    private static final File dbFile = new File("database-test/" + NAME);
    private LevelDbDataSource dataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new LevelDbDataSource(NAME);
    }

    @Test
    public void step1_doLongBatch() {
        LevelDbDataSource dataSource = new LevelDbDataSource(NAME);
        dataSource.destroyDB(dbFile);
        dataSource.init();

        System.out.println("Starting long batch");
        final int batchSize = 500000;
        Map<byte[], byte[]> batch = createBatch(batchSize);

        System.out.println("Batch ended");
        
        dataSource.updateBatch(batch);
        dataSource.close();
    }

    @Test
    public void step2_checkConsistency() {
        LevelDbDataSource dataSource = new LevelDbDataSource(NAME);
        dataSource.init();

        System.out.println("Loaded keys count " + dataSource.keys().size());
    }

    @Test
    public void step3_repair() throws IOException {
        LevelDbDataSource dataSource = new LevelDbDataSource(NAME);

        final Options options = new Options();
        options.createIfMissing(true);
        options.compressionType(CompressionType.NONE);
        options.blockSize(10 * 1024 * 1024);
        options.writeBufferSize(10 * 1024 * 1024);
        options.cacheSize(0);
        options.paranoidChecks(true);
        options.verifyChecksums(true);
        options.maxOpenFiles(32);
        factory.repair(dbFile, options);

        dataSource.init();

        System.out.println("Loaded keys count " + dataSource.keys().size());
    }


    private static Map<byte[], byte[]> createBatch(int batchSize) {
        HashMap<byte[], byte[]> result = new HashMap<>();
        for (int i = 0; i < batchSize; i++) {
            result.put(randomBytes(32), randomBytes(5120));
        }
        return result;
    }

}