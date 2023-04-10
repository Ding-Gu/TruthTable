package Progress;
import algs.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
public class TruthTable {
    private char[] pf;//命题公式
    private int len;//有效字符串长度
    private boolean[] value;//存储命题公式的值
    private final char[] v;
    private final boolean[][] vh;
    public TruthTable(String propositionalFormula) {
        char[] current=propositionalFormula.toCharArray();
        len=current.length;
        pf=new char[len*3];//pf为三倍长度，方便操作
        Arrays.fill(pf,' ');
        System.arraycopy(current, 0, pf, 0, len);
        transform(0,len-1);//转换字符数组
        v=variables();
        vh=variableHash(v);
        calculate();//计算值
    }//从构造方法获得字符串
    //打印真值表
    public void printTruthTable(){
        int i,j;
        //打印顶部
        System.out.print("┌");
        for(i=0;i<v.length;i++)System.out.print("─┬");
        System.out.println("─┐");

        for(char ch:v)System.out.print("│"+ch);
        System.out.println("│f│");
        //打印中间
        for(i=0;i< vh.length;i++){
            System.out.print("├");
            for(int k=0;k<v.length;k++)System.out.print("─┼");
            System.out.println("─┤");

            for(j=0;j<v.length;j++) {
                System.out.print('│');
                if(vh[i][(int)v[j]-65])System.out.print(1);
                else System.out.print(0);
            }
            System.out.print('│');
            if(value[i])System.out.print(1);
            else System.out.print(0);
            System.out.println('│');
        }
        //打印底部
        System.out.print("└");
        for(i=0;i<v.length;i++)System.out.print("─┴");
        System.out.println("─┘");
    }//打印真值表
    public boolean[] getValue(){return value;}

    //修改字符串
    private int transform(int lo,int hi){
        int i,c;

        for(i=lo;i<=hi;i++) {
            if(pf[i]=='(') {
                int index=fineRight(i);//找到对应右括号
                deleteChar(i);
                deleteChar(--index);//删除左右括号
                hi-=2;
                int len=transform(i,--index);//递归，返回字串长度
                hi+=len-(index-i+1);//为hi添加长度差
                i+=len-2;//i跳步
            }
        }//第一遍循环，用于递归处理括号内的字符串
        for(i=lo,c=0;i<=hi;i++){
            if(pf[i]=='(')c++;
            if(pf[i]==')')c--;
            if(pf[i]=='!'&&c==0){
                insertChar(i++,'(');
                if(pf[i+1]=='(') {
                    insertChar(i = fineRight(i + 1), ')');
                    i++;
                }
                else insertChar(i=(i + 2), ')');
                hi+=2;
            }
        }//第二遍循环，修改叹号
        for(i=lo,c=0;i<=hi;i++){
            if(pf[i]=='(')c++;
            if(pf[i]==')')c--;
            if(pf[i]=='*'&&c==0){
                if(pf[i-1]==')')insertChar(fineLeft(i-1),'(');//在其对应的左括号处插入一个左括号
                else insertChar(i-1,'(');
                if(pf[++i+1]=='!')i++;//插入右括号需要注意
                if(pf[i+1]=='(') {
                    insertChar(i = fineRight(i + 1), ')');
                    i++;
                }
                else insertChar(i=(i + 2), ')');
                hi+=2;
            }
        }//第三遍循环，修改乘号
        for(i=lo,c=0;i<=hi;i++){
            if(pf[i]=='(')c++;
            if(pf[i]==')')c--;
            if(pf[i]=='+'&&c==0){
                if(pf[i-1]==')')insertChar(fineLeft(i-1),'(');//在其对应的左括号处插入一个左括号
                else insertChar(i-1,'(');
                if(pf[++i+1]=='!')i++;//插入右括号需要注意
                if(pf[i+1]=='(') {
                    insertChar(i = fineRight(i + 1), ')');
                    i++;
                }
                else insertChar(i=(i + 2), ')');
                hi+=2;
            }
        }//第三遍循环，修改加号
        if(lo==0&&hi==len-1){
            for(i=0;i<len;i++)
                if(pf[i]=='('||pf[i]=='v')deleteChar(i--);
            char[] newPf=new char[len];
            System.arraycopy(pf,0,newPf,0,len);
            pf=newPf;
        }
        return hi-lo+1;
    }//转换字符串，以方便计数，返回长度：hi-lo+1
    //计算bool值
    private void calculate(){
        Stack<Character> charStack=new Stack<>();
        Stack<Boolean> boolStack=new Stack<>();
        value=new boolean[vh.length];

        for(int i=0;i<vh.length;i++){
            for (char c : pf)
                switch (c) {
                    case '!':
                    case '*':
                    case '+':
                        charStack.push(c);
                        break;//存储运算符号
                    case ')':
                        cal(charStack, boolStack);
                        break;//进行运算
                    default:
                        boolStack.push(vh[i][(int) c - 65]);//存储bool
                }
            value[i]=boolStack.pop();
        }
        if(!boolStack.isEmpty()||!charStack.isEmpty())System.out.println("出现了bug，运算不完全");
    }//运算

