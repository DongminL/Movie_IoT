package mini_project;

import com.pi4j.wiringpi.Gpio;

public class DHT11 {
	private static final int MAXTIMINGS = 85;	// Data 교환이 이루어질 수 있는 최대 경과 시간 정의
	private final int[] dht11_f = { 0, 0, 0, 0, 0 };	// DHT11 data format (5 bytes) 
	
	/* DHT11 생성자 */
	public DHT11() {
		// Setup wiringPi
		if (Gpio.wiringPiSetup() == -1) {
			System.out.println(" ==>> GPIO SETUP FAILED");
			return;
		}
	}
	
	/* 매개변수 pin 값에 따라 해당 GPIO pin에 연결된 온습도 센서의 데이터를 가져옴 */
	public float[] getData(final int pin) {
		int laststate = Gpio.HIGH;	// signal 상태 변화를 알기 위해 기존 상태를 기억
		int j = 0;	// 수신한 Bit의 index counter
		float h = -99;	// 습도
		float c = -99;	// 섭씨 온도
		float f = -99;	// 화씨 온도
		
		// Integral RH, Decimal RH, Integral T, Decimal
		dht11_f[0] = dht11_f[1] = dht11_f[2] = dht11_f[3] = dht11_f[4] = 0;
		
		// 1. DHT11 센서에게 start signal 전달
		Gpio.pinMode(pin, Gpio.HIGH);
		Gpio.digitalWrite(pin, Gpio.LOW);
		Gpio.delay(18);	// 18 ms
		
		// 2. Pull-up -> 수신 모드로 전환 -> 센서의 응답 대기
		Gpio.digitalWrite(pin, Gpio.HIGH);
		Gpio.pinMode(pin, Gpio.INPUT);
		
		// 3. 센서의 응답에 따른 동작
		for (int i = 0; i < MAXTIMINGS; i++) {
			int counter = 0;
			
			while (Gpio.digitalRead(pin) == laststate) {	// Gpio pin 상태가 바뀌지 않으면 대기
				counter++;
				Gpio.delayMicroseconds(1);
				if (counter == 255) {
					break;
				}
			}
			
			laststate = Gpio.digitalRead(pin);
			if (counter == 255) {
				break;
			}
			
			// 각각의 bit 데이터 저장
			if (i >= 4 && i % 2 == 0) {	// 첫 3개의 상태 변화는 무시, last state가 low에서 high로 바뀔 때만 값을 저장
				//data 저장 
				dht11_f[j / 8] <<= 1;	// 0 bit
				if (counter > 16) {
					dht11_f[j / 8] |= 1;	// 1 bit
				}
				
				j++;
			}
		}
		
		/* Checksum 확인
		Check we read 40 bits (8 bit x 5) + verify checksum in the last */
		if (j >= 40 && getChecksum()) {
			h = (float) ((dht11_f[0] << 8) + dht11_f[1]) / 10;
			if (h > 100) {
				h = dht11_f[0];	// for DHT11
			}
			
			c = (float) (((dht11_f[2] & 0x7F) << 8) + dht11_f[3]) / 10;
			if (c > 125) {
				c = dht11_f[2];	// for DHT11
			}
			if ((dht11_f[2] & 0x80) != 0) {
				c = -c;
			}
			
			f = c * 1.8f + 32;
			System.out.println("Humidity = " + h + "% Temperature = " + c + "°C | " + f + "℉");
		}
		else {
			System.out.println("Checksum Error");
		}
		
		float[] result = {h, c, f};
		return result;
	}
	
	/* Checksum 값이 맞는지 확인 */
	private boolean getChecksum() {
		return dht11_f[4] == (dht11_f[0] + dht11_f[1] + dht11_f[2] + dht11_f[3] & 0xFF);
	}
}