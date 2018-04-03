package in.frisc.distroid.database;

import co.jasonwyatt.squeaky.Table;

public class FileChunksTable extends Table {
    @Override
    public String getName() {
        return "myfilechunks";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String[] getCreateTable() {
        return new String[] {
            "CREATE TABLE myfilechunks ("+
            "    path TEXT NOT NULL"+
            "    id INTEGER NOT NULL PRIMARY KEY"+
            "    file_id INTEGER NOT NULL"+
            "    device_present INTEGER NOT NULL"+
            ")"
        };
    }

    @Override
    public String[] getMigration(int nextVersion) {
        return new String[0];
    }
}