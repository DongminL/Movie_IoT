package mini_project;

import java.util.List;

import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.rest.BasicCoapResource;
import org.ws4d.coap.core.rest.CoapData;
import org.ws4d.coap.core.tools.Encoder;

public class DHT11_sensor extends BasicCoapResource{	// CoAp 통신을 위해 BasicCoapResource 클래스를 상속 받음

	private String state = "";	// 대기 상태 
	DHT11 dht = new DHT11();	// 온습도 장치 객체 생성 
	
	/* DHT11_sensor 생성자 */
	private DHT11_sensor(String path, String value, CoapMediaType mediaType) {
		super(path, value, mediaType);
	}

	/* DHT11_sensor 생성자 */
	public DHT11_sensor() {
		this("/dht", "", CoapMediaType.text_plain);	// URI, state의 초기값, CoAp Media Type 설정
	}

	
	/* GET 메시지 수신 시 */
	@Override
	public synchronized CoapData get(List<String> query, List<CoapMediaType> mediaTypesAccepted) {
		return get(mediaTypesAccepted);
	}
	
	/* GET 메시지 수신 시 */
	@Override
	public synchronized CoapData get(List<CoapMediaType> mediaTypesAccepted) {
		float[] sensing_data = dht.getData(15);	// GPIO 15번 Pin에 연결된 온습도 센서의 데이터를 가져옴
		this.state = figureToState(sensing_data);	// DHT11 센서 값은 대기 상태 값으로 변환
		
		return new CoapData(Encoder.StringToByte(this.state), CoapMediaType.text_plain);	// 대기 상태 값을 
	}
	
	/* state 값이 변경될 때만 서버로 전송 */
	public synchronized void optional_changed() {
		float[] sensing_data = dht.getData(15);	// GPIO 15번 Pin에 연결된 온습도 센서의 데이터를 가져옴
		String result = figureToState(sensing_data);	// 센서로 받아온 수치들을 대기 상태(텍스트)로 변환
		
		// 온도값 비교 후, 온도값 변경 시에만 Observe 응답 전송
		if (result.equals(this.state)) {
			System.out.println("The State has not changed. ");
		}
		else {
			this.state = result;
			this.changed(this.state);
			
			System.out.println(this.state);
		}
	}
	
	/* 온도와 습도 값에 따른 대기 상태를 Text 형태로 변환 */
	public String figureToState(float[] data) {
		float humi = data[0];	// 상대 습도
		float temp = data[1];	// 온도 (섭씨)
		String result = "";	// 대기 상태
		
		// Cheaksum error로 값 변경하지 않기
		if (humi == -99f || temp == -99f) {
			return this.state;
		}
		
		// 값에 따라 대기 상태 구분
		if (humi > 70f && temp > 23f) {
			result = "고온다습";
		}
		else if (humi > 70f && temp > 7f) {
			result = "온화다습";
		}
		else if (humi > 70f && temp <= 7f) {
			result = "한랭다습";
		}
		else if (humi > 50f && temp > 23f) {
			result = "고온";
		}
		else if (humi > 50f && temp > 7f) {
			result = "온화";
		}
		else if (humi > 50f && temp <= 7f) {
			result = "한랭";
		}
		else if (humi <= 50f && temp > 23f) {
			result = "고온건조";
		}
		else if (humi <= 50f && temp > 7f) {
			result = "온화건조";
		}
		else if (humi <= 50f && temp <= 7f) {
			result = "한랭건조";
		}
		
		return result;
	}

	/* 대기 상태 변경 */
	@Override
	public synchronized boolean setValue(byte[] value) {
		this.state = Encoder.ByteToString(value);
		
		return true;
	}
	
	/* POST 메시지 수신 시 */
	@Override
	public synchronized boolean post(byte[] data, CoapMediaType type) {
		return this.setValue(data);	// data로 값 변경
	}

	/* PUT 메시지 수신 시 */
	@Override
	public synchronized boolean put(byte[] data, CoapMediaType type) {
		return this.setValue(data);	// data로 값 변경
	}

	/* Resource Type 명시 */
	@Override
	public synchronized String getResourceType() {
		return "Raspberry pi 4 DHT11 Sensor";
	}
	
}
