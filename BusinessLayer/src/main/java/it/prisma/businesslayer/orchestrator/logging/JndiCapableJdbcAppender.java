package it.prisma.businesslayer.orchestrator.logging;
/*
 * CURRENTLY NOT USED
 * */

//package org.jbpm.examples.logging;
//
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.sql.DataSource;
//
//import org.apache.log4j.jdbc.JDBCAppender;
//import org.apache.log4j.spi.ErrorCode;
//
///**
// * Extension of the log4j {@link JDBCAppender} to allow using a JNDI data source
// * rather than a direct connection.
// *
// * <p>
// * Example configuration:
// * <pre>
// *   &lt;appender name="database" class="nl.iprofs.blogs.log4j.databaselogging.JndiCapableJdbcAppender"&gt;
// *     &lt;param name="jndiDataSource" value="java:comp/jdbc/datasource" /&gt;
// *     &lt;layout class="org.apache.log4j.PatternLayout"&gt;
// *       &lt;param name="ConversionPattern" value="INSERT INTO logging (createdAt, level, location, message) VALUES ('%d{yyyy-MM-dd HH:mm:ss.sss}','%p','%c{1}','%m')" /&gt;
// *     &lt;/layout&gt;
// *   &lt;/appender&gt;
// * </pre>
// *
// * @author Bart Bakker
// */
//public class JndiCapableJdbcAppender extends JDBCAppender {
//
//    private static final Pattern SQL_VALUE_PATTERN = Pattern.compile("'(.*?)'(?=\\s*[,)])", Pattern.MULTILINE);
//    private String jndiDataSource;
//
//    /** {@inheritDoc} */
//    @Override
//    protected Connection getConnection() throws SQLException {
//        if (jndiDataSource == null) {
//            return super.getConnection();
//        } else {
//            return lookupDataSource().getConnection();
//        }
//    }
//
//    /**
//     * Looks up the datasource in the naming context specified by the
//     * {@link #jndiDataSource}.
//     *
//     * @return the datasource.
//     */
//    private DataSource lookupDataSource() {
//        try {
//            Context context = new InitialContext();
//            return (DataSource) context.lookup(jndiDataSource);
//        } catch (NamingException e) {
//            throw new RuntimeException("Cannot find JNDI DataSource: " + jndiDataSource, e);
//        }
//    }
//
//    /** {@inheritDoc} */
//    @Override
//    protected void closeConnection(Connection con) {
//        try {
//            con.close();
//        } catch (SQLException e) {
//            errorHandler.error("Failed to close connection", e, ErrorCode.CLOSE_FAILURE);
//        }
//    }
//
//    /**
//     * Executes the specified SQL statement, by parsing its values and turning
//     * them into parameters, so that characters that must be escaped in a SQL
//     * statement are supported.
//     */
//    @Override
//    protected void execute(String sql) throws SQLException {
//        String statement = sql;
//        ArrayList<String> args = new ArrayList<String>();
//        Matcher m = SQL_VALUE_PATTERN.matcher(sql);
//        while (m.find()) {
//            args.add(m.group(1));
//            statement = statement.replace(m.group(), "?");
//        }
//
//        executeStatement(statement, args.toArray(new String[args.size()]));
//    }
//
//    /**
//     * Executes the statement settings its parameters to the specified arguments.
//     * 
//     * @param statement
//     *            the statement to execute.
//     * @param args
//     *            the parameter values.
//     */
//    protected void executeStatement(String statement, String[] args) throws SQLException {
//        Connection con = getConnection();
//        PreparedStatement stmt = null;
//        try {
//            stmt = con.prepareStatement(statement);
//            for (int i = 0; i < args.length; i++) {
//                stmt.setString(i + 1, args[i]);
//            }
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            if (stmt != null) {
//                stmt.close();
//            }
//            throw e;
//        }
//        stmt.close();
//        closeConnection(con);
//    }
//
//    public String getJndiDataSource() {
//        return jndiDataSource;
//    }
//
//    public void setJndiDataSource(String jndiDataSource) {
//        this.jndiDataSource = jndiDataSource;
//    }
//}