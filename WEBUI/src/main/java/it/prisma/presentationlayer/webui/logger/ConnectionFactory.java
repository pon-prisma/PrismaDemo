package it.prisma.presentationlayer.webui.logger;

import it.prisma.presentationlayer.webui.Application;
import it.prisma.presentationlayer.webui.configs.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ConnectionFactory {

	@Autowired
	private org.springframework.core.env.Environment env;

	@Value("${environment}")
	private String environment;

	@Value("${environment.${environment}.database.url}")
	private String URL;
	@Value("${environment.${environment}.database.username}")
	private String username;
	@Value("${environment.${environment}.database.password}")
	private String password;

	String loggerDatabaseURL;

	private static interface Singleton {
		final ConnectionFactory INSTANCE = new ConnectionFactory();
	}

	private DataSource dataSource;

	private ConnectionFactory() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}

//		Properties prop = new Properties();
//		InputStream input = null;
//
//		try {
//			String filename = "application.properties";
//			input = Application.class.getClassLoader().getResourceAsStream(filename);
//			if (input == null) {
//				System.exit(0);
//			}
//			prop.load(input);	
//
//		} catch (IOException ex) {
//			ex.printStackTrace();
//			System.exit(0);
//		} finally {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//					System.exit(0);
//				}
//			}
//		}

		Properties properties = new Properties();
		
		loggerDatabaseURL = URL;
		properties.setProperty("user", username);
		properties.setProperty("password", password);

		GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
		DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(loggerDatabaseURL, properties);
		new PoolableConnectionFactory(connectionFactory, pool, null, "SELECT 1", 3, false, false, Connection.TRANSACTION_READ_COMMITTED);
		this.dataSource = new PoolingDataSource(pool);
		
	}

	public static Connection getDatabaseConnection() throws SQLException {
		return Singleton.INSTANCE.dataSource.getConnection();
	}
}