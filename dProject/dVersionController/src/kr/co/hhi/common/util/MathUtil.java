package kr.co.hhi.common.util;

import java.util.Collections;
import java.util.List;

public class MathUtil {
	
	
	// --- �Էµ�����-----//
	
	static double data[]={
			4.150,
			4.660,
			4.520,
			4.580,
			4.010,
			4.880,
			3.960,
			3.840,
			3.860 };
	
	static double USL=6.0, LSL=4.0;
	
	// --- �Էµ�����-----//
	
	public static void DoMyCode() {
		
		double mean = Get_Mean(9, data);
		double std = Get_StandardDeviation(9, data);

	//------------------------------------------------------------
	//---------   case1: ����(USL), ����(LSL)�� ��� �����ϴ� ���
		
		double Z_u = (USL-mean)/std;
		double Z_l = (mean - LSL) / std;


		double P_u = 1- phi(Z_u);  //���� �ҷ���
		double P_l = phi(Z_l);    // ���� �ҷ���

		double P_total = P_u + P_l;   // �� �ҷ���

		
		double Z_bench =  NormalCDFInverse(P_total);

//		cout << "Z_bench (with USL & LSL) is : " << Z_bench << "\n";
	//---------  end of case1
	//------------------------------------------------------------



	//------------------------------------------------------------
	//---------  case2: ���� USL�� �����ϴ� ��� (�������� LSL�� '*' �� ���)

		Z_bench = (USL-mean)/std;

//		cout << "Z_bench (with USL only) is : " << Z_bench << "\n";

	//---------  end of case2
	//------------------------------------------------------------



	//------------------------------------------------------------
	//---------  case3: ���� LSL�� �����ϴ� ���(�������� USL�� '*' �� ���)

		Z_bench = (mean - LSL)/std;

//		cout << "Z_bench (with LSL only) is : " << Z_bench << "\n";

	//---------  end of case3
	//------------------------------------------------------------
		
		System.out.println("����: " + data.length);
		System.out.println("���: " + mean);
		System.out.println("ǥ��: " + std);
		System.out.println("Z_u: " + Z_u);
		System.out.println("Z_l: " + Z_l);
		System.out.println("P_u: " + P_u);
		System.out.println("P_l: " + P_l);
		System.out.println("P_total: " + P_total);
		System.out.println("Z_bench: " + Z_bench);
		//System.out.println("cont: " + cont);
	}
	
	
	//��� ���ϱ�
	public static double Get_Mean(double N_data, double data[]) {
		
		double sum = 0;
		
		for (int i = 0; i < N_data; i++) {
			sum += data[i];
		}
		
		return sum/N_data;
	}
	
	//ǥ������ ���ϱ� (���õ����Ϳ� ���� ������ ǥ������)
	public static double Get_StandardDeviation(double N_data, double data[]) {

		double mean = Get_Mean(N_data, data);
		double tmp_sum = 0;

		for (int i = 0; i < N_data; i++){
			tmp_sum+=(data[i]-mean)*(data[i]-mean);
		}
		
		//return sqrt(tmp_sum/N_data); // ���� ��ü ǥ�� ����
		return Math.sqrt(N_data/(N_data-1)) * Math.sqrt(tmp_sum/N_data);  // ������ ǥ������ ���
	}

	
	//���� �Լ�
	public static double phi(double x) {
		// constants
		double a1 =  0.254829592;
		double a2 = -0.284496736;
		double a3 =  1.421413741;
		double a4 = -1.453152027;
		double a5 =  1.061405429;
		double p  =  0.3275911;

		// Save the sign of x
		int sign = 1;
		if (x < 0)
			sign = -1;
		x = Math.abs(x)/Math.sqrt(2.0);

		// A&S formula 7.1.26
		double t = 1.0/(1.0 + p*x);
		double y = 1.0 - (((((a5*t + a4)*t) + a3)*t + a2)*t + a1)*t*Math.exp(-x*x);

		return 0.5*(1.0 + sign*y);
	}
	

	public static double RationalApproximation(double t) {
		// Abramowitz and Stegun formula 26.2.23.
		// The absolute value of the error should be less than 4.5 e-4.
		double c[] = {2.515517, 0.802853, 0.010328};
		double d[] = {1.432788, 0.189269, 0.001308};
		return t - ((c[2]*t + c[1])*t + c[0]) / (((d[2]*t + d[1])*t + d[0])*t + 1.0);
	}
	
