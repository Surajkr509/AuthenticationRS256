package com.aio_player.utils;

import java.util.ArrayList;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.stereotype.Component;

import com.aio_player.model.Players;



@Component
public class BeanValidator {
	
	public Validator getValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}

	public ArrayList<String> userSignupValidate(Players reqData) {
		ArrayList<String> arrayList = new ArrayList<>();
		Set<ConstraintViolation<Players>> constraintViolations = getValidator().validate(reqData);
		for (ConstraintViolation<Players> constraintViolation : constraintViolations) {
			if (constraintViolation.getPropertyPath().toString().equals("name")) {
				arrayList.add(constraintViolation.getMessage());
			}
			if (constraintViolation.getPropertyPath().toString().equals("email")) {
				arrayList.add(constraintViolation.getMessage());
			}
			if (constraintViolation.getPropertyPath().toString().equals("mobNo")) {
				arrayList.add(constraintViolation.getMessage());
			}
		}
		return arrayList;
	}
	

}
