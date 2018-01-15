import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProblemProducerCustomer {
	public static void main(String[] args) {
		Warehouse warehouse = new Warehouse();
		ExecutorService pool = Executors.newCachedThreadPool();
		pool.execute(new Customer(50, warehouse));
		pool.execute(new Producer(20, warehouse));
		pool.execute(new Producer(30, warehouse));
		pool.execute(new Producer(20, warehouse));
		pool.shutdown();
	}
	public static class Warehouse{
		int num = 0;
		final int max = 100;

		public synchronized void product(int x){
			while (num + x > max){
				try{
					System.out.println ("超出容量，等待消费");
					wait();
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			num = num + x;
			System.out.println("已生产");
			notifyAll();
		}
		public synchronized void consume(int x){
			while (num - x < 0){
				try{
					System.out.println("容量不足，等待生产");
					wait();
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			num = num - x;
			System.out.println("已经消费");
			notify();
		}

		public int getNum(){
			return num;
		}
	}


	public static class Producer implements Runnable{
		int speed;
		Warehouse warehouse;

		public  Producer(int speed, Warehouse warehouse){
			this.speed = speed;
			this.warehouse = warehouse;
		}

		@Override
		public void run(){
			warehouse.product(speed);
		}
	}

	public static class Customer implements Runnable{
		int speed;
		Warehouse warehouse;

		public  Customer(int speed, Warehouse warehouse){
			this.speed = speed;
			this.warehouse = warehouse;
		}
		@Override
		public void run(){
			warehouse.consume(speed);
		}
	}
}
 