

package domein;

import java.sql.Connection;
import java.sql.DriverManager;

public class Mapper {

public void getSpelers(){
	try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL)) {
		
	}
	catch(Exception ex) {}
}
	
}
