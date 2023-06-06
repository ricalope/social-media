package com.ricalope.tweeter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException{
    private static final long serialVersionUID = 6528930232146124623L;
    private String message;
}