	/**
	 * Z-value ���ϴ� ����
	 * (0���� �۰ų� 1���� ū���� ������ �ȵȴ�)
	 * @param p
	 * @return
	 */
	public static double NormalCDFInverse(double p) {
		if (p <= 0.0 || p >= 1.0) {
			//std::stringstream os;
			//os << "Invalid input argument (" << p 
			//	<< "); must be larger than 0 but less than 1.";
			//throw std::invalid_argument( os.str() );
		}

		// See article above for explanation of this section.
		if (p < 0.5) {
			// F^-1(p) = - G^-1(p)
			return -RationalApproximation( Math.sqrt(-2.0*Math.log(p)) );
		} else {
			// F^-1(p) = G^-1(1-p)
			return RationalApproximation( Math.sqrt(-2.0*Math.log(1-p)) );
		}
	}
	
/*	public int _tmain(int argc, String argv[], String envp[]) {
		int nRetCode = 0;

		// initialize MFC and print and error on failure
		if (!AfxWinInit(::GetModuleHandle(NULL), NULL, ::GetCommandLine(), 0)) {
			// TODO: change error code to suit your needs
			System.out.println("Fatal Error: MFC initialization failed\n");
			nRetCode = 1;
		} else {
			DoMyCode();
		}
		
		return nRetCode;
	}*/

//	/**
//	 * 
//	 * @param format  ex #.##
//	 * @param value double type
//	 * @return
//	 */
//	public static String getDecimalFormat(String format, double value){
//		
//		String result	=	"";
//		
//		DecimalFormat form = new DecimalFormat(format);
//
//	    result	=	form.format(value); 
//	    
//	    return result;
//	    
//	}
	
	public static void main(String[] args) {
		DoMyCode();
	}
	
	public static double getMedian(List<Double> numArray){
		Collections.sort(numArray);
		double median;
		if (numArray.size() % 2 == 0)
			median = ((double)numArray.get(numArray.size()/2) + (double)numArray.get(numArray.size()/2 - 1))/2;
		else
		    median = (double) numArray.get(numArray.size()/2);
		
		return median;
	}
	
	public static double getMinimum(List<Double> numArray){
//		System.out.println("------");
//		for(int i = 0; i < numArray.size(); i++){
//			System.out.println(numArray.get(i));
//		}
//		System.out.println("------");
		Collections.sort(numArray);
		return numArray.get(0);
	}
	
	public static double getMaximum(List<Double> numArray){
		Collections.sort(numArray);
		return numArray.get(numArray.size() - 1);
	}
	
	/**
	 * ���޵� ������ �迭�� �������(1/4, 2/4, 3/4) ���� ����
	 * @param data
	 * @param percentile
	 * ex) excelPercentile(entres, 1) : 1/4 �������
	 *     excelPercentile(entres, 2) : 2/4 �������
	 *     excelPercentile(entres, 3) : 3/4 �������
	 * @return ���� ������� ��
	 */
	public static double getQuartiles(List<Double> numArray, int percentile){
		Collections.sort(numArray);
	    double index = (double)((double)percentile / 4) * ((double)numArray.size()-1);
	    int lower = (int)Math.floor(index);
	    if(lower<0) { // should never happen, but be defensive
	       return numArray.get(0);
	    }
	    if(lower>=numArray.size()-1) { // only in 100 percentile case, but be defensive
	       return numArray.get(numArray.size()-1);
	    }
	    double fraction = index-lower;
	    // linear interpolation
	    double result=numArray.get(lower) + fraction*(numArray.get(lower+1)-numArray.get(lower));
	    return result;
	}
	
	/**
	 * z-value ��갪�� ����
	 * @param usl ���Ѱ�
	 * @param lsl ���Ѱ�
	 * @param avg ������ ���
	 * @param stddev ������ ǥ������
	 * @return ���� z-value
	 */
	public static Double getZvalue(Double usl, Double lsl, Double avg, Double stddev){
		
		if(usl == null || lsl == null || avg == null || stddev == null){
			return null;
		}
		System.out.println("---");
		System.out.println(usl);
		System.out.println(lsl);
		System.out.println(avg);
		System.out.println(stddev);
		System.out.println("===");
		
		double z_u = (usl - avg) / stddev;
		double z_l = (avg - lsl) / stddev;
		double p_u = 1- MathUtil.phi(z_u);
		double p_l = MathUtil.phi(z_l);
		double p = p_u + p_l;
		Double z_value = MathUtil.NormalCDFInverse(p);
		
		System.out.println(z_u);
		System.out.println(z_l);
		System.out.println(p_u);
		System.out.println(p_l);
		System.out.println(p);
		System.out.println(z_value);
		
		if(Double.isNaN(z_value) || Double.isInfinite(z_value)){
			return null;
		}else{
			return z_value;
		}
		
	}
	
	/**
	 * �ִ����� ���ϱ�
	 * @param a
	 * @param b
	 * @return int
	 */
	public static int greatestCommonDivisor(int a, int b) {
		   if (b==0) return a;
		   return greatestCommonDivisor(b, a%b);
	}
}

