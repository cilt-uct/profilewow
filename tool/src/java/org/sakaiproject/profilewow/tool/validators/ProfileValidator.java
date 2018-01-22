package org.sakaiproject.profilewow.tool.validators;

import org.sakaiproject.profilewow.tool.facade.SakaiPersonFacade;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfileValidator implements Validator {


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
