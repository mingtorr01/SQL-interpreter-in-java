package SQL;

import java.util.*;
//SELECT * FROM student;
//SELECT name FROM student WHERE name='박민철';
//union : SELECT * FROM student UNION SELECT * FROM professor;
//intersect : SELECT * FROM student INTERSECT SELECT * from professor;
//minus : SELECT * FROM student MINUS SELECT * from professor;
//join :SELECT student.name FROM student,professor WHERE student.name=professor.name;
//cartesian : SELECT student.name FROM student,professor;
//divide : SELECT student.name FROM student,professor WHERE student.dname=professor.dname AND student.dname='ce';

public class _main {
	static Table student = new Table(10);
	static Table professor = new Table(10);	
	public static void init_result(String[][] result) {
		for(int i=0; i<result.length;i++)
			for(int j=0;j<result[0].length;j++)
				result[i][j]="";
	}
	public static void divide(String query) {
		
		String[] arrQuery = query.split("(select)|(from)|(where)|(and)|;");
		String[] arrIstrue = arrQuery[3].split("(\\.)|=");
		String[] arrColumn = arrQuery[1].split(",");
		if(arrQuery[1].contains(arrIstrue[1]) || arrQuery[1].contains(arrIstrue[3]))
			return;
		String[][] result = new String[16][6];
		int[] restrict;
    	Join_Restriction(result,arrQuery[3]);
    	String[] arrWhere=new String[5];
    	if(arrQuery[4].contains("student")) {
    		String[] arrAnd = arrQuery[4].split("student.");
    		restrict=student.Restrict(arrAnd[1]);
    		student.print_restrict(restrict);
    	}
    	else if( arrQuery[4].contains("professor")) {
    		String[] arrAnd = arrQuery[4].split("professor.");
    		restrict=professor.Restrict(arrAnd[1]);
    		professor.print_restrict(restrict);
    	}
    	else 
    		return;
    	for(int i=0; i<5;i++) {
    		if(restrict[i]!=-1 && arrQuery[4].contains("student"))
    			arrWhere[i]=student.getData(restrict[i], 0);
    		if(restrict[i]!=-1 && arrQuery[4].contains("professor"))
    			arrWhere[i]=professor.getData(restrict[i], 0);
    	}
    	int[] number= new int[6];
		for(int i=0;i<number.length;i++)
			number[i]=-1;
		Join_Projection(number,arrQuery[1]);
		int flag=0;
		System.out.println("Call PROJECT");
		System.out.println("*******************************************");
		
		for(int i=0; i<16;i++) {
			for(int j=0; j<5;j++)
			if(result[i][0]!=null&&arrWhere[j]==result[i][0]) {
				for(int a=0; a<3;a++) {
						System.out.print(result[i][a]+"\t");
				}
				System.out.println("");
			}
		}
		
		System.out.println("*******************************************");
	}
	public static void Join_print(int[] num,String[][] result ) {
		System.out.println("CALL PROJECT");
		System.out.println("*******************************************");
		int flag=0;
		for(int i=0; i<result.length;i++){   
			flag=0;
			for(int j=0;j<num.length;j++)
			{
				if(num[j]!=-1 && result[i][num[j]] != null)
				{
					System.out.print(result[i][num[j]]+"\t");
					flag++;
				}
				if(j==result[0].length-1&&flag!=0)
					System.out.println("");   
			}
		}
		System.out.println("*******************************************");
	}
	
