package org.spo.svc2.trx.pgs.mh.controller;

import java.lang.reflect.Type;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spo.cms.model.QMessage;
import org.spo.cms.svc.PageService;
import org.spo.cms.svc.SocketConnector;
import org.spo.ifs2.template.web.Constants;
import org.spo.ifs2.template.EchoService;
import org.spo.svc2.trx.pgs.mc.cmd.PostContent;
import org.spo.svc2.trx.pgs.mh.cmd.M_Home_01;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Handles requests for the application home page.
 */
//@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory
            .getLogger(HomeController.class);

    @Autowired
    private EchoService echoService = null;
 

	@Autowired
	public PageService svc ;
	private SocketConnector connector=new SocketConnector();
   // @Autowired
    //private JmsQueueSender sender;
    /**
     * Simple controller for "/" that returns a Thymeleaf view.
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
       
        return "redirect:trx/M01/LA01T";
    }

    @RequestMapping(value="/home/{contentId}", method = RequestMethod.GET)
	 public String fetchPost(    final PostContent content, final BindingResult bindingResult, final ModelMap model,
			 @PathVariable String contentId) {
		 if (bindingResult.hasErrors()) {
			 return "seedstartermng";
		 }

		 System.out.println(content.getHtmlContent());
		 logger.info("Searching "+contentId  );

		  QMessage message = new QMessage();
			message.setHandler("pages");
			message.setContent("M_Home_1/f01/null");
			String response ="";
			try {		
				response = svc.readUpPage("templates", contentId);
			} catch (Exception e) {			
				e.printStackTrace();
			}
			try{
				Gson gson = new Gson();
				Type typ = new TypeToken<M_Home_01>(){}.getType();//FIXME right now only string works
				M_Home_01 cmd= gson.fromJson(response,typ);		
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
				model.addAttribute("message",cmd);
				System.out.println(cmd.toString());
				
			}catch(Exception e){
				System.out.println("Error during messagePayload processing from  TestResourceServerException on" );
				e.printStackTrace();
			}
	        
	        return "index1";
	 }
	 
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Locale locale, Model model) {
    	
    	  return "redirect:trx/M01/LA01T";
    }
    
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about(Locale locale, final ModelMap info) {

    	PostContent content = new PostContent();
		 String response = svc.readUpPage("posts", "M_About");
		 response=response.equals("")?"<p>blank reply</p>":response;
		 info.clear();
		 content.setHtmlContent(response);
		 info.addAttribute("message", content);
        return "lc/about";
    }
    @RequestMapping(value = "/contactold", method = RequestMethod.GET)
    public String contact(Locale locale, Model model) {
        logger.info("Welcome home! the client locale is " + locale.toString());

        return "lc/contact";
    }
    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public String post(Locale locale, Model model) {
        logger.info("Welcome home! the client locale is " + locale.toString());

        return "post";
    }
  
}
