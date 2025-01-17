package org.example.database.model;

import com.google.gson.JsonObject;
import jakarta.persistence.*;
import org.example.database.converter.JsonObjectConverter;

import java.util.Objects;

@Entity
@Table(name = "location", schema = "public")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_id_gen")
    @SequenceGenerator(name = "location_id_gen", sequenceName = "Location_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Convert(converter = JsonObjectConverter.class)
    @Column(name = "weather_data", columnDefinition = "json not null")
    private JsonObject weatherData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public JsonObject getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(JsonObject weatherData) {
        this.weatherData = weatherData;
    }

    @Override
    public String toString() {
        return "LocationEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", weatherData='" + weatherData + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LocationEntity that = (LocationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude) && Objects.equals(weatherData, that.weatherData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, latitude, longitude, weatherData);
    }
}