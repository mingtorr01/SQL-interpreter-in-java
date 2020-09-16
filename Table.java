package SQL;

import java.util.Arrays;
import java.util.Vector;

public class Table {

	private Vector<Elements[]> words = new Vector<Elements[]>();
	int index=0;
   
	public Table() {
    }
   
	public Table(int size) {//생성자
		Vector<Elements> words = new Vector<Elements>(size);
    }
   
	public void init_table() {//테이블 초기화
		Elements[] arr = new Elements[3];
		for(int i = 0;i < 3;i++) {
			arr[i]= new Elements();
		}
		arr[0].addElements("name");
		arr[1].addElements("num");
		arr[2].addElements("dname");
		words.add(arr);
		index++;
	}
   //행초기화
	public void addtuple(String name,String num, String dname) {
		Elements[] arr = new Elements[3];
		for(int i = 0;i < 3;i++) {
			arr[i]= new Elements();
		}
		arr[0].addElements(name);
		arr[1].addElements(num);
		arr[2].addElements(dname);
		words.add(index,arr);
		index++;
	}
	public void mk_result(String[][] result, int[] column){
		for(int i=0; i<result.length;i++)
			for (int j=0; j<result[0].length;j++) {
				result[i][j] = "";
			}
		for(int i=0; i<column.length;i++)
			for (int j=0; j<words.size();j++) {
				if(column[i] != -1)
					result[j][i]=getData(j)[column[i]].toString();
			}
	}
	
	public void mk_result(String[][] result, int[] column, int[] tuple){
		for(int i=0; i<result.length;i++)
			for (int j=0; j<result[0].length;j++) {
				result[i][j] = "";
			}
		for(int i=0; i<column.length;i++)
			for (int j=0; j<tuple.length;j++) {
				if(column[i] != -1 && tuple[j] != -1)
					result[j][i]=getData(tuple[j])[column[i]].toString();
			}
	}
	
	public int[] Project(String select)
	{
		int[] rtn = new int[3];
		for(int i=0;i<3;i++)
			rtn[i] = -1;
		int cnt=0;
		int t=0;
		if(select.contains("*")) {
			for(int i=0;i<3;i++)
				rtn[i]=i;
			return rtn;
		}
		String[] arr= select.split(",");
		for(int i=0; i<arr.length;i++){
			int flag=getColumn(arr[i]);
			if(flag==-1)
				continue;
			rtn[cnt] = flag;
			cnt++;
			}
		return rtn;
	}
	
	public int getColumn(String value) {
		for(int i=0;i<3;i++) {
			if(getData(0)[i].toString().equals(value))
				return i;
		}
		System.out.println("error: column does not exits");
		return -1;
	}
	
	public String[] getTuple(int column) {
		String[] result= new String[words.size()];
		for(int i=0; i<words.size();i++) {
			result[i]=getData(i)[column].toString();
		}
		return result;
	}
	
	public int[] Restrict (String where){
		int[] rtn = new int[words.size()];
		for(int i=0;i<words.size();i++)
			rtn[i] = -1;
		int cnt=1;
		String[] arr= new String[3];
		arr=where.split("(>=)|(<=)|>|<|!=|=");
		int flag=getColumn(arr[0]);
		rtn[0]=0;
		if(flag==-1)
			return rtn;
		if(where.contains("num")) {
			if(where.contains(">=")) {
				for(int i=1; i<words.size();i++) {
					if( Integer.parseInt(getData(i)[flag].toString())>= Integer.parseInt(arr[1])) {
						rtn[cnt]=i;
						cnt++;
					}
				}
			}
			else if(where.contains("<=")) {
				for(int i=1; i<words.size();i++) {
					if( Integer.parseInt(getData(i)[flag].toString())<= Integer.parseInt(arr[flag])) {
						rtn[cnt]=i;
						cnt++;
					}
				}
			}
			else if(where.contains("<")) {
				for(int i=1; i<words.size();i++) {
					if( Integer.parseInt(getData(i)[flag].toString())< Integer.parseInt(arr[flag])) {
						rtn[cnt]=i;
						cnt++;
					}
				}
			}
			else if(where.contains(">")) {
				for(int i=1; i<words.size();i++) {
					if( Integer.parseInt(getData(i)[flag].toString())> Integer.parseInt(arr[flag])) {
						rtn[cnt]=i;
						cnt++;
					}
				}
			}
		}
		if(where.contains("!=")) {
			for(int i=1; i<words.size();i++) {
				if(getData(i)[flag].toString().equals(arr[1].toString())==false) {
					rtn[cnt]=i;
					cnt++;
				}
			}
		}
		
		if(where.contains("=")) {
			for(int i=1; i<words.size();i++) {
				if(getData(i)[flag].toString().equals(arr[1].toString())) {
					rtn[cnt]=i;
					cnt++;
				}
			}
		}
		return rtn;
    	}
   
    	public void print(String[][] result, int mode)//여러 열 출력
    	{
    		if(mode==0)
    			System.out.println("Call PROJECT");
    		if(mode==1)
    			System.out.println("Call UNION");
    		if(mode==2)
    			System.out.println("Call INTERSECT");
    		if(mode==3)
    			System.out.println("Call MINUS");
    		System.out.println("*******************************************");
    		int flag=0;
    		for(int i=0; i<result.length;i++){   
    			flag=0;
    			for(int j=0;j<result[0].length;j++)
    			{
    				if(result[i][j] != "")
    				{
    					System.out.print(result[i][j]+"\t");
    					flag++;
    				}
    				if(j==result[0].length-1&&flag!=0)
    					System.out.println("");   
    			}
    		}
    		System.out.println("*******************************************\n");
    	}
    	public void crossJoin(String[] src, String[] dest) {
    		System.out.println("Call CARTESIAN");
    		System.out.println("*******************************************");
    		System.out.println(src[0]+"\t"+dest[0]);
    		for(int i=1; i<src.length;i++) {
    			for(int j=1;j<dest.length;j++)
    				System.out.println(src[i]+"\t"+dest[j]);
    		}
   
    		System.out.println("*******************************************\n");
    	}
    	public void printAll() {   
    		System.out.println("*******************************************");
    		for(int cnt = 0; cnt < words.size(); cnt++) {
    			for(int i=0; i<3;i++) {
    				System.out.print(getData(cnt)[i].toString()+"\t");}
    			System.out.print("\n");
    		}
    		System.out.println("*******************************************\n");
    	}
    	public void print_restrict(int[]rest) {
    		System.out.println("Call RESTRICT");
    		System.out.println("*******************************************");
    		int flag=0;
    		for(int i=0; i<rest.length;i++) {
    			flag=0;
    			for(int j=0; j<3;j++) {
    				if(rest[i]!= -1) {
    					System.out.print(getData(rest[i])[j].toString()+"\t");
    					flag++;
    				}
    				if(j==2&&flag!=0)
    					System.out.println("");
    			}
    		}
    		System.out.println("*******************************************\n");
    	}
    	public void print_project(String[]rest) {
    		System.out.println("Call PROJECT");
    		System.out.println("*******************************************");
    		int flag=0;
    		for(int i=0; i<5;i++) {
    			System.out.println(rest[i]);
    		}
    		System.out.println("*******************************************\n");
    	}
    	public Elements[] getData(int index) {
    		return words.get(index);
    	}	
    	public String getData(int index, int idx) {
    		return words.get(index)[idx].toString();
    	}
}