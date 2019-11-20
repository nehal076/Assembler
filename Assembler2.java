import java.io.*;
import java.util.*;

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

class Assembler2
{
 public static void main(String arg[])
 {
  Hashtable<String,SymTab> symtab = new Hashtable<String,SymTab>();

  Hashtable<String,Integer> optab = new Hashtable<String,Integer>();
  optab.put("ADD",15000);
  optab.put("MOV",15001);
  optab.put("END",-1);
  optab.put("START",0);

  Hashtable<String,Integer> register = new Hashtable<String,Integer>(); 
  register.put("R1",20001);
  register.put("R2",20002);
  register.put("R3",20003);
  register.put("R4",20004);
  register.put("R5",20005);

  if(arg.length==0)
  {
   System.err.print("\nrequired parameter missing..\n");
   System.exit(1);
  }

  File f=new File(arg[0]);
  File d=new File("INTERMEDIATE.txt");

  try
  {
   FileReader fr1=new FileReader("SYMTAB.txt");
   BufferedReader br1=new BufferedReader(fr1);

   FileReader fr=new FileReader(f);
   BufferedReader br=new BufferedReader(fr);

   FileWriter fw=new FileWriter(d);
   BufferedWriter bw=new BufferedWriter(fw); 

   String s,s1;
   while((s1=br1.readLine())!=null)
   {
    StringTokenizer stn=new StringTokenizer(s1," ");
    while(stn.hasMoreTokens())
    {
     int loc=Integer.parseInt(stn.nextToken());
     SymTab st=new SymTab(loc,stn.nextToken());
     symtab.put(stn.nextToken(),st);
    }
   }

   bw.write("File Name     : "+f.getName()+"\n");
   bw.write("Last Modified : "+new Date(f.lastModified())+"\n");
   bw.write("Path          : "+f.getCanonicalPath()+"\n");
   bw.write("File size     : "+f.length()+"\n\n");
   bw.flush();

   while((s=br.readLine())!=null)
   {
    StringTokenizer stn=new StringTokenizer(s," ");
    //System.out.print(s+"\r\n");
    while(stn.hasMoreTokens())
    {
     String str=stn.nextToken();
     
     if(optab.containsKey(str))
     {
      if(str.equals("START"))
      {
       bw.write(optab.get(str)+"\r\n");
       bw.flush();
      }
      else
      {
       bw.write(optab.get(str)+" ");
       bw.flush();
      }
     }
     else
     if(register.containsKey(str))
     {
      bw.write(register.get(str)+" ");
      bw.flush();
     }
     else
     if(str.equals("BYTE"))
     {
      SymTab st=symtab.get(stn.nextToken());
      String s2=st.val+"";
      bw.write(s2.substring(1,s2.length())+"\r\n");
      bw.flush();
     }
     else
     if(symtab.containsKey(str))
     {
      SymTab st=symtab.get(str);
      bw.write(st.loc+"\r\n");
      bw.flush();
     }
    }
   }
   fr.close();
   fw.close();
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