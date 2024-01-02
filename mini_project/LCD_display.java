package mini_project;

import java.io.IOException;
import java.util.List;

import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.rest.BasicCoapResource;
import org.ws4d.coap.core.rest.CoapData;
import org.ws4d.coap.core.tools.Encoder;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import week07.I2CLCD;


public class LCD_display extends BasicCoapResource{	// CoAp 통신을 위해 BasicCoapResource 클래스를 상속 받음
	private String movieNm = "";	// 추천할 영화명
	private I2CBus bus;	// I2C Bus 객체 생성
	private I2CDevice dev;	// I2C Device 객체 생성
	private I2CLCD lcd;	// I2CLCD Class 객체 생성, 생성한 I2C Device 객체를 인자값으로 넣어줌
    
	
	/* LCD_display 객체 생성자 */
	private LCD_display(String path, String value, CoapMediaType mediaType) {
		super(path, value, mediaType);
		
		try {
			bus = I2CFactory.getInstance(I2CBus.BUS_1);	// I2C Bus 객체 생성
			dev = bus.getDevice(0x27);	// I2C Device 객체 생성
			lcd = new I2CLCD(dev);	// I2CLCD Class 객체 생성, 생성한 I2C Device 객체를 인자값으로 넣어줌
			
			lcd.init();	// 초기화
			lcd.backlight(true);	// Back light on
			
			lcd.clear();	// LCD 디스플레이 화면 지우기
	        lcd.display_string("Recommend Movie :", 1);	// LCD 첫 번재 라인에 표시
			lcd.display_string(movieNm, 2);	// LCD 두 번째 라인에 표시
			
			System.out.println(this.movieNm);
		} catch (UnsupportedBusNumberException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* LCD_display 객체 생성자 */
	public LCD_display() {
		this("/lcd", "", CoapMediaType.text_plain);	// 이 생성자는 매개변수가 없으므로 uri, 초기값, CoAP Media Type 값 설정
		
		try {
			bus = I2CFactory.getInstance(I2CBus.BUS_1);	// I2C Bus 객체 생성
			dev = bus.getDevice(0x27);	// I2C Device 객체 생성
			lcd = new I2CLCD(dev);	// I2CLCD Class 객체 생성, 생성한 I2C Device 객체를 인자값으로 넣어줌
			
			lcd.init();	// 초기화
			lcd.backlight(true);	// Back light on
		} catch (UnsupportedBusNumberException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* GET 메시지 수신 시 */
	@Override
	public synchronized CoapData get(List<String> query, List<CoapMediaType> mediaTypesAccepted) {
		return get(mediaTypesAccepted);
	}
	
	/* GET 메시지 수신 시 */
	@Override
	public synchronized CoapData get(List<CoapMediaType> mediaTypesAccepted) {
		return new CoapData(Encoder.StringToByte(this.movieNm), CoapMediaType.text_plain);
	}

	/* LCD Display의 출력하는 값 변경 */
	@Override
	public synchronized boolean setValue(byte[] value) {
		this.movieNm = Encoder.ByteToString(value);
		
		lcd.clear();	// LCD 디스플레이 화면 지우기
        lcd.display_string("Recommend Movie :", 1);	// LCD 첫 번재 라인에 표시
		lcd.display_string(movieNm, 2);	// LCD 두 번째 라인에 표시
		
		System.out.println(this.movieNm);
		
		return true;
	}
	
	/* PUT 메시지 수신 시 */
	@Override
	public synchronized boolean put(byte[] data, CoapMediaType type) {
		return this.setValue(data);
	}
	
	/* POST 메시지 수신 시 */
	@Override
	public synchronized boolean post(byte[] data, CoapMediaType type) {
		return this.setValue(data);
	}

	/* Resource Type 명시 */
	@Override
	public synchronized String getResourceType() {
		return "Raspberry pi 4 LCD Display";
	}

}