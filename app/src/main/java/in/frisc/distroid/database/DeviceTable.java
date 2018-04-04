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
            "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "    ip TEXT NOT NULL"+
            ")"
        };
    }

    @Override
    public String[] getMigration(int nextVersion) {
        return new String[0];
    }

}