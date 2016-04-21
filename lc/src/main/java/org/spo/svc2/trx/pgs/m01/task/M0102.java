package org.spo.svc2.trx.pgs.m01.task;

import java.lang.reflect.Type;

import org.spo.cms.svc.PageService;
import org.spo.ifs2.dsl.controller.NavEvent;
import org.spo.ifs2.dsl.controller.StateInfo;
import org.spo.ifs2.dsl.controller.TrxInfo;
import org.spo.ifs2.dsl.model.AbstractTask;
import org.spo.ifs2.template.web.Constants;
import org.spo.svc2.trx.pgs.c01.cmd.CA01T;
import org.spo.svc2.trx.pgs.m01.cmd.LA01T;
import org.spo.svc2.trx.pgs.m01.handler.M01Handler;
import org.spo.svc2.trx.pgs.mc.cmd.PostContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
@Component
public class M0102 extends AbstractTask {

	@Autowired
	public PageService svc ;
	@Override
	public NavEvent initView(TrxInfo info) {
		return null;
	}

	@Override
	public NavEvent initTask(String dataId, TrxInfo info) throws Exception {


		String contentId= dataId;
		String response="";
		String response_content="";

		response = svc.readUpPage("templates", contentId);

		String dataId_Content="" ;
		if(dataId.equals("LB01T")){
			//regular Menu can be mapped with A01T content
			dataId_Content = "BA01T";
		}

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

		return M01Handler.EV_INIT_02;
	
	}

	@Override
	public NavEvent processViewEvent(String event, TrxInfo info) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String processAjaxEvent(String event, TrxInfo info) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
