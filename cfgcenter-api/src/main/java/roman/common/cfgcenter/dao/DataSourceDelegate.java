package roman.common.cfgcenter.dao;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.logging.Logger;

public class DataSourceDelegate implements DataSource {

    private DataSource delagate;

    public DataSourceDelegate(DataSource delagate) {
        Objects.requireNonNull(delagate);
        this.delagate = delagate;
    }

    public void setDelagate(DataSource delagate) {
        Objects.requireNonNull(delagate);
        this.delagate = delagate;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return delagate.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return delagate.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delagate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delagate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delagate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delagate.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delagate.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delagate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delagate.isWrapperFor(iface);
    }
}
