package newFunction;

import java.util.List;

public class Functions {

	public static double f1(List<Double> x) {
		double result = x.get(0);
		int n = x.size();
		
		double sum = 0;
		double count = 0; // length of J
		for (int j = 3; j < n; j += 2) {
			double pow = 0.5 * (1.0 + (3 * (j - 2)) / (n + 2));
			double term = Math.pow(x.get(j) - Math.pow(x.get(0), pow), 2);
			sum += term;
			count++;
		}
		
		return result + (2 / count) * sum;
	}

	public static double f2(List<Double> x) {
		double result = 1 - Math.sqrt(x.get(0));
		int n = x.size();
		
		double sum = 0;
		double count = 0; // length of J
		for (int j = 2; j < n; j += 2) {
			double pow = 0.5 * (1.0 + (3 * (j - 2)) / (n + 2));
			double term = Math.pow(x.get(j) - Math.pow(x.get(0), pow), 2);
			sum += term;
			count++;
		}
		
		return result + (2 / count) * sum;
	}
	
	public static double f1(double[] x) {
		double result = x[0];
		int n = x.length;
		
		double sum = 0;
		double count = 0; // length of J
		for (int j = 3; j < n; j += 2) {
			double pow = 0.5 * (1.0 + (3 * (j - 2)) / (n + 2));
			double term = Math.pow(x[j] - Math.pow(x[0], pow), 2);
			sum += term;
			count++;
		}
		
		return result + (2 / count) * sum;
	}

	public static double f2(double[] x) {
		double result = 1 - Math.sqrt(x[0]);
		int n = x.length;
		
		double sum = 0;
		double count = 0; // length of J
		for (int j = 2; j < n; j += 2) {
			double pow = 0.5 * (1.0 + (3 * (j - 2)) / (n + 2));
			double term = Math.pow(x[j] - Math.pow(x[0], pow), 2);
			sum += term;
			count++;
		}
		
		return result + (2 / count) * sum;
	}
	
	public static double f1Norm(double[] x, double min, double max) {
		return f1(x) / (max - min);
	}

	public static double f2Norm(double[] x, double min, double max) {
		return f2(x) / (max - min);		
	}

//---------------------------------------------EDC Part-------------------------------------------------


	public static double MFT(List<EDC> edc){
		double sum = 0.0;
		double FTO = 0.99;
		for(int i = 0;i<edc.size();i++){
			sum += (1-edc.get(i).getError_Coverage())/(1-FTO);
		}
		return sum/edc.size();
	}

	public static double HDFT(List<EDC> edc){
		double FTO = 0.99;  //目标错误覆盖率
		double average = MFT(edc);
		double sum = 0;
		double FT = 0;
		for (int i = 0;i<edc.size();i++){
			FT = (1 - edc.get(i).getError_Coverage()) / (1 - FTO);
			sum += (FT - average)*(FT - average);
		}
		sum = Math.sqrt(sum / edc.size());
		return sum;
	}

	public static double obj(List<EDC> edc){
		double sum = 0.9*MFT(edc) + 0.1*HDFT(edc);
		return sum;
	}

	//求解平均错误覆盖率
	public static double Average_Coverage( List<EDC> edc)
	{
		double sum = 0.0;
		for (int i = 0; i < edc.size(); i++)
		{
			sum += edc.get(i).getError_Coverage();
		}
		return sum / (edc.size());
	}

	//求解异构度
	public static double Calculate_std(List<EDC> edc)
	{
		double average = Average_Coverage(edc);
		double sum = 0;
		for (int i = 0; i < edc.size(); i++)
		{
			sum += (edc.get(i).getError_Coverage() - average)*(edc.get(i).getError_Coverage() - average);
		}
		sum = Math.sqrt(sum / edc.size());
		return sum;
	}

	public static double f1Test(double[] x){
		return 0.0;
	}

	public static double f2Test(double[] x){
		return 0.0;
	}
}
