// 관계형 데이터베이스에 접근하기

import java.sql.*;

try (
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:");
	Statement stat = conn.createStatement();
	ResultSet rs = stat.executeQuery("SELECT 2 + 2 AS total")
) {
    if (rs.next()) {
	System.out.println(rs.getInt("total"));
	assert rs.getInt("total") == 4;
    }
}
