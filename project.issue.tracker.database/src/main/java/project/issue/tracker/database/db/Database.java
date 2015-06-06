package project.issue.tracker.database.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.Compression;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.CqlResult;
import org.apache.cassandra.thrift.CqlRow;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public final class Database { // ver: 3.3.6

    static private final class CassandraFactory extends BasePoolableObjectFactory< Client> {

        private int port;
        private String host;

        public CassandraFactory(final String host_, final int port_) {
            this.host = host_;
            this.port = port_;
        }

        @Override
        public Client makeObject() throws Exception { // INPUT AND OUTPUT TRANSPORT IS ONE SINGLE OBJECT !
            //System.out.println("MAKE");
            TTransport transport = new TFramedTransport(new TSocket(host, port));
            TProtocol protocol = new TBinaryProtocol(transport);
            return new Client(protocol);
        }

        @Override
        public void activateObject(Client client) throws Exception {
            //System.out.println("ACTIVATE");
            TTransport transport = client.getInputProtocol().getTransport();
            transport.open();
            super.activateObject(client);
        }

        @Override
        public void passivateObject(Client client) throws Exception {
            //System.out.println("PASSIVE");
            TTransport transport = client.getInputProtocol().getTransport();
            transport.flush();
            transport.close();
            super.passivateObject(client);
        }

        @Override
        public void destroyObject(Client client) throws Exception {
            //System.out.println("DESTROY");
            TTransport transport = client.getInputProtocol().getTransport();
            transport.flush();
            transport.close();
            super.destroyObject(client);
        }
    }

    static private final class TreeMapCompare implements Comparator< String> {

        char first, second;
        int key1_length, key2_length;

        public int compare(final String key1, final String key2) {
            if (key1.charAt(0) != '#' && key2.charAt(0) == '#') {
                return -1;
            }
            if (key2.charAt(0) != '#' && key1.charAt(0) == '#') {
                return 1;
            }
            key1_length = key1.length();
            key2_length = key2.length();
            if (key1_length < key2_length) {
                return -1;
            } else if (key1_length > key2_length) {
                return 1;
            } else {
                for (int i = 0; i < key1_length; ++i) {
                    first = key1.charAt(i);
                    second = key2.charAt(i);
                    if (first == second) {
                        continue;
                    }
                    if (first < second) {
                        return -1;
                    } else {
                        return 1;
                    }
                }

            }
            return 0;
        }
    }

    static public final class Pair {

        private String first, second;

        public Pair() {
            this.first = null;
            this.second = null;
        }

        public Pair(final String first_, final String second_) {
            this.first = first_;
            this.second = second_;
        }

        public final String getFirst() {
            return this.first;
        }

        public final void setFirst(final String first_) {
            this.first = first_;
        }

        public final String getSecond() {
            return this.second;
        }

        public final void setSecond(final String second_) {
            this.second = second_;
        }

        @Override
        public String toString() {
            return "Pair{" + "first=" + first + ", second=" + second + '}';
        }
    }
    static private ObjectPool<Client> cassandra_pool;
    private Client client;

    public Database() {
        this.initConnection("127.0.0.1", 9160, "debugtracker");
    }

    public Database(final String host, final int port, final String keyspace) {
        this.initConnection(host, port, keyspace);
    }

    private void initConnection(final String host, final int port, final String keyspace) {
        final StringBuilder s_builder = new StringBuilder();

        if (Database.cassandra_pool == null) {
            Database.cassandra_pool = new StackObjectPool<Client>(new CassandraFactory(host, port));
        }

        if (this.client == null) {
            try {
                this.client = cassandra_pool.borrowObject();
                this.client.set_cql_version("3.0.0");
                this.client.set_keyspace(keyspace);
            } catch (InvalidRequestException e) {
                s_builder.append("CREATE KEYSPACE ");
                s_builder.append(keyspace);
                s_builder.append(" WITH REPLICATION = { 'class':'SimpleStrategy', 'replication_factor':1 }");
                this.query(s_builder.toString());
                try {
                    this.client.set_keyspace(keyspace);
                } catch (TException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (Exception e) {
                // System.out.println("Error in initConnection: " + e.toString());
            }
        }

    }

    private void query(final String query) {
        try {
            this.client.execute_cql3_query(ByteBufferUtil.bytes(query), Compression.NONE, ConsistencyLevel.ONE);
        } catch (Exception e) {
            // System.out.println("Error while executing query: " + e.toString());
        }
    }

    private CqlResult resultQuery(final String query) throws Exception {
        final CqlResult cql_result = this.client.execute_cql3_query(ByteBufferUtil.bytes(query), Compression.NONE, ConsistencyLevel.ONE);
        return cql_result;
    }

    private String getNextAutoId(final String table_name, final String... keys) {
        String next_auto_id;

        next_auto_id = this.selectSingle(table_name, keys);
        if (next_auto_id == null) {
            next_auto_id = "1";
        }
        this.insertWithKey(table_name, this.incrementString(next_auto_id), keys);
        return "#" + next_auto_id;
    }

    private void deleteTree(final String table_name, final TreeMap< String, Object> map, final String keys[], final int index) {
        if (map == null) {
            return;
        }

        Object value;
        Set< String> map_keys = map.keySet();
        for (String key : map_keys) {
            keys[index] = key;
            value = map.get(key);
            if (value instanceof String) {
                this.delete(table_name, false, keys);
            } else {
                deleteTree(table_name, (TreeMap< String, Object>) value, keys, index + 1);
            }
            keys[index] = "";
        }
    }

    private String incrementString(final String value) {
        int digit, for_next_digit = 1;
        StringBuilder s_builder = new StringBuilder(value);

        for (int i = s_builder.length() - 1; i >= 0; --i) {
            digit = s_builder.charAt(i) - '0' + for_next_digit;
            for_next_digit = 0;
            if (digit <= 9) {
                s_builder.setCharAt(i, (char) (digit + '0'));
                break;
            } else {
                s_builder.setCharAt(i, '0');
                for_next_digit = 1;
            }
        }
        if (for_next_digit != 0) {
            return "1" + s_builder.toString();
        } else {
            return s_builder.toString();
        }
    }

    private TreeMap< String, Object> selectAllForDelete(final String table_name, final String... keys) {
        final int depth = keys.length;
        final CqlResult cql_result;
        final StringBuilder s_builder = new StringBuilder();
        final List< CqlRow> rows;
        final TreeMap< String, Object> result;

        int column_index, columns_size = 0;
        String column_name, value;
        Column column;
        TreeMap< String, Object> working_map, parent_map;
        ArrayList< Column> columns;

        s_builder.append("SELECT value");
        for (int i = 1; i <= depth; ++i) {
            if (!keys[i - 1].equals("")) {
                continue;
            }
            s_builder.append(",c_key");
            s_builder.append(i);
            ++columns_size;
        }
        if (columns_size == 0) {
            return null;
        }

        s_builder.append(" FROM ");
        s_builder.append(table_name);
        if (!keys[0].equals("")) {
            s_builder.append(" WHERE ");
            for (int i = 1; i <= depth; ++i) {
                s_builder.append("c_key");
                s_builder.append(i);
                s_builder.append("='");
                s_builder.append(keys[i - 1]);
                s_builder.append("'");
                if (i + 1 <= depth) {
                    if (keys[i].equals("")) {
                        break;
                    }
                    s_builder.append(" AND ");
                }
            }
        }
        try {
            cql_result = this.resultQuery(s_builder.toString());
        } catch (Exception e) {
            return null;
        }

        rows = cql_result.getRows();
        result = new TreeMap< String, Object>(new TreeMapCompare());
        try {
            for (CqlRow row : rows) {
                value = null;
                working_map = result;
                columns = (ArrayList< Column>) row.getColumns();

                for (int i = 0; i <= columns_size; ++i) {
                    column = columns.get(i);
                    if (i == 0) {
                        value = new String(column.getValue(), "UTF-8");
                        continue;
                    }
                    parent_map = working_map;
                    column_name = new String(column.getValue(), "UTF-8");
                    if (i < columns_size) {
                        if (columns.get(i + 1).getValue().length == 0) {
                            parent_map.put(column_name, value);
                            break;
                        } else {
                            working_map = (TreeMap< String, Object>) parent_map.get(column_name);
                            if (working_map == null) {
                                working_map = new TreeMap< String, Object>(new TreeMapCompare());
                                parent_map.put(column_name, working_map);
                            }
                        }
                    } else {
                        working_map.put(column_name, value);
                    }
                }
            }
        } catch (Exception e) {
            // System.out.println("Error in select (Encoding): " + e.toString());
        }
        return result;
    }

    public final void createTable(final String table_name, final int depth) {
        final StringBuilder s_builder = new StringBuilder();

        s_builder.append("CREATE TABLE ");
        s_builder.append(table_name);
        s_builder.append(" (");
        for (int i = 1; i <= depth; ++i) {
            s_builder.append("c_key");
            s_builder.append(i);
            s_builder.append(" text,");
        }
        s_builder.append("value TEXT, PRIMARY KEY(");
        for (int i = 1; i <= depth; ++i) {
            s_builder.append("c_key");
            s_builder.append(i);
            if (i + 1 <= depth) {
                s_builder.append(",");
            } else {
                s_builder.append("))");
            }
        }
        this.query(s_builder.toString());
    }

    public final void dropTable(final String table_name) {
        final StringBuilder s_builder = new StringBuilder();

        s_builder.append("DROP TABLE ");
        s_builder.append(table_name);
        this.query(s_builder.toString());
    }

    public final void insert(final String table_name, final String value, final String... keys) {
        final int depth = keys.length;
        final String keys_clone[] = keys.clone();

        for (int i = 0; i < depth; ++i) {
            if (keys_clone[i].equals("")) {
                keys_clone[i] = "next_auto_id";
                keys[i] = this.getNextAutoId(table_name, keys_clone);
                break;
            }
        }
        this.insertWithKey(table_name, value, keys);
    }

    public final String insertWithReturnKey(final String table_name, final String value, final String... keys) {
        final int depth = keys.length;
        final String keys_clone[] = keys.clone();

        int save_i = -1;

        for (int i = 0; i < depth; ++i) {
            if (keys_clone[i].equals("")) {
                save_i = i;
                keys_clone[i] = "next_auto_id";
                keys[i] = this.getNextAutoId(table_name, keys_clone);
                break;
            }
        }
        this.insertWithKey(table_name, value, keys);
        if (save_i != -1) {
            return keys[save_i];
        } else {
            return null;
        }
    }

    public final void insertWithKey(final String table_name, final String value, final String... keys) {
        final int depth = keys.length;
        final StringBuilder s_builder = new StringBuilder();

        s_builder.append("INSERT INTO ");
        s_builder.append(table_name);
        s_builder.append("(");
        for (int i = 1; i <= depth; ++i) {
            s_builder.append("c_key");
            s_builder.append(i);
            s_builder.append(",");
        }
        s_builder.append("value) VALUES('");
        for (int i = 1; i <= depth; ++i) {
            s_builder.append(keys[i - 1]);
            s_builder.append("','");
        }
        s_builder.append(value);
        s_builder.append("')");
        // System.out.println("INSERT: " + s_builder.toString());
        this.query(s_builder.toString());
    }

    public final void insert(final String table_name, final List< String> values, final String... keys) {
        final int depth = keys.length;
        final String keys_clone[] = keys.clone();
        final StringBuilder s_builder = new StringBuilder();

        int key_position = 0;

        for (int i = 0; i < depth; ++i) {
            if (keys_clone[i].equals("")) {
                key_position = i;
                keys_clone[i] = "next_auto_id";
                break;
            }
        }

        for (String value : values) {
            s_builder.setLength(0);
            s_builder.append("INSERT INTO ");
            s_builder.append(table_name);
            s_builder.append("(");
            for (int i = 1; i <= depth; ++i) {
                s_builder.append("c_key");
                s_builder.append(i);
                s_builder.append(",");
            }
            s_builder.append("value) VALUES('");
            for (int i = 1; i <= depth; ++i) {
                if (i - 1 == key_position) {
                    keys[i - 1] = this.getNextAutoId(table_name, keys_clone);
                }
                s_builder.append(keys[i - 1]);
                s_builder.append("','");
            }
            s_builder.append(value);
            s_builder.append("')");
            // System.out.println("INSERT: " + s_builder.toString());
            this.query(s_builder.toString());
        }
    }

    public final void insertWithKey(final String table_name, final List<Pair> values, final String... keys) {
        final int depth = keys.length;
        final StringBuilder s_builder = new StringBuilder();

        boolean is_first;

        for (Pair pair : values) {
            is_first = true;
            s_builder.setLength(0);
            s_builder.append("INSERT INTO ");
            s_builder.append(table_name);
            s_builder.append("(");
            for (int i = 1; i <= depth; ++i) {
                s_builder.append("c_key");
                s_builder.append(i);
                s_builder.append(",");
            }
            s_builder.append("value) VALUES('");
            for (int i = 1; i <= depth; ++i) {
                if (keys[i - 1].equals("") && is_first) {
                    s_builder.append(pair.getFirst());
                    is_first = false;
                } else {
                    s_builder.append(keys[i - 1]);
                }
                s_builder.append("','");
            }
            s_builder.append(pair.getSecond());
            s_builder.append("')");
            // System.out.println("INSERT: " + s_builder.toString());
            this.query(s_builder.toString());
        }
    }

    public final String selectSingle(final String table_name, final String... keys) {
        final int depth = keys.length;
        final Column column;
        final StringBuilder s_builder = new StringBuilder();

        CqlResult cql_result = null;

        s_builder.append("SELECT value FROM ");
        s_builder.append(table_name);
        s_builder.append(" WHERE ");
        for (int i = 1; i <= depth; ++i) {
            s_builder.append("c_key");
            s_builder.append(i);
            s_builder.append("='");
            s_builder.append(keys[i - 1]);
            s_builder.append("'");
            if (i + 1 <= depth) {
                s_builder.append(" AND ");
            }
        }
        try {
            cql_result = this.resultQuery(s_builder.toString());
            column = cql_result.getRows().get(0).getColumns().get(0);
            return new String(column.getValue(), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    public final TreeMap< String, Object> select(final String table_name, final String... keys) {
        final int depth = keys.length;
        final CqlResult cql_result;
        final StringBuilder s_builder = new StringBuilder();
        final List< CqlRow> rows;
        final TreeMap< String, Object> result;

        int columns_size = 0;
        String column_name, value;
        Column column;
        TreeMap< String, Object> working_map, parent_map;
        ArrayList< Column> columns;

        s_builder.append("SELECT value");
        for (int i = 1; i <= depth; ++i) {
            if (!keys[i - 1].equals("")) {
                continue;
            }
            s_builder.append(",c_key");
            s_builder.append(i);
            ++columns_size;
        }
        if (columns_size == 0) {
            return null;
        }

        s_builder.append(" FROM ");
        s_builder.append(table_name);
        if (!keys[0].equals("")) {
            s_builder.append(" WHERE ");
            for (int i = 1; i <= depth; ++i) {
                s_builder.append("c_key");
                s_builder.append(i);
                s_builder.append("='");
                s_builder.append(keys[i - 1]);
                s_builder.append("'");
                if (i + 1 <= depth) {
                    if (keys[i].equals("")) {
                        break;
                    }
                    s_builder.append(" AND ");
                }
            }
        }
        // System.out.println(s_builder.toString());
        try {
            cql_result = this.resultQuery(s_builder.toString());
        } catch (Exception e) {
            //System.out.println(s_builder.toString());
            // System.out.println("ERROR IN SELECT: " + e.toString());
            return null;
        }

        rows = cql_result.getRows();
        result = new TreeMap< String, Object>(new TreeMapCompare());
        try {
            for (CqlRow row : rows) {
                value = null;
                working_map = result;
                columns = (ArrayList< Column>) row.getColumns();

                for (int i = 0; i <= columns_size; ++i) {
                    column = columns.get(i);
                    if (i == 0) {
                        value = new String(column.getValue(), "UTF-8");
                        continue;
                    }
                    parent_map = working_map;
                    column_name = new String(column.getValue(), "UTF-8");
                    if (column_name.equals("next_auto_id")) {
                        break;
                    }
                    if (i < columns_size) {
                        if (columns.get(i + 1).getValue().length == 0) {
                            parent_map.put(column_name, value);
                            break;
                        } else {
                            working_map = (TreeMap< String, Object>) parent_map.get(column_name);
                            if (working_map == null) {
                                working_map = new TreeMap< String, Object>(new TreeMapCompare());
                                parent_map.put(column_name, working_map);
                            }
                        }
                    } else {
                        working_map.put(column_name, value);
                    }
                }
            }
        } catch (Exception e) {
            // System.out.println("Error in select (Encoding): " + e.toString());
        }
        return result;
    }

    public final void delete(final String table_name, final boolean propagandation, final String... keys) {
        final int depth = keys.length;

        if (propagandation == false) {
            final StringBuilder s_builder = new StringBuilder();

            s_builder.append("DELETE FROM ");
            s_builder.append(table_name);
            s_builder.append(" WHERE ");
            for (int i = 1; i <= depth; ++i) {
                s_builder.append("c_key");
                s_builder.append(i);
                s_builder.append("='");
                s_builder.append(keys[i - 1]);
                s_builder.append("'");
                if (i + 1 <= depth) {
                    s_builder.append(" AND ");
                }
            }
            //System.out.println(s_builder.toString());
            this.query(s_builder.toString());
        } else {
            int i;
            for (i = 0; i < keys.length; ++i) {
                if (keys[i].equals("")) {
                    break;
                }
            }
            this.deleteTree(table_name, this.selectAllForDelete(table_name, keys), keys, i);
        }
    }

    public final void close() {
        try {
            Database.cassandra_pool.returnObject(this.client);
        } catch (Exception e) {
            //System.out.println("Error in close: " + e.toString());
        }
        this.client = null;
    }
}