    //辅助计算的方法
    private void cal(Stack<Character> charStack,Stack<Boolean> boolStack){
        char ch=charStack.pop();
        switch (ch){
            case '+':boolStack.push(or(boolStack.pop(),boolStack.pop()));break;
            case '*':boolStack.push(and(boolStack.pop(),boolStack.pop()));break;
            case '!':boolStack.push(not(boolStack.pop()));break;
        }
    }
    private char[] variables(){
        char[] buffer=new char[26];//先缓存
        int i,j,k;
        for(i=0,j=0;i<pf.length;i++)
            if(pf[i]>='A'&&pf[i]<='Z')buffer[j++]=pf[i];
        //删除重复
        for(i=0;i<j-1;i++){
            for(k=i+1;k<j;k++)
                if(buffer[i]==buffer[k]){
                    char t=buffer[k];
                    buffer[k]=buffer[--j];
                    buffer[j]=t;
                    k--;
                }
        }
        char[] variables=new char[j];
        System.arraycopy(buffer,0,variables,0,j);
        Arrays.sort(variables);
        return variables;
    }//返回所有变元升序组成的数组
    private boolean[][] variableHash(char[] variable){
        int row=(int)Math.pow(2,variable.length),list=26;
        boolean[][] table=new boolean[row][list];
        for(int i=0;i<variable.length;i++){
            boolean t=false;
            int n=(int)Math.pow(2,variable.length-1-i);//转换TF的间隔
            int index=(int)variable[i]-65;//下标
            for(int j=0;j<row;j++) {
                if(j!=0&&j%n==0)t=!t;
                table[j][index]=t;
            }
        }
        return table;
    }//返回存有所有输入的哈希表
    //辅助修改字符数组方法
    private void insertChar(int index,char ch){//用于往字符数组插入元素
        len++;
        for (int i=len;i>index;i--) {
            pf[i] = pf[i - 1];
        }
        pf[index]=ch;
    }
    private void deleteChar(int index){
        len--;
        for(;index<len;index++)
            pf[index]=pf[index+1];
        pf[len]=' ';
    }
    private int fineRight(int index){//index是左括号的下标，返回该括号对应的右括号下标
        int c=0;
        for(;index<len;index++){
            if(pf[index]=='(')c++;
            if(pf[index]==')') {
                c--;
                if(c==0)return index;
            }
        }
        return -1;
    }
    private int fineLeft(int index){//index是右括号的下标，返回该括号对应的左括号下标
        int c=0;
        for(;index>=0;index--){
            if(pf[index]==')')c++;
            if(pf[index]=='(') {
                c--;
                if(c==0)
                    return index;
            }
        }
        return -1;
    }

    //与或非运算
    private static boolean and(Boolean p, Boolean q) {return p && q;}
    private static boolean or(Boolean p, Boolean q) {return p || q;}
    private static boolean not(Boolean p) {return !p;}
}