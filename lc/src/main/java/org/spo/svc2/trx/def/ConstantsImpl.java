package org.spo.svc2.trx.def;

import org.spo.ifs2.template.web.Constants;
import org.springframework.stereotype.Component;
@Component
public class ConstantsImpl implements Constants {

	public String getRepoPath() {
		if(!System.getProperty("os.name").startsWith("Windows")){
			return "/usr/local/share/data-cms/lc";
		}else{
			return "C:\\Users\\premganesh\\git\\bg1_3\\src\\main\\resources\\data-cms";
		}
	}

	public String getLandingPage() {
		return "trx/M01/LA01T";
	}

	public int getPortNumber() {
		// TODO Auto-generated method stub
		return 8089;
	}

}

