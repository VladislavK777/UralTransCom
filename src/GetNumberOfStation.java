
/*
*
* Интерфейс получения Кодов станций
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
*/

public interface GetNumberOfStation {
    String codeOfStation(String name);
    String getStringQueryOfRoute(String nameOfStation1, String nameOfStation2);
}
