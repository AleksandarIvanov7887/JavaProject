package project.issue.tracker.database.db;

import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

class CassandraFactory extends BasePoolableObjectFactory<Client> {

	private int port;
	private String host;
	static CassandraFactory instance = null;

	public static CassandraFactory getInstance(final String host, final int port) {
		if (instance == null) {
			if (instance == null) {
				instance = new CassandraFactory(host, port);
			}
		}
		return instance;
	}

	private CassandraFactory(final String host_, final int port_) {
		this.host = host_;
		this.port = port_;
	}

	@Override
	public Client makeObject() throws Exception { // INPUT AND OUTPUT TRANSPORT
													// IS ONE SINGLE OBJECT !
		// System.out.println("MAKE");
		TTransport transport = new TFramedTransport(new TSocket(host, port));
		TProtocol protocol = new TBinaryProtocol(transport);
		return new Client(protocol);
	}

	@Override
	public void activateObject(Client client) throws Exception {
		// System.out.println("ACTIVATE");
		TTransport transport = client.getInputProtocol().getTransport();
		transport.open();
		super.activateObject(client);
	}

	@Override
	public void passivateObject(Client client) throws Exception {
		// System.out.println("PASSIVE");
		TTransport transport = client.getInputProtocol().getTransport();
		transport.flush();
		transport.close();
		super.passivateObject(client);
	}

	@Override
	public void destroyObject(Client client) throws Exception {
		// System.out.println("DESTROY");
		TTransport transport = client.getInputProtocol().getTransport();
		transport.flush();
		transport.close();
		super.destroyObject(client);
	}
}
