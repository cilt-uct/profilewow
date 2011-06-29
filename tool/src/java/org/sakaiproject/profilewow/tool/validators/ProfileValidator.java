package org.sakaiproject.profilewow.tool.validators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.profilewow.tool.facade.SakaiPersonFacade;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProfileValidator implements Validator {

	protected final Log log = LogFactory.getLog(ProfileValidator.class);
	public boolean supports(Class arg0) {
		// TODO Auto-generated method stub
		return arg0.equals(SakaiPersonFacade.class);
	}

	public void validate(Object object, Errors errors) {
		// TODO Auto-generated method stub
		SakaiPersonFacade facade = (SakaiPersonFacade)object;
		if (facade.getSakaiPerson() == null) {
			errors.reject("profile.null", "empty profile");
			return;
		}

		//SakaiPerson person = facade.getSakaiPerson();

		
		
		

	}




}
