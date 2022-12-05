package br.gov.jfrj.siga.matrix.sinc;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import com.google.common.base.CaseFormat;

import br.gov.jfrj.siga.base.Prop;

public class JdbcSimpleOrm implements Closeable {
    private Connection con;

    public JdbcSimpleOrm() throws Exception {
        con = getConnection();
        con.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        con.commit();
    }

    public void rollback() throws SQLException {
        con.rollback();
    }

    public Connection getConnection() throws Exception {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:");
            String dsName = Prop.get("matrix.datasource.name");
            DataSource ds = (DataSource) envContext.lookup(dsName);
            Connection connection = ds.getConnection();
            if (connection == null)
                throw new Exception("Can't open connection to matrix database.");
            return connection;
        } catch (NameNotFoundException nnfe) {
            Connection connection = null;

            Class.forName("oracle.jdbc.OracleDriver");

            String dbURL = Prop.get("matrix.datasource.url");
            String username = Prop.get("matrix.datasource.username");
            String password = Prop.get("matrix.datasource.password");
            connection = DriverManager.getConnection(dbURL, username, password);
            if (connection == null)
                throw new Exception("Can't open connection to Oracle.");
            PreparedStatement pstmt = null;
            try {
                pstmt = connection.prepareStatement(
                        "alter session set current_schema=" + Prop.get("matrix.datasource.schema"));
                pstmt.execute();
            } finally {
                if (pstmt != null)
                    pstmt.close();
            }
            return connection;
        }
    }

    public <T> List<T> loadAll(Class<T> clazz, String where) throws Exception {
        List<T> l = new ArrayList<>();

        Connection con = null;
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            String query = "SELECT * FROM "
                    + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, clazz.getSimpleName())
                    + (where == null ? "" : " " + where);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                T i = clazz.newInstance();
                for (Field f : clazz.getDeclaredFields()) {
                    f.setAccessible(true);
                    f.set(i, rs.getString(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, f.getName())));
                }
                l.add(i);
            }
            rs.close();
            con.close();
            stmt.close();
        } finally {
            if (stmt != null)
                stmt.close();
        }
        return l;
    }

    public <T> void insert(T i) throws Exception {
        List<Field> fields = fields(i);

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO " + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, i.getClass().getSimpleName())
                + " (");
        {
            boolean first = true;
            for (Field f : fields) {
                if (first)
                    first = false;
                else
                    sql.append(", ");
                sql.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, f.getName()));
            }
        }
        sql.append(") VALUES (");
        {
            boolean first = true;
            for (Field f : fields) {
                if (first)
                    first = false;
                else
                    sql.append(",");
                sql.append("?");
            }
        }
        sql.append(")");

        PreparedStatement ps = con.prepareStatement(sql.toString());
        int index = 0;
        for (Field f : fields) {
            index++;
            ps.setString(index, (String) f.get(i));
        }
        ps.execute();
    }

    public <T> void update(T i) throws Exception {
        List<Field> fields = fields(i);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE " + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, i.getClass().getSimpleName())
                + " SET ");
        {
            boolean first = true;
            for (Field f : fields) {
                f.setAccessible(true);
                if (first)
                    first = false;
                else
                    sql.append(", ");
                sql.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, f.getName()));
                Object val = f.get(i);
                sql.append(" = ?");
            }
        }
        sql.append(" WHERE " + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fields.get(0).getName())
                + " = ?");

        PreparedStatement ps = con.prepareStatement(sql.toString());
        int index = 1;
        for (Field f : fields) {
            ps.setString(index, (String) f.get(i));
            index++;
        }
        ps.setString(index, (String) fields.get(0).get(i));
        ps.execute();
    }

    public <T> void remove(T i) throws Exception {
        List<Field> fields = fields(i);

        StringBuilder sql = new StringBuilder();
        sql.append("DELETE " + CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, i.getClass().getSimpleName())
                + " WHERE " + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, fields.get(0).getName())
                + " = ?");

        PreparedStatement ps = con.prepareStatement(sql.toString());
        ps.setString(1, (String) fields.get(0).get(i));
        ps.execute();
    }

    private <T> List<Field> fields(T i) {
        return Arrays.stream(i.getClass().getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .collect(Collectors.toList());
    }

    @Override
    public void close() throws IOException {
        if (con != null)
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

}
