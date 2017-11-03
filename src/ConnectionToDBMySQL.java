/*
*
* Класс соединения с БД
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
*/

public class ConnectionToDBMySQL {

    // Определяем параметры БД
    private static final String url = "jdbc:mysql://localhost:3306/restapi?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String pass = "root";

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }
}
