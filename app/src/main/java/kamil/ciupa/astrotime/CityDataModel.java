package kamil.ciupa.astrotime;

/**
 * Created by Kamil on 2017-06-21.
 */

public class CityDataModel {

    public CityDataModel(){

    }

    public CityDataModel(String cityName){
        this.cityName = cityName;
    }

    public CityDataModel(long id, String cityName) {
        this.id = id;
        this.cityName = cityName;
    }
    private long id;
    private String cityName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }




}
