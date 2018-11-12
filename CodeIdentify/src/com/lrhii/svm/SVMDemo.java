package com.lrhii.svm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import libsvm.svm;
import libsvm.svm_model;

public class SVMDemo {
	private static svm_model model = null;
	
	static{
		InputStream in = SVMDemo.class.getResourceAsStream("/model/model.txt");
		BufferedReader br=new BufferedReader(new InputStreamReader(in));  
		try {
			model = svm.svm_load_model(br);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
    public static void main(String[] args) throws IOException {  
                String[] arg = { "E:\\svmTmp\\traindata.txt", //训练集  
                        "E:\\svmTmp\\model.txt" }; // 存放SVM训练模型  
  
  
        String[] parg = { "E:\\svmTmp\\testdata.txt", //测试数据  
                           "E:\\svmTmp\\model.txt", // 调用训练模型  
                           "E:\\svmTmp\\predict.txt" }; //预测结果  
        System.out.println("........SVM运行开始..........");  
//        long start=System.currentTimeMillis();   
        svm_train.main(arg); //训练  
//        System.out.println("用时:"+(System.currentTimeMillis()-start));   
        //预测  
//        svm_model model = svm.svm_load_model("E:\\svmTmp\\model.txt");
        svm_predict.main(parg);
//        double res = svm.svm_predict(model, svm_predict.getNode("1 0:8 1:14 2:0 3:8 4:3 5:15 6:2 7:12 8:0 9:12 10:11 11:9 12:0 13:8 14:16 15:5"));
//        System.out.println(res);
    	
    																													
    }
    
    /**
     * 预测结果
     * @param line
     * @return
     */
    public static char predict(String line){
    	double res = -9999;
    	res = svm.svm_predict(model, svm_predict.getNode(line));
//			System.out.println(res);
		return (char)res;
    }
    
    /**
     * 输出符合SVM标准的特征字符串
     * @param array
     * @return
     */
    public static String arrayToString(int[] array){
    	String str = "";
    	for (int i = 0; i < array.length; i++) {
    		if(i==array.length-1){
    			str += i + ":" + array[i]+"";
    		}else{
    			str += i + ":" + array[i]+" ";
    		}
		}
    	return str;
    }
    
}  