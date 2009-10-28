import java.io.*;

public class Sample {
	private String hello;
	
	public Sample() {
		hello = "hello world";
	}
	
	public void print() {
		System.out.println(hello);
		try {
			PrintWriter pw = new PrintWriter(new FileWriter("e:\\hello.txt"));
			pw.println("hello world");
			pw.close();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public static void main(String[] args) {
		Sample sample = new Sample();
		sample.print();
	}
}