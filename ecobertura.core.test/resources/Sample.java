public class Sample {
	private String hello;
	
	public Sample() {
		hello = "hello world";
	}
	
	public void print() {
		System.out.println(hello);
	}

	public static void main(String[] args) {
		Sample sample = new Sample();
		sample.print();
	}
}