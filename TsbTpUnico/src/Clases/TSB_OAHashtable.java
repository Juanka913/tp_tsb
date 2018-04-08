/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
/**
 *
 * @author JuanB
 */
public class TSB_OAHashtable<K,V> implements Map<K,V>, Cloneable, Serializable 
{
//************************ Constantes (privadas o públicas).    
    
    
    private final static int MAX_SIZE = Integer.MAX_VALUE;


    //************************ Atributos privados (estructurales).
    
    
    private Map.Entry<K, V> table[];
    
    private String estados[];
    
    
    private int initial_capacity;
    
   
    private int count;
    
   
    private float load_factor;
      
    
    //************************ Atributos privados (para gestionar las vistas).

    
    private transient Set<K> keySet = null;
    private transient Set<Map.Entry<K,V>> entrySet = null;
    private transient Collection<V> values = null;

    
    //************************ Atributos protegidos (control de iteración).
    
   
    protected transient int modCount;
    
    
    //************************ Constructores.

    
    public TSB_OAHashtable()
    {
        this(5, 0.5f);
        for (int i = 0; i < 5; i++) {
            
            estados[i] = "abierto";
        }
    }
    
    
    public TSB_OAHashtable(int initial_capacity)
    {
        this(initial_capacity, 0.5f);
        
    }

    
    public TSB_OAHashtable(int initial_capacity, float load_factor)
    {
        if(load_factor <= 0) { load_factor = 0.5f; }
        if(initial_capacity <= 0) { initial_capacity = 11; }
        else
        {
            if(initial_capacity > TSB_OAHashtable.MAX_SIZE) 
            {
                initial_capacity = TSB_OAHashtable.MAX_SIZE;
            }
        }
        
        int tamano = siguientePrimo(initial_capacity);
        this.table = new Map.Entry[tamano];
        this.estados = new String[tamano];
        for (int i = 0; i < tamano; i++) {
            
            estados[i] = "abierto";
        }
        
        this.initial_capacity = tamano;
        this.load_factor = load_factor;
        this.count = 0;
        this.modCount = 0;
    }
    
    
    public TSB_OAHashtable(Map<? extends K,? extends V> t)
    {
        this(11, 0.8f);
        this.putAll(t);
    }
    
    
    //************************ Implementación de métodos especificados por Map.
    
    
    @Override
    public int size() 
    {
        return this.count;
    }

    
    @Override
    public boolean isEmpty() 
    {
        return (this.count == 0);
    }

    
    @Override
    public boolean containsKey(Object key) 
    {
        return (this.get((K)key) != null);
    }

        
    @Override
    public boolean containsValue(Object value)
    {
        return this.contains(value);
    }

   
    @Override
    public V get(Object key) 
    {   
       
       if(key == null) throw new NullPointerException("get(): parámetro null");
       
       int ib = this.h(key.hashCode());
       Map.Entry<K, V> bucket = this.table[ib];
            
       Map.Entry<K, V> x = this.search_for_entry((K)key);
       return (x != null)? x.getValue() : null;
    }

    
    @Override
    public V put(K key, V value) 
    {
       if(key == null || value == null) throw new NullPointerException("put(): parámetro null");
       
       int ib = this.h(key);
       
            
       V old = null;
       Map.Entry<K, V> x = this.search_for_entry((K)key);
       if(x != null) 
       {
           old = x.getValue();
           x.setValue(value);
       }
       else
       {
           
           if(this.averageLength() >= this.load_factor * table.length) this.rehash();
           ib = this.h(key);
           
           if ("cerrado".equals(this.estados[ib]))
           {    
                Map.Entry<K, V> entry = new Entry<>(key, value);
                this.table[this.encontrarUbicacion(ib, table.length)] = entry;
                this.count++;
                this.modCount++;
                estados[this.encontrarUbicacion(ib, table.length)] = "cerrado";
           }
           
           else
           {
               Map.Entry<K, V> entry = new Entry<>(key, value);
                this.table[ib] = entry;
                this.count++;
                this.modCount++;
                estados[ib] = "cerrado";
           }
       }
       
       return old;
    }

    
    @Override
    public V remove(Object key) 
    {
       if(key == null) throw new NullPointerException("remove(): parámetro null");
       
       int ib = this.h(key.hashCode());
       
       
       int ik = this.search_for_index((K)key);
       V old = null;
       if(ik != -1)
       {
           old = this.remove(ik).getValue();
          
       }
       
       return old;        
    }

    
    @Override
    public void putAll(Map<? extends K, ? extends V> m) 
    {
        for(Map.Entry<? extends K, ? extends V> e : m.entrySet())
        {
            put(e.getKey(), e.getValue());
        }
    }

    
    @Override
    public void clear() 
    {
        this.table = new Map.Entry[5];
        this.estados = new String[5];
        for (int i = 0; i < 5; i++) {
            
            estados[i] = "abierto";
        }
        
        this.count = 0;
        this.modCount++;
    }

    
    @Override
    public Set<K> keySet() 
    {
        if(keySet == null) 
        { 
            // keySet = Collections.synchronizedSet(new KeySet()); 
            keySet = new KeySet();
        }
        return keySet;  
    }
        
    
    @Override
    public Collection<V> values() 
    {
        if(values==null)
        {
            // values = Collections.synchronizedCollection(new ValueCollection());
            values = new ValueCollection();
        }
        return values;    
    }

    
    @Override
    public Set<Map.Entry<K, V>> entrySet() 
    {
        if(entrySet == null) 
        { 
            // entrySet = Collections.synchronizedSet(new EntrySet()); 
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    
    //************************ Redefinición de métodos heredados desde Object.
    
    
    @Override
    protected Object clone() throws CloneNotSupportedException 
    {
        TSB_OAHashtable<K, V> t = (TSB_OAHashtable<K, V>)super.clone();
        t.table = new Map.Entry[table.length];
        
        t.keySet = null;
        t.entrySet = null;
        t.values = null;
        t.modCount = 0;
        return t;
    }

    
    @Override
    public boolean equals(Object obj) 
    {
        if(!(obj instanceof Map)) { return false; }
        
        Map<K, V> t = (Map<K, V>) obj;
        if(t.size() != this.size()) { return false; }

        try 
        {
            Iterator<Map.Entry<K,V>> i = this.entrySet().iterator();
            while(i.hasNext()) 
            {
                Map.Entry<K, V> e = i.next();
                K key = e.getKey();
                V value = e.getValue();
                if(t.get(key) == null) { return false; }
                else 
                {
                    if(!value.equals(t.get(key))) { return false; }
                }
            }
        } 
        
        catch (ClassCastException | NullPointerException e) 
        {
            return false;
        }

        return true;    
    }

    
    @Override
    public int hashCode() 
    {
        if(this.isEmpty()) {return 0;}
        
        int hc = 0;
        for(Map.Entry<K, V> entry : this.entrySet())
        {
            hc += entry.hashCode();
        }
        
        return hc;
    }
    
    
    @Override
    public String toString() 
    {
        StringBuilder cad = new StringBuilder("");
        for(int i = 0; i < this.table.length; i++)
        {
            cad.append("\nLista ").append(i).append(":\n\t").append(this.table[i].toString());   
        }
        return cad.toString();
    }
    
    
    //************************ Métodos específicos de la clase.

    
    public boolean contains(Object value)
    {
        if(value == null) return false;
        
        for(Map.Entry<K, V> bucket : this.table)
        {
            
                if(value.equals(bucket.getValue())) return true;
                      
        }
        return false;
    }
    
    
    protected void rehash()
    {
        int old_length = this.table.length;
        
        // nuevo tamaño: doble del anterior, más uno para llevarlo a impar...
        int new_length = siguientePrimo(old_length * 2);
        
        // no permitir que la tabla tenga un tamaño mayor al límite máximo...
        // ... para evitar overflow y/o desborde de índices...
        if(new_length > TSB_OAHashtable.MAX_SIZE) 
        { 
            new_length = TSB_OAHashtable.MAX_SIZE;
        }

        // crear el nuevo arreglo con new_length listas vacías...
        Map.Entry<K, V> temp[] = new Map.Entry[new_length];
        String tempEstados[] = new String[new_length];
        for (int i = 0; i < new_length; i++) {
            
            tempEstados[i] = "abierto";
        }
        estados = tempEstados;
        // notificación fail-fast iterator... la tabla cambió su estructura...
        this.modCount++;  
       
        // recorrer el viejo arreglo y redistribuir los objetos que tenia...
        for(int i = 0; i < this.table.length; i++)
        {
         
               Map.Entry<K, V> x = table[i];
               
               
               if (x != null)
               {
                   
                K key = x.getKey();
                int y = this.h(key, temp.length);
           
               if ("cerrado".equals(tempEstados[y]))
               {    
                    
                    int ubicacion = this.encontrarUbicacion(y, temp.length);
                    
                    temp[ubicacion] = x;
                    
                    estados[ubicacion] = "cerrado";
               }

               else
               {
                    temp[y] = x;
                    estados[y] = "cerrado";            
               }
               }
           }
        
       
        // cambiar la referencia table para que apunte a temp...
        this.table = temp;

    }
    
    public Map.Entry<K,V> remove(int index)
    {
        if(index >= count || index < 0)
        {
            throw new IndexOutOfBoundsException("remove(): índice fuera de rango...");
        }
        
        Object old = table[index];

        estados[index] = "tumba";
        
        count--;

        // detección rápida de fallas en el iterador (fail-fast iterator)...
        this.modCount++; 
        
        return (Map.Entry<K,V>) old;
    }
    
    public void addFromList(List<String> lista)
    {
    
        for(String i : lista)
        {
            if(this.search_for_entry((K) i) != null )
            {
               Map.Entry<K,V> p = this.search_for_entry((K) i);
               Object nuevo = Integer.parseInt(p.getValue().toString()) + 1;
               V n = (V) nuevo;
               p.setValue(n);
             
            }
       else
       {
           
           if(this.count >= this.load_factor * table.length) this.rehash();
           int ib = this.h((K)i);
           
           if ("cerrado".equals(this.estados[ib]))
           {    
                Map.Entry<K, V> entry = new Entry<>((K)i, (V)(Object)1);
                int ubicacion = this.encontrarUbicacion(ib, table.length);
                
                this.table[ubicacion] = entry;
                this.count++;
                this.modCount++;
                estados[ubicacion] = "cerrado";
           }
           
           else
           {
               Map.Entry<K, V> entry = new Entry<>((K)i, (V)(Object)1);
               
                this.table[ib] = entry;
                this.count++;
                this.modCount++;
                estados[ib] = "cerrado";
           }
       }
       
        }  
    }
    
    
    
    private static final int siguientePrimo ( int n )
    {
        if ( n % 2 == 0) n++;
        for ( ; !esPrimo(n); n+=2 ) ;
        return n;
    }
    
    
    private static boolean esPrimo(int numero)
    {
        boolean flag = true;
    
    for (int z = 2; z < numero  ; z++)
    {
        if (numero%z == 0)
        {
        flag = false;
        break;
        }
    }
        
        return flag;
    }
    
    
    private int encontrarUbicacion(int indice, int largo) 
    {
        int contador = 1;
        int j = 0;
        if ((indice + 1) != largo)
        {
            j = indice + 1;
        }
        
        for (int i = j ; i < largo; ) 
        {
            if("abierto".equals(estados[i]) || "tumba".equals(estados[i]))
            {
                
                return i;
            }
            contador = contador + 1; 
            i = (indice + (int) Math.pow((contador), 2));
            
           if (i >= largo )
            {
                indice = 0;
                i = 0;
                contador = 0;
            }
       
        }
        
       throw new NullPointerException("La cola no posee lugares libres.");
    }

    //************************ Métodos privados.
    
    
    private int h(int k)
    {
        return h(k, this.table.length);
    }
    
   
    private int h(K key)
    {
        return h(key.hashCode(), this.table.length);
    }
    
    
    private int h(K key, int t)
    {
        return h(key.hashCode(), t);
    }
    
    
    private int h(int k, int t)
    {
        if(k < 0) k *= -1;
        return k % t;        
    }
    
    
    private int averageLength()
    {
        return this.table.length - this.count;
    } 
    
    
    private Map.Entry<K, V> search_for_entry(K key)
    {
        
        for(Map.Entry<K, V> a : table)
        {
            if(a!= null && key.equals(a.getKey())) return a;
        }
        return null;
    }
    
    
    private int search_for_index(K key)
    {
        for(int i = 0; i < table.length; i++)
        {
            if(key.equals(table[i].getKey())) return i;
        }
        return -1;
    }  
    
    
    

    
    //************************ Clases Internas.
    
    
    private class Entry<K, V> implements Map.Entry<K, V>, Serializable
    {
        private K key;
        private V value;
        
        public Entry(K key, V value) 
        {
            if(key == null || value == null)
            {
                throw new IllegalArgumentException("Entry(): parámetro null...");
            }
            this.key = key;
            this.value = value;
        }
        
        @Override
        public K getKey() 
        {
            return key;
        }

        @Override
        public V getValue() 
        {
            return value;
        }

        @Override
        public V setValue(V value) 
        {
            if(value == null) 
            {
                throw new IllegalArgumentException("setValue(): parámetro null...");
            }
                
            V old = this.value;
            this.value = value;
            return old;
        }
       
        @Override
        public int hashCode() 
        {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.key);
            hash = 61 * hash + Objects.hashCode(this.value);            
            return hash;
        }

        @Override
        public boolean equals(Object obj) 
        {
            if (this == obj) { return true; }
            if (obj == null) { return false; }
            if (this.getClass() != obj.getClass()) { return false; }
            
            final Entry other = (Entry) obj;
            if (!Objects.equals(this.key, other.key)) { return false; }
            if (!Objects.equals(this.value, other.value)) { return false; }            
            return true;
        }       
        
        @Override
        public String toString()
        {
            return "(" + key.toString() + ", " + value.toString() + ")";
        }
    }
    
    
    
    private class KeySet extends AbstractSet<K> 
    {
        @Override
        public Iterator iterator() 
        {
            return new KeySetIterator(table);
        }
        
        @Override
        public int size() 
        {
            return TSB_OAHashtable.this.count;
        }
        
        @Override
        public boolean contains(Object o) 
        {
            return TSB_OAHashtable.this.containsKey(o);
        }
        
        @Override
        public boolean remove(Object o) 
        {
            return (TSB_OAHashtable.this.remove(o) != null);
        }
        
        @Override
        public void clear() 
        {
            TSB_OAHashtable.this.clear();
        }
        
        private class KeySetIterator implements Iterator
        {
            int contadorActual;
            Object[] vector;
            Map.Entry actual;
        
        public KeySetIterator(Object[] v)
        {
            contadorActual = 0;
            vector = v;
        }
        
        @Override
        public boolean hasNext() {
            if (table.length == 0)
            {
                return false;
            }
            if (contadorActual == table.length)
            {
                return false;
            }
            return true;
        }

        @Override
        public Object next() {
            if (!hasNext())
            {
                throw new NoSuchElementException("No quedan elementos");
            }
            else
            {    
                
                actual = (Map.Entry<K, V>)vector[contadorActual];
                contadorActual += 1;
            }
            
            return actual;
        
        }

        @Override
        public void remove() {
            
            if (actual != null)
            {
                
                int restantes = table.length - (contadorActual);
                if (restantes > 0)
                {
                TSB_OAHashtable.this.remove(contadorActual);
                
                }
                
                contadorActual -= 1;
            }
            else
            {
                throw new NoSuchElementException();
            }    
        }
          
    }
    }

    
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> 
    {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() 
        {
            return new EntrySetIterator(table);
        }

        /*
         * Verifica si esta vista (y por lo tanto la tabla) contiene al par 
         * que entra como parámetro (que debe ser de la clase Entry).
         */
        @Override
        public boolean contains(Object o) 
        {
            if(o == null) { return false; } 
            if(!(o instanceof Entry)) { return false; }
            
            Map.Entry<K, V> entry = (Map.Entry<K,V>)o;
            K key = entry.getKey();
            
            if(TSB_OAHashtable.this.containsKey(key)) { return true; }
            return false;
        }

        
        @Override
        public boolean remove(Object o) 
        {
            if(o == null) { throw new NullPointerException("remove(): parámetro null");}
            if(!(o instanceof Entry)) { return false; }

            Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
            K key = entry.getKey();
            V value = entry.getValue();
            
            
            if(TSB_OAHashtable.this.remove(key, value)) 
            {
                TSB_OAHashtable.this.count--;
                TSB_OAHashtable.this.modCount++;
                return true;
            }
            return false;
        }

        @Override
        public int size() 
        {
            return TSB_OAHashtable.this.count;
        }

        @Override
        public void clear() 
        {
            TSB_OAHashtable.this.clear();
        }
        
        private class EntrySetIterator implements Iterator
        {
            int contadorActual;
            Object[] vector;
            Map.Entry actual;
        
        public EntrySetIterator(Object[] v)
        {
            contadorActual = 0;
            vector = v;
        }
        
        @Override
        public boolean hasNext() {
            if (table.length == 0)
            {
                return false;
            }
            if (contadorActual == table.length)
            {
                return false;
            }
            return true;
        }

        @Override
        public Object next() {
            if (!hasNext())
            {
                throw new NoSuchElementException("No quedan elementos");
            }
            else
            {    
                
                actual = (Map.Entry<K, V>)vector[contadorActual];
                contadorActual += 1;
            }
            
            return actual;
        
        }

        @Override
        public void remove() {
            
            if (actual != null)
            {
                
                int restantes = table.length - (contadorActual);
                if (restantes > 0)
                {
                TSB_OAHashtable.this.remove(contadorActual);
                
                }
              
                contadorActual -= 1;
            }
            else
            {
                throw new NoSuchElementException();
            }    
        }
          
    }
    }    
    
    
    private class ValueCollection extends AbstractCollection<V> 
    {
        @Override
        public Iterator<V> iterator() 
        {
            return new ValueCollectionIterator(table);
        }
        
        @Override
        public int size() 
        {
            return TSB_OAHashtable.this.count;
        }
        
        @Override
        public boolean contains(Object o) 
        {
            return TSB_OAHashtable.this.containsValue(o);
        }
        
        @Override
        public void clear() 
        {
            TSB_OAHashtable.this.clear();
        }
        
        private class ValueCollectionIterator implements Iterator
        {
            int contadorActual;
            Object[] vector;
            Map.Entry actual;
        
        public ValueCollectionIterator(Object[] v)
        {
            contadorActual = 0;
            vector = v;
        }
        
        @Override
        public boolean hasNext() {
            if (table.length == 0)
            {
                return false;
            }
            if (contadorActual == table.length)
            {
                return false;
            }
            return true;
        }

        @Override
        public Object next() {
            if (!hasNext())
            {
                throw new NoSuchElementException("No quedan elementos");
            }
            else
            {    
                
                actual = (Map.Entry<K, V>)vector[contadorActual];
                contadorActual += 1;
            }
            
            return actual;
        
        }

        @Override
        public void remove() {
            
            if (actual != null)
            {
                
                int restantes = table.length - (contadorActual);
                if (restantes > 0)
                {
                TSB_OAHashtable.this.remove(contadorActual);
                
                }

                contadorActual -= 1;
            }
            else
            {
                throw new NoSuchElementException();
            }    
        }
          
    }
    }
}
    

