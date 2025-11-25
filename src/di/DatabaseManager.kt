package di

import java.sql.Connection
import java.sql.DriverManager

object DatabaseManager {
    private const val DB_URL = "jdbc:h2:./db/data;AUTO_SERVER=TRUE"
    private var connection: Connection? = null

    fun connect(): Connection {
        if (connection == null || connection!!.isClosed) {
            Class.forName("org.h2.Driver")
            connection = DriverManager.getConnection(DB_URL)
        }
        return connection!!
    }

    fun close() {
        connection?.close()
    }
}