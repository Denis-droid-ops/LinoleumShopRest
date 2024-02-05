package com.kuznetsov.linoleumShopRest.exception;

public class RevisionNotFoundException extends RuntimeException{

    public RevisionNotFoundException(){
        super("Revision is null, not found!");
    }

    public RevisionNotFoundException(String message){
        super(message);
    }
}
