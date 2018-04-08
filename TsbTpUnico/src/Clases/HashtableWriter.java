/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;
import java.io.*;
/**
 *
 * @author JuanB
 */
public class HashtableWriter {
    
private String arch = "lista.dat";

public HashtableWriter()
{
}

public HashtableWriter(String nom)
{
arch = nom;
}

public void write (TSB_OAHashtable sl) throws HashtableIOException
{
try
{
FileOutputStream ostream = new FileOutputStream(arch);
ObjectOutputStream p = new ObjectOutputStream(ostream);
p.writeObject(sl);
p.flush();
ostream.close();
}
catch ( Exception e )
{
    System.out.println(e.getMessage());
throw new HashtableIOException("Error...");
}
}
}
