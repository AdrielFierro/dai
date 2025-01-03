package com.tpo.TPO.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "You're not the owner of the entity")
public class NoMatchUserException extends Exception {

}
