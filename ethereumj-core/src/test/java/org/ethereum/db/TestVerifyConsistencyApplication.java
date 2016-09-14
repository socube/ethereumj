package org.ethereum.db;

import org.ethereum.datasource.LevelDbDataSource;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.ethereum.TestUtils.randomBytes;

public class TestVerifyConsistencyApplication {

    private static final String NAME = "database-longbatch";
    private static final File dbFile = new File("database-test/" + NAME);


    public static void main(String[] args) {
        System.out.println("Run");
        LevelDbDataSource dataSource = new LevelDbDataSource(NAME);
        System.out.println("Initializing...");
        dataSource.init();
        System.out.println("Initialized");

        System.out.println("Done. Loaded keys: " + dataSource.keys().size());


        dataSource.close();
    }


}
