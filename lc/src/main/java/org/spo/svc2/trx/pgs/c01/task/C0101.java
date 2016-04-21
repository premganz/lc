package org.spo.svc2.trx.pgs.c01.task;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spo.cms.svc.PageService;
import org.spo.cms.svc.SocketConnector;
import org.spo.ifs2.dsl.controller.NavEvent;
import org.spo.ifs2.dsl.controller.TrxInfo;
import org.spo.ifs2.dsl.model.AbstractTask;
import org.spo.ifs2.template.web.Constants;
import org.spo.svc2.trx.pgs.c01.cmd.CA01T;
import org.spo.svc2.trx.pgs.c01.handler.C01Handler;
import org.spo.svc2.trx.pgs.m01.cmd.LA01T;
import org.spo.svc2.trx.pgs.mc.cmd.PostContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Component
public class C0101 extends AbstractTask {



	private static final Logger logger = LoggerFactory.getLogger(C0101.class);
	
	@Autowired
	public PageService svc ;
	
	private SocketConnector connector=new SocketConnector();
	
	@Override
	public NavEvent initTask(String dataId, TrxInfo info) {

		String contentId= dataId;
		String response="";
		String response_content="";

		response = svc.readUpPage("templates", contentId);

		String dataId_Menu="LA01T" ;//MEnu responsive
		
		String dataId_Content = dataId;

		
		response = svc.readUpPage("templates", dataId_Menu);
		response_content = svc.readUpPage("templates", dataId_Content);

		try{
			Gson gson = new Gson();
			Type typ = new TypeToken<LA01T>(){}.getType();//FIXME right now only string works
			LA01T cmd_menu= gson.fromJson(response,typ);		

			typ = new TypeToken<CA01T>(){}.getType();//FIXME right now only string works
			CA01T cmd= gson.fromJson(response_content,typ);		
			if(cmd.getPage_content_type_cd().equals("1")){
				String contentId1 = cmd.getPage_content_text();
				response = svc.readUpPage("posts", contentId1);
				String response_meta = svc.readUpPage("posts", contentId1+"_meta");
				response=response.equals("")?"<p>blank reply</p>":response;				
				cmd.setPage_content_text(response);	
				PostContent contentObj = new PostContent();
				contentObj.setHtmlContent(response);
				contentObj.setMeta(response_meta);
				cmd.setPage_content_meta(response_meta);
				cmd.setContentObject(contentObj);
			}
			info.addToModelMap("menu",cmd_menu);
			info.addToModelMap("message",cmd);
			System.out.println(cmd.toString());

		}catch(Exception e){
			System.out.println("Error during messagePayload processing from  TestResourceServerException on" );
			e.printStackTrace();
		}
		return C01Handler.EV_INIT_01;
	}

	@Override
	public NavEvent processViewEvent(String event, TrxInfo info) {
		if(event.startsWith("EV_DTL")){
			String dataId = event.replaceAll("EV_DTL_","");
				NavEvent navEvent = C01Handler.EV_REFRESH_CONTENT;
				navEvent.dataId=dataId;				
				return navEvent;
		}
		else if(event.startsWith("EV_ABOUT")){			
			NavEvent navEvent = C01Handler.EV_REFRESH_CONTENT;
			navEvent.dataId="B01T";
			return navEvent;
		}
		return C01Handler.EV_INIT_01;
	}

	
	
	
	
	@Override
	public String processAjaxEvent(String event, TrxInfo info) {
		return "";
	}

	public SocketConnector getConnector() {
		return connector;
	}

	public void setConnector(SocketConnector connector) {
		this.connector = connector;
	}

	@Override
	public NavEvent initView(TrxInfo info) {
		// TODO Auto-generated method stub
		return null;
	}




}
