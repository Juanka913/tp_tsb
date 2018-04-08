/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author JuanB
 */
public class HashtableIOException extends Exception 
{
    private String message = "Problema al serializar la tabla";

public HashtableIOException()
{
}

public HashtableIOException(String msg)
{
message = msg;
}

public String getMessage()
{
return message;
}
}
