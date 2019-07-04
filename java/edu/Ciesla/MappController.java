package edu.Ciesla;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import edu.Ciesla.models.Location;
import edu.Ciesla.models.Weather;
import edu.Ciesla.models.WeatherComparator;
import edu.Ciesla.services.HService;
import edu.Ciesla.services.TemperatureGetter;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@RestController
public class MappController {
    private ObjectMapper objectMapper;
    //HService services;
    private Trigger trigger;
    private TemperatureGetter tg;
    StdSchedulerFactory factory;
    //private Scheduler scheduler = null;

    MappController() {
        objectMapper = new ObjectMapper();
        List<Location> list = HService.getAllLocations();
        for (Location l : list) {
            schedule(l);
        }
    }

    @RequestMapping(value = "/api/register/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addLocation(@RequestParam String name){

        return new ResponseEntity<>("", HttpStatus.ACCEPTED);
    }
    @RequestMapping(value = "/api/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addLocation(@RequestParam double lat, @RequestParam double lon, @RequestParam double freq) {
        Location l = new Location(lon, lat, freq);
        HService.add(l);
        schedule(l);
        ObjectWriter w = objectMapper.writer();
        try {
            return new ResponseEntity<>(w.writeValueAsString(l), HttpStatus.ACCEPTED);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JsonProcessingException has appeared.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/api/stat/{ID}/{n}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> temOf(@PathVariable int ID, @PathVariable int n) {
        ArrayList<Weather> weathers = HService.getWeathersOf(ID);
        Collections.sort(weathers);
        Collections.reverse(weathers);
        double returnVale =0;
        int i=0;
        for(; i<n && i<weathers.size();i++){
            returnVale+=weathers.get(i).getTemperature();
        }
        return new ResponseEntity<>("temp of "+i+" last surveys: "+(returnVale/i), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/api/temp/{ID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkTemp(@PathVariable int ID) {
        double n = HService.checkTemperature(HService.getLocation(ID));
        return new ResponseEntity<>("" + n, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/api/updateNow/{ID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateNow(@PathVariable int ID) {
        double n = HService.checkTemperature(HService.getLocation(ID));
        return new ResponseEntity<>("" + n, HttpStatus.ACCEPTED);
    }

    private void schedule(Location l){
        JobDetail jd = JobBuilder.newJob().ofType(TemperatureGetter.class)
                .storeDurably()
                .withIdentity(l.getId() + "")
                .withDescription("" + l.getId())
                .build();
        try {
            factory = new StdSchedulerFactory();
            trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("" + l.getId(), "" + l.getId())
                    .withDescription("" + l.getId())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds((int) (1200 * l.getFrequency()))
                            .repeatForever())
                    .build();
            Scheduler scheduler = factory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(jd, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}



/*





 */


/*
trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("get temp of: ", ""+l.getId())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds((int)(1*freq)).repeatForever())
                .build();
        try{
            Scheduler scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            JobDetail jd = JobBuilder.newJob().ofType(TemperatureGetter.class)
                    .storeDurably()
                    .withIdentity("Details of getting temp")
                    .withDescription("description of Job")
                    .build();
            scheduler.scheduleJob(jd,trigger);
            //scheduler.scheduleJob(tg, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }



 */







