	public static void Join_Projection(int[] number, String message) {
		int cnt=0;
		int sid=0, pid=0;
		String[] arrSelect = message.split(",");
	    for(int i=0;i<arrSelect.length;i++) {
	    	String[] arrJoin = arrSelect[i].split("\\.");
	        if(arrJoin[0].equals("student")) {
	        	sid= student.getColumn(arrJoin[1].toString());
	        	if(sid==-1)
	        		return;
	        	number[cnt]= sid;
	        	cnt++;
	        }
	        else if(arrJoin[0].equals("professor")) {
	        	pid= professor.getColumn(arrJoin[1].toString());
	            if(pid==-1)
	            	return;
	            number[cnt]= pid+3;
	        	cnt++;
	         }
	        else
	        	return;
	    }
	}
	public static void Join_Restriction(String[][] result, String message) {
    	for(int j=0; j<result[0].length;j++) {
    			if(j<3)
    				result[0][j]=student.getData(0, j);
    			else
    				result[0][j]=professor.getData(0, j-3);
    	}	
		String[] arrRestrict= message.split("(\\.)|=");
		int id= student.getColumn(arrRestrict[1]);
		if(id==-1)
			return;
		String[] results= student.getTuple(id).clone();
		String[] resultp= professor.getTuple(id).clone();
		int cnt=1;
		for(int i=1; i<5;i++) {
			for(int j=1; j<5;j++) {
				if(results[i].equals(resultp[j].toString())) {
					for(int a=0; a<3;a++)
						result[cnt][a]= student.getData(i, a);
					for(int a=3; a<6;a++)
						result[cnt][a]= professor.getData(j, a-3);
					cnt++;
				}
			}
		}
		int flag=0;
		System.out.println("Call JOIN");
		System.out.println("*******************************************");
		for(int i=0;i<result.length;i++) {
			flag=0;
			for(int j=0; j<result[0].length;j++) {
				if(result[i][j]!=null) {
					System.out.print(result[i][j]+"\t");
					flag++;
				}
				if(j==result[0].length-1&&flag!=0)
					System.out.println("");
			}
		}
		System.out.println("*******************************************\n");
	}
	
	public static void Join(String query) {
		String[] arrQuery = query.split("(select)|(from)|(where)|;");
		String[] arrColumn = arrQuery[1].split(",");
		int[] number= new int[6];
		for(int i=0;i<number.length;i++)
			number[i]=-1;
		
	    Join_Projection(number,arrQuery[1]);
	    if(arrQuery.length==3) {
	    	int sid=0, pid=0;
	    	String[] arrSelect = arrQuery[1].split(",");
	    	for(int i=0;i<arrSelect.length;i++) {
		    	String[] arrJoin = arrSelect[i].split("\\.");
		        if(arrJoin[0].equals("student")) {
		        	sid= student.getColumn(arrJoin[1].toString());
		        }
		        else if(arrJoin[0].equals("professor")) {
		        	pid= professor.getColumn(arrJoin[1].toString());
		        }
		        else
		        	return;
		    }
		    if(pid==-1 ||sid==-1)
		    	return;
        	String[] results= student.getTuple(sid).clone();
        	student.print_project(results);
			String[] resultp= professor.getTuple(pid).clone();
			professor.print_project(resultp);
			
        	student.crossJoin(results,resultp);
        	return;
	    }
	    else if(arrQuery.length==4) {
	    	String[][] result = new String[16][6];
	    	Join_Restriction(result,arrQuery[3]);
			Join_print(number,result);
		}
		else
			System.out.println("error: Join error");
	    return;
	}
	
	public static void set_result(String[][] src,int[] flag,int tmp) {
		for(int i=1;i<5;i++){
			if(flag[i]==tmp) {
				for(int j=0;j<src[i].length;j++) {
					src[i][j]="";
	            }
			}
		}
	}
	
	public static void set_handle(String[][] src,String[][] dest,int mode)
	{
		int[] flag= new int[5];
		for(int i=1;i<5;i++) {
			flag[i]=0;
		}
		for(int i=0;i<5;i++) {
         for(int j=0; j<5;j++) {
            if(Arrays.equals(src[i],dest[j])==true)
               flag[i]=1;
         }
      }
      if(mode==1)
      {
         set_result(src, flag,1);
         for(int i=1;i<5;i++) {
            for(int j=0;j<3;j++) {
               src[i+4][j]=dest[i][j];
            }
         }
      }
      else if(mode==2)
      {
         set_result(src, flag,0);
      }
      else
      {
         set_result(src, flag,1);
      }
	}
   
