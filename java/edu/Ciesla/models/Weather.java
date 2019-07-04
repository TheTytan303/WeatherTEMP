package edu.Ciesla.models;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Table(name = "Weather", uniqueConstraints=@UniqueConstraint(columnNames = {"id"}))
public class Weather implements Comparable<Weather> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JoinColumn(name = "id", unique=true, nullable = false)
    private int id;
    @Column(name = "temperature")
    private double temperature;
    @ManyToOne
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;

    public Weather(Location l, double temp){
        this.location=l;
        this.temperature = temp;
    }
    public Weather(){
    }

    public int getId() {
        return id;
    }

    public double getTemperature() {
        return temperature;
    }

    public Location getLocation() {
        return location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTemperature(double lon) {
        this.temperature = lon;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int compareTo(Weather w){
        return Integer.compare(this.getId(), w.getId());
    }
}
