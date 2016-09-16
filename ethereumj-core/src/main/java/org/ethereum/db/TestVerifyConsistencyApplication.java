package org.ethereum.db;

import org.ethereum.config.SystemProperties;
import org.ethereum.datasource.LevelDbDataSource;

import java.io.File;
import java.nio.file.Paths;


public class TestVerifyConsistencyApplication {

    public static void main(String[] args) {
        final String NAME;
        final File dbFile;

        if (args.length > 0) {
            NAME = args[0];
            dbFile = new File("database-test/" + NAME);
        } else {
            NAME = "database-longbatch";
            dbFile = new File("database-test/" + NAME);
        }

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
