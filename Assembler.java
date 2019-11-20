import java.io.*;
import java.util.*;
import java.util.Arrays.*;

class SymTab
{
 int loc;
 String val;

 SymTab(int loc,String val)
 {
  this.loc=loc;
  this.val=val;
 }
}

class Assembler
{
 String str="";
 static HashMap<String, Integer> opcode = new HashMap<>();
 static Hashtable<String,SymTab> symtab = new Hashtable<String,SymTab>();
 static int loc=0;

 public static void main(String[] arg) 
 {
  BufferedReader br;
  int list[];

  if(arg.length==0)
  {
   System.err.print("\nrequired parameter missing..\n");
   System.exit(1);
  }
   
  opcode.put("BYTE",2);
  opcode.put("MOV",2);
  opcode.put("ADD",2);
  opcode.put("END",0);

  File f=new File(arg[0]);
  
  if(!f.exists())
  {
   System.err.print("\nfile not found - "+arg[0]+"\n");
   System.exit(2);
  }

  try
  {
   FileReader fr=new FileReader(arg[0]);
   br=new BufferedReader(fr);

   String s;
   boolean ok=true,end=false;
   String last="";
   while((s=br.readLine())!=null)
   {
    StringTokenizer stn=new StringTokenizer(s," ");
    last=s;
    if(ok)
    {
     if(stn.nextToken().equals("START"))
     {
      loc=Integer.parseInt(stn.nextToken());
     }
     else
     {
      System.out.println("START not initialized");
      loc=0;
     }
     ok=false;
    }

    while(stn.hasMoreTokens())
    {
     String s1=stn.nextToken();
     String s2="";
     

     if(s1.equals("END"))
     {
      end=true;
     }
     else if(opcode.containsKey(s1))
     {
      int n=(Integer)opcode.get(s1);
      char c,c1;
      if(n==stn.countTokens())
      {
       String op=stn.nextToken();
       c=op.charAt(0);
       ++loc;

       switch(s1)
       {
        case "BYTE" : SymTab st;
                      if((c>='a' && c<='z') ||(c=='_') || (c>='A' && c<='Z'))
                      {
                       st=new SymTab(loc,stn.nextToken());
                       symtab.put(op,st);
                      }
                      else
		      {
		       System.out.println("invalid variable declaration : "+op);
                      }
                      break;

        case  "MOV" : 
        case  "ADD" : s2=stn.nextToken();
                      
                      c1=s2.charAt(0);
                      if(!(op.equals("R1")||op.equals("R2")||op.equals("R3")||op.equals("R4")||op.equals("R5")||c=='@'))
                      {
                       if((c>='a' && c<='z') ||(c=='_') || (c>='A' && c<='Z'))
                       {
                        if(!symtab.containsKey(op))
                        {
                         st=new SymTab(-1,"-1");
                         symtab.put(op,st);
                        }
                       }
                       else
                       {
                        System.out.println("invalid variable declaration : "+op);
                       }
                      }
                      
                      if(!(s2.equals("R1")||s2.equals("R2")||s2.equals("R3")||s2.equals("R4")||                                                  s2.equals("R5")||c1=='@'||c1=='#'))
                      {
                       if((c1>='a' && c1<='z') ||( c1=='_') || (c1>='A' && c1<='Z'))
                       {
                        if(!symtab.containsKey(s2))
                        {
                         st=new SymTab(-1,"-1");
                         symtab.put(s2,st);
                        }
                       }
                       else
                       {
                        System.out.println("invalid variable declaration : "+s2);
                       }
                      }
                      else
                      {
                       System.out.println("invalid operand for : "+s1);
                      }
                       
       }
      }
      else
      {
       System.out.println("Invalid number of operands for "+s1);
       System.exit(3);
      }
     }
    }
   }
   
   if(!end)
   {
    System.out.println("END not found");
    System.exit(3);
   }

   /*Enumeration et=symtab.keys();
   while(et.hasMoreElements())
   {
    String op=et.nextElement()+"";
    SymTab st=symtab.get(op);
    if(st.loc==-1)
    {
     System.out.println("variable must be declared : "+op);
    }
    else
    {
     System.out.println(st.loc+" "+op+" "+st.val);
    }    
   }*/    
   try
   {
    FileWriter file=new FileWriter("SYMTAB.txt");
    BufferedWriter b=new BufferedWriter(file);

    Enumeration et=symtab.keys();
    while(et.hasMoreElements())
    {
     String op=et.nextElement()+"";
     SymTab st=symtab.get(op);
     if(st.loc==-1)
     {
      System.out.println("variable must be declared : "+op);
      b.write("----");
      //b.flush();
     }
     else
     {
      //System.out.println(st.loc+" "+op+" "+st.val);
      b.write(st.loc+" "+st.val+" "+op+"\r\n");
      b.flush();
     }
    }
    file.close();
   }
   catch(IOException e)
   {
    System.err.println("\ni/o alert - "+e.getMessage()+"\n");
   }
  }
  catch(FileNotFoundException e)
  {
   System.err.println("\nfile not found - "+arg[0]+"\n");
  }
  catch(IOException e)
  {
   System.err.println("\ni/o alert - "+e.getMessage()+"\n");
  } 
 }	
}