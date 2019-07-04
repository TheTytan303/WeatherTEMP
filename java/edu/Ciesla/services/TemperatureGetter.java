package edu.Ciesla.services;

import edu.Ciesla.models.Location;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class TemperatureGetter implements Job {

    private HService service;

    public TemperatureGetter(){}

    public void execute(JobExecutionContext context) {
        //service.checkTemperature(location);
        //service = (HService)context.getScheduler().getContext().get("service");
        int id = Integer.parseInt(context.getJobDetail().getKey().getName());
        System.out.println("checking tmp for; "+ id);
        Location l = HService.getLocation(id);
        if(l != null){
            HService.checkTemperature(l);
        }
        else {
            System.out.println("NULL!");
        }


        //HService.checkTemperature(HService.getLocation(id));

    }
}
