/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author JuanB
 */
public class LogicaPantallas {
    
    
    
public List<String> agregarArchivos(String nombre) throws FileNotFoundException, UnsupportedEncodingException
{
    
    
    
    List<String> lista = new ArrayList<>();
        
        
        File fc = new File(nombre);
        
    
    
Scanner sc = new Scanner(new InputStreamReader(new FileInputStream(fc), "ISO-8859-1"));
     
        
{

while (sc.hasNextLine())
{
    Scanner sc2 = new Scanner(sc.nextLine());
    while(sc2.hasNext())
    {
    
    String word = sc2.next();
    
    // Si tambien queremos remover los acentos y cualquier simbolo especial
    //    word = Normalizer.normalize(word, Normalizer.Form.NFD);
    //    word = word.replaceAll("[^\\p{ASCII}]", "");
    
    word = word.replace("-"," ");
    String[] palabras = word.replaceAll("[^ñÑáéíóúa-zA-Z ]", "").toLowerCase().split("\\s+");
    
    for(String s : palabras)
    {
        if(!"".equals(s))
    {
        lista.add(s);
    }
    }
    
    }
}
}


return lista;
}



public void guardar(TSB_OAHashtable tabla)
{
    try
    {
        HashtableWriter slw = new HashtableWriter();
        slw.write( tabla );
    }
    catch(HashtableIOException e)
    {
        System.out.println("Error: " + e.getMessage());
    }

}


public TSB_OAHashtable cargar()
{
    TSB_OAHashtable sl = new TSB_OAHashtable();
    try
    {
        
        HashtableReader slr = new HashtableReader();
        sl = (TSB_OAHashtable) slr.read();
        
    }
    catch( HashtableIOException e)
    {
        System.out.println("Error: " + e.getMessage());
    }

    return sl;
}

public String[] buscar(TSB_OAHashtable tabla, String buscar)
{
    String[] devolver = new String[2];
    Iterator a = tabla.entrySet().iterator();
    
    while (a.hasNext())
    {
        Map.Entry entrada = (Map.Entry) a.next();
        
        if (entrada != null && entrada.getKey().equals(buscar))
        {
            devolver[0] = entrada.getKey().toString();
            devolver[1] = entrada.getValue().toString();
        
        }
        
     
    }
      return devolver;
}

}

    
    

