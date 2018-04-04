package in.frisc.distroid.database;

import co.jasonwyatt.squeaky.Table;

public class FilesTable extends Table {
    @Override
    public String getName() {
        return "files";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String[] getCreateTable() {
        return new String[] {
            "CREATE TABLE files (" +
            "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    path TEXT NOT NULL," +
            "    name TEXT NOT NULL," +
            "    thumbnail TEXT NOT NULL," +
            "    size INTEGER NOT NULL" +
            ")"
        };
    }

    @Override
    public String[] getMigration(int nextVersion) {
        return new String[0];
    }
}