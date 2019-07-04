package edu.Ciesla.models;

import edu.Ciesla.services.HService;
import edu.Ciesla.services.TemperatureGetter;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Location", uniqueConstraints=@UniqueConstraint(columnNames = {"id"}))
public class Location{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JoinColumn(name = "id", unique=true, nullable = false)
    private int id;
    @Column(name = "lat")
    private double lat;
    @Column(name = "lon")
    private double lon;
    @Column(name = "city_name")
    private String city_name;
    @Column(name = "frequency")
    private double frequency;
    //@OneToMany(mappedBy = "location",fetch = FetchType.EAGER)
    //@JoinColumn(name = "weather")
    //private Set<Weather> weathers = new HashSet<Weather>();



    public Location(){};
    public Location(double lon, double lat, double freq){
        this.lat=lat;
        this.lon=lon;
        this.frequency=freq;
    }
    public Location(String city_name, double freq){
        this.city_name=city_name;
        this.frequency=freq;
    }



    public int getId() {
        return id;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public String getCity_name() {
        return city_name;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
    /*
    public void setWeathers(Set<Weather> weathers) {
        this.weathers = weathers;
    }
    public Set<Weather> getWeathers() {
        return weathers;
    }*/
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
}
//, referencedColumnName = "id"