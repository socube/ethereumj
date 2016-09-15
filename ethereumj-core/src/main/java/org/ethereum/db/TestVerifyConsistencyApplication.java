package org.ethereum.db;

import org.ethereum.config.SystemProperties;
import org.ethereum.datasource.LevelDbDataSource;

import java.io.File;
import java.nio.file.Paths;


public class TestVerifyConsistencyApplication {

    private static final String NAME = "database-longbatch";

    private static final File dbFile = new File("database-test/" + NAME);

    public static void main(String[] args) {
        System.out.println("Run database " + Paths.get(SystemProperties.getDefault().databaseDir(), NAME));
        LevelDbDataSource dataSource = new LevelDbDataSource(NAME);
        System.out.println("Initializing...");
        try {
            dataSource.init();
            System.out.println("Initialized");
            System.out.println("Done. Loaded keys: " + dataSource.keys().size());
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Error in init " + e.getMessage());
            e.printStackTrace();
            System.exit(5);
        }
    }
}
