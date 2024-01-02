package mini_project;

import org.ws4d.coap.core.rest.CoapResourceServer;


public class CoAP_Server {
	private static CoAP_Server coapServer;	// CoAP Server
	private CoapResourceServer resourceServer;	// CoAP Resource Server
	
	public static void main(String[] args) {
		coapServer = new CoAP_Server();	// CoAP Server 객체 생성
		coapServer.start();	// 서버 실행
	}

	/* CoAP Server 실행 */
	public void start() {
		System.out.println("===Run Test Server ===");

		// 리소스 서버 생성
		if (this.resourceServer != null)	this.resourceServer.stop();	// 처음 서버를 시작하는 것이기에 리소스 서버가 null 값이 아니면 리소스 서버 중지
		this.resourceServer = new CoapResourceServer();	// 그렇지 않으면 리소스 서버 생성


		// 리소스 객체 생성
		LCD_display lcd = new LCD_display();	// LCD Display 객체 생성
		DHT11_sensor dht = new DHT11_sensor();		// DHT11 센서 객체 생성
		dht.setObservable(true);	// dht 객체의 Observing 가능
		
		// 서버에 리소스 추가(등록)
		this.resourceServer.createResource(lcd);	// LCD_display 리소스 등록	(LCD Display)
		this.resourceServer.createResource(dht);	// DHT11_sensor 리소스 등록	(온습도 센서)
		dht.registerServerListener(resourceServer);	// CoAP 리소스 서버에 Observe 하려는 dht 객체를 Observe 등록
		
		
		// 리소스 서버 실행
		try {
			this.resourceServer.start();	// 리소스 서버 실행
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// Observe 10초 간격으로 Client로 변화한 DHT11 값 전송
		while(true) {
			try {
				Thread.sleep(10000);	// 10초간 정지
				dht.optional_changed();	// 값이 변할 때만 전송
			}catch (Exception e) {
				// TODO: handle exception
			}		
		}
	}
}