	public static void handler(String[][] result,String query) 
	{
		for(int i=0; i<9;i++)
			for(int j=0;j<3;j++)
				result[i][j]="";
		String[] arrQuery = query.split("(select)|(from)|(where)|;");
		if(arrQuery.length==4){
			int[] arrRestrict= new int[5];
			int[] arrProject= new int[3];
			if(arrQuery[2].equals("student")) {
				arrRestrict= student.Restrict(arrQuery[3]).clone();
				student.print_restrict(arrRestrict);
				arrProject = student.Project(arrQuery[1]).clone();
				student.mk_result(result, arrProject, arrRestrict);
				student.print(result,0);
			}
			else if(arrQuery[2].equals("professor")) {
				arrRestrict= professor.Restrict(arrQuery[3]).clone();
				professor.print_restrict(arrRestrict);
				arrProject = professor.Project(arrQuery[1]).clone();
				professor.mk_result(result, arrProject, arrRestrict);
				professor.print(result,0);
			}
			else
				System.out.println("error: wrong query!!d");	
			
		}
		else if(arrQuery.length==3){
			int[] arrProject= new int[5];
			if(arrQuery[2].equals("student")) {
				arrProject=student.Project(arrQuery[1]);
				student.mk_result(result, arrProject);
				student.print(result,0);
			}
			else if(arrQuery[2].equals("professor")) {
				arrProject=professor.Project(arrQuery[1]);
				professor.mk_result(result, arrProject);
				professor.print(result,0);
			}
			else
				System.out.println("error: wrong query!!e");
			return;
			}
		else {
			System.out.println("error: wrong query!!f");
			return;
			
		}
		
   }
   
	public static void main(String[] args) {
      
		Scanner scan = new Scanner(System.in); 
		//테이블 생성
		int mode=0;
		student.init_table();
		professor.init_table();
		student.addtuple("박민철", "3051", "ce");
 	 	student.addtuple("박토리", "0503", "math");
 	 	student.addtuple("양해찬", "3061", "ce");
 	 	student.addtuple("이현정", "2068", "food");
 	 	professor.addtuple("박민철", "3050", "ce");
 	 	professor.addtuple("박토리", "0503", "math");
 	 	professor.addtuple("임보민", "3077", "art");
 	 	professor.addtuple("이동건", "3070", "ce");
 	 	System.out.println("student Table");
 	 	student.printAll();
 	 	System.out.println("professor Table");
 	 	professor.printAll();
 	 	String[] dname = {"컴퓨터공학과","수의학과"};
 	 	
 	 	String[][] result= new String[9][3];
 	 	String[][] result1= new String[9][3];
 	 	while(true) {
 	 		System.out.println("쿼리를 입력하세요:"); 
 	 		String message = scan.nextLine();            // 키보드 문자 입력
 	 		message = message.toLowerCase();
 	 		message = message.replace(" ", "");
 	 		message = message.replace("\'", "");
 	 		if(message.contains(";")==false||message.contains("select")==false||message.contains("from")==false) {
 	 			System.out.println("error: wrong query!! (not ')");
 	 			continue;
 	 		}
 	 		if(message.contains("and")) {
 	 			divide(message);
 	 			continue;
 	 		}
 	 		if(message.contains(".")) {
 	 			Join(message);
 	 			continue;
 	 		}
 	 		if(message.contains("exit"))
 	 			break;
 	 		if(message.contains("union"))
 	 			mode=1;
 	 		if(message.contains("intersect"))
 	 			mode=2;
 	 		if(message.contains("minus"))
 	 			mode=3;
 	 		if(mode !=0){
 	 			String[] set_arr= message.split("(union)|(intersect)|(minus)|;");
 	 			handler(result,set_arr[0]);
 	 			handler(result1,set_arr[1]);
 	 			set_handle(result,result1,mode);
 	 			student.print(result, mode);
 	 		}
 	 		else{
 	 			handler(result,message);
 	 		}
 	 		
 	 	}
 	 	scan.close();    
	}
}