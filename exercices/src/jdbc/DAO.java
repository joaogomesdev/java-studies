package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO {

	private Connection connection;

	public int insert(String query, Object... atributes) {

		try {
			
			PreparedStatement stmt = getConnetion().prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			addAttributes(stmt, atributes);
			
			if(stmt.executeUpdate() > 0) {
				ResultSet result = stmt.getGeneratedKeys();
				
				if(result.next()) {
					return result.getInt(1);
				}
			}
			
			return -1;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void addAttributes(PreparedStatement stmt, Object[] attributes) throws SQLException {
		int index = 1;
		
		for(Object attribute: attributes) {
			
			if(attribute instanceof String) {
				stmt.setString(index, (String) attribute);
			}else if (attribute instanceof Integer) {
				stmt.setInt(index, (Integer) attribute);
			}
			
			index++;
		}
	}

	private Connection getConnetion() {
		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}
		} catch (SQLException e) {

		}

		connection = ConnectionFactory.getConnection();
		return connection;
	}
	
	public void closeConnection() {
		try {
			getConnetion().close();
		} catch (SQLException e) {
			
		} finally {
			connection = null;
		}
	}
}
