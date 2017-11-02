import java.sql.*;

public class GetNumberOfStationImpl extends VaribalesForRestAPI implements GetNumberOfStation {

    private static final String url = "jdbc:mysql://localhost:3306/restapi?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String pass = "root";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    @Override
    public String codeOfStation(String name) {
        String query = "select s.station_key from stations s where s.station_name = ?";
        /*try {
            Call<ResponseBody> result = api.execSomeMethod2(name, "get_station2");
            Response response = result.execute();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((ResponseBody) response.body()).bytes());
            InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readed;
            while ((readed = bufferedReader.readLine()) != null) {
                split = readed.split(" ");
                if (split[1].trim().equals(name)) {
                    return split[0].trim();
                }
                try (FileWriter fileWriter = new FileWriter(new File("c:\\Users\\Vladislav.Klochkov\\Desktop\\JavaTest\\codeofstations.txt"), true)) {

                    fileWriter.write(split[0] + " ");
                    fileWriter.write(split[1] + " ");
                    fileWriter.write(split[2] + " ");
                    fileWriter.write(split[3] + " ");
                    fileWriter.write(split[4] + "\r\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String station = new String();
        PreparedStatement pstmt = null;
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, pass);

            // getting Statement object to execute query
            stmt = con.createStatement();

            pstmt = con.prepareStatement("select s.station_key from stations s where s.station_name = ?");
// Определяем значения параметров
            pstmt.setString(1, name);

            // executing SELECT query
            rs = pstmt.executeQuery();


            while (rs.next()) {
                station = rs.getString(1);
                //System.out.println("Total number of books in the table : " + station);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            //close connection ,stmt and resultset here
            try {
                con.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                rs.close();
            } catch (SQLException se) { /*can't do anything */ }
            try {
                pstmt.close();
            } catch (SQLException se) { /*can't do anything */ }
        }

        return station;
    }

    @Override
    public String getStringQueryOfRoute(String nameOfStation1, String nameOfStation2) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(codeOfStation(nameOfStation1));
        stringBuilder.append(";");
        stringBuilder.append(codeOfStation(nameOfStation2));

        return stringBuilder.toString();
    }


}
