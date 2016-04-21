package org.spo.svc2.trx.pgs.c01.handler;

import org.spo.ifs2.dsl.controller.AbstractHandler;
import org.spo.ifs2.dsl.controller.DSLConstants.EventType;
import org.spo.ifs2.dsl.controller.NavEvent;
import org.spo.svc2.trx.pgs.c01.task.C0101;
import org.spo.svc2.trx.pgs.c01.task.C0102;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class C01Handler extends AbstractHandler{

   
   @Autowired
   C0101 c0101;
   
   @Autowired
   C0102 c0102;
   
   public static final NavEvent EV_INIT_01 =  NavEvent.create(EventType.REFRESHPAGE);
   public static final NavEvent EV_INIT_02 =  NavEvent.create(EventType.REFRESHPAGE, "m01/M0102");
   public static final NavEvent EV_SWITCH_TO_DTL =  NavEvent.create(EventType.TASKSET, "02");
   public static final NavEvent EV_REFRESH_CONTENT =  NavEvent.create(EventType.TASKSET, "01");
  
  
   @Override
   public void configureChannel() {
			taskChannel.put("01",c0101);
			taskChannel.put("02",c0102);
   }
   
   
  
    
	
	
	
	
	
	
}
