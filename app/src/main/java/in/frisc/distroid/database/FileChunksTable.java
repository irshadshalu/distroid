package in.frisc.distroid.database;

import co.jasonwyatt.squeaky.Table;

public class FileChunksTable extends Table {
    @Override
    public String getName() {
        return "filechunks";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String[] getCreateTable() {
        return new String[] {
            "CREATE TABLE filechunks ("+
            "    id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "    path TEXT NOT NULL,"+
            "    file_id INTEGER NOT NULL,"+
            "    device_present INTEGER NOT NULL"+
            ")"
        };
    }

    @Override
    public String[] getMigration(int nextVersion) {
        return new String[0];
    }
}