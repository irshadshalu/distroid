package in.frisc.distroid.database;

import co.jasonwyatt.squeaky.Table;

public class FilesTable extends Table {
    @Override
    public String getName() {
        return "myfiles";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String[] getCreateTable() {
        return new String[] {
            "CREATE TABLE myfiles ("+
            "    path TEXT NOT NULL"+
            "    id INTEGER NOT NULL PRIMARY KEY"+
            "    directory TEXT NOT NULL"+
            "    size INTEGER NOT NULL"+
            ")"
        };
    }

    @Override
    public String[] getMigration(int nextVersion) {
        return new String[0];
    }
}