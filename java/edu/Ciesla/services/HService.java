package edu.Ciesla.services;

import com.squareup.okhttp.OkHttpClient;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import edu.Ciesla.models.Location;
import edu.Ciesla.models.Weather;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HService {
    private static String APPID = "742603ccf5d2c56c8637a809b62faa79";
    private static String UNITS = "metric";
    //TemperatureGetter gt = new TemperatureGetter();


    public static Location add(Location l){
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try{
            tx = session.beginTransaction();
            session.save(l);
            tx.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
        return l;
    }
    public static Weather add(Weather weather){
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try{
            tx = session.beginTransaction();
            session.save(weather);
            tx.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
        return weather;
    }
    public static Location getLocation(int id){
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try{

            tx = session.beginTransaction();
            Location returnVale = session.get(Location.class, id);
            tx.commit();
            return returnVale;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
        return null;
    }
    public static List<Location> getAllLocations(){
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try{

            tx = session.beginTransaction();
            List<Location> returnVale = session.createCriteria(Location.class).list();
            tx.commit();
            return returnVale;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
        return null;
    }
    public static double checkTemperature(Location l){
        String address = "http://api.openweathermap.org/data/2.5/weather?lat="+l.getLat()+"&lon="+l.getLon()+"&units="+UNITS+"&appid="+APPID;
        double returnVale = deJsonTemp(sendRequest(address));
        add(new Weather(l, returnVale));
        return returnVale;
    }
    private static double deJsonTemp(String JSONstring){
        String[] s = JSONstring.split("temp");
        s[1] = s[1].split(",")[0];
        String returnVale = s[1].substring(2);
        return Double.parseDouble(returnVale);
    }
    public static String sendRequest (String link){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(link).build();
        try{
            Response response = client.newCall(request).execute();
            assert response.body() != null;
            return response.body().string();
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }
    public static ArrayList<Weather> getWeathersOf(int ID){
        ArrayList<Weather> returnVale = getCommands("SELECT * FROM `weather` WHERE location=" + ID);
        return returnVale;
    }

    private static ArrayList<Weather> getCommands(String sql){
        Transaction tx;
        ArrayList<Weather> returnVale = new ArrayList<>();
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity(Weather.class);
            returnVale.addAll(query.list());
            tx.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
        return returnVale;
    }
    //public void(){
    //
    //}
}
/*
    public Weather addWeather(Weather weather){
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try{
            tx = session.beginTransaction();
            session.save(weather);
            tx.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
        return weather;
    }
*/