/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class HashtableReader {
    private String arch = "lista.dat";

public HashtableReader()
{
}

public HashtableReader(String nom)
{
arch = nom;
}

public TSB_OAHashtable read() throws HashtableIOException
{
TSB_OAHashtable sl = null;
try
{
FileInputStream istream = new FileInputStream(arch);
ObjectInputStream p = new ObjectInputStream(istream);
sl = ( TSB_OAHashtable ) p.readObject();
p.close();
istream.close();
}
catch (Exception e)
{
throw new HashtableIOException("Error…");
}
return sl;
}
}
