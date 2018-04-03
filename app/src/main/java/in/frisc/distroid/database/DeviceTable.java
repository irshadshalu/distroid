package in.frisc.distroid.database;

import co.jasonwyatt.squeaky.Table;

public class DeviceTable extends Table {
    @Override
    public String getName() {
        return "devices";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String[] getCreateTable() {
        return new String[] {
            "CREATE TABLE devices ("+
            "    ip TEXT NOT NULL"+
            "    id INTEGER NOT NULL PRIMARY KEY"+
            ")"
        };
    }

    @Override
    public String[] getMigration(int nextVersion) {
        return new String[0];
    }
}