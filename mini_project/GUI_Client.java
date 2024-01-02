package mini_project;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.ws4d.coap.core.CoapClient;
import org.ws4d.coap.core.CoapConstants;
import org.ws4d.coap.core.connection.BasicCoapChannelManager;
import org.ws4d.coap.core.connection.api.CoapChannelManager;
import org.ws4d.coap.core.connection.api.CoapClientChannel;
import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.enumerations.CoapRequestCode;
import org.ws4d.coap.core.messages.api.CoapRequest;
import org.ws4d.coap.core.messages.api.CoapResponse;
import org.ws4d.coap.core.rest.CoapData;
import org.ws4d.coap.core.tools.Encoder;


public class GUI_Client extends JFrame implements CoapClient{
	// Button 객체 생성
	JButton btn_obs = new JButton("Observe Temperature and Humidit");	// 온습도 값을 Observe 하는 버튼
	JButton btn_input = new JButton("Input Emotion");	// 감정을 입력하면 그 값을 전송하는 버튼
	
	// Label 및 TextArea 객체 생성
	JLabel payload_label = new JLabel("Your current feeling (기쁨, 슬픔, 화남, 불안 중 하나 선택)");	// 감정 입력의 Label
	JTextArea payload_text = new JTextArea("", 1,1);	// 감정을 입력하는 부분 스크롤바 없음
	JTextArea display_text = new JTextArea();	// 감정과 대기 상태에 따른 추천된 영화를 출력하는 부분
	JScrollPane display_text_jp  = new JScrollPane(display_text);	// 스크롤바 있음
	JLabel display_label = new JLabel("Display");	// Display Label
	
	CoapClientChannel clientChannel = null;

	/* GUI_Client 생성자 */
	public GUI_Client (String serverAddress, int serverPort) {
		//제목 설정
		super("Mini Project GUI client");
		
		//레이아웃 설정
		this.setLayout(null);
		String sAddress = serverAddress;	// 서버 IP주소
		int sPort = serverPort;	// 서버 포트번호

		// CoAP 통신을 위한 CoapChannelManager 객체 생성
		CoapChannelManager channelManager = BasicCoapChannelManager.getInstance();	
	
		// Client와 Server 연결
		try {
			clientChannel = channelManager.connect(this, InetAddress.getByName(sAddress), sPort);	// client를 서버 IP와 Port에 연결
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);	// 프로세스 강제 종료
		}
		
		// clientChannel 설정이 안되어 있으면 종료
		if (null == clientChannel) {
			return;
		}
		

		// Button 위치 설정
		btn_obs.setBounds(20, 670, 300, 50);
		btn_input.setBounds(430, 670, 300, 50);

		
		/* Observe 버튼 클릭 시 이벤트 정의 */
		btn_obs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = "/dht";	// URI 경로 (온습도 센서)
				
				// request 메시지 설정
				CoapRequest request = clientChannel.createRequest(CoapRequestCode.GET, path, true);	// /dht 경로로 메소드는 GET, CON 메시지로 요청
				request.setToken(Encoder.StringToByte("ObToken"));	// Observe Token 값 설정
				request.setObserveOption(0);	// Sequence Number 0부터 시작
				
				clientChannel.sendMessage(request);		// request 메시지 전송
				
				// display 부분에 해당 텍스트들 추가
				display_text.append(System.lineSeparator());	// 줄 바꿈
				display_text.append("Observe the Conditions of the Atmosphere");
				display_text.append(System.lineSeparator());	// 줄 바꿈
			}
		});

		
		/* Input 버튼 클릭 시 이벤트 정의 */
		btn_input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String path = "/lcd";	// URI 경로 (LCD Display 장치)
				String payload = RecommendMovieList.emotionRecommend(payload_text.getText());	// request로 보낼 payload 값 : 입력한 기분에 따른 추천 영화
				
				// 잘못 입력된 payload 값일 때
				if (payload.equals("Incorrectly Inputted")) {
					display_text.append(System.lineSeparator());	// 줄 바꿈
					display_text.append(payload);	// Display 부분에 잘못 입력되었다는 것을 출력
					display_text.append(System.lineSeparator());	// 줄 바꿈
					
					return;
				}
				
				// request 메시지 설정
				CoapRequest request = clientChannel.createRequest(CoapRequestCode.PUT, path, true);	// /lcd 경로로 메소드는 PUT, CON 메시지로 요청
				request.setPayload(new CoapData(payload, CoapMediaType.text_plain));	// payload로 보낼 값과 타입 설정
				
				clientChannel.sendMessage(request);		// request 메시지 전송
				
				// display 부분에 해당 텍스트들 추가
				display_text.append(System.lineSeparator());	// 줄 바꿈
				display_text.append("Recommended Movie (" + payload_text.getText() + ") : " + payload);	// "Recommended Movie (Emotion) : Movie name" 형태로 출력
				display_text.append(System.lineSeparator());	// 줄 바꿈
			}
		});
		
		// payload_label 위치 및 글꼴 설정
		payload_label.setBounds(20, 570, 350, 30);
		payload_text.setBounds(20, 600, 440, 30);
		payload_text.setFont(new Font("arian", Font.BOLD, 15));
		
		// display_label 위치 및 글꼴 설정
		display_label.setBounds(20, 10, 100, 20);
		display_text.setLineWrap(true);
		display_text.setFont(new Font("arian", Font.BOLD, 15));
		display_text_jp.setBounds(20, 40, 740, 430);
		
		// Button 및 Label들을 화면창에 추가
		this.add(btn_obs);
		this.add(btn_input);
		this.add(payload_label);
		this.add(payload_text);
		this.add(display_text_jp);
		this.add(display_label);

		// 프레임 크기 지정	
		this.setSize(800, 800);

		// 프레임 보이기
		this.setVisible(true);

		//swing에만 있는 X버튼 클릭시 종료
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

	/* 연결 실패 시 동작 */
	@Override
	public void onConnectionFailed(CoapClientChannel channel, boolean notReachable, boolean resetByServer) {
		System.out.println("Connection Failed");
		System.exit(-1);	// 프로세스 강제 종료
	}

	/* Server에서 온 Observe 응답 메시지 처리 */
	@Override
	public void onResponse(CoapClientChannel channel, CoapResponse response) {
		if (Encoder.ByteToString(response.getToken()).equals("ObToken")) {	// 응답 메시지의 Token 값이 Observe Token 값인 "ObToken"이면
			controlLCD(Encoder.ByteToString(response.getPayload()));	// 받은 응답 값(대기 상태)에 따른 추천 영화를 LCD Display 센서로 전송
		}
	}
	
	/* LCD Display 센서로 전송 */
	public void controlLCD(String state) {
		// 대기 상태 값에 오류가 있으면 건너뛰기
		if (state.equals("Error")) {
			return;
		}
		
		// URI 경로가 /lcd로 put 메소드의 CON 요청
		CoapRequest request = clientChannel.createRequest(CoapRequestCode.PUT, "/lcd", true);	// /lcd 경로로 메소드는 PUT, CON 메시지로 요청
		request.setPayload(new CoapData(RecommendMovieList.weatRecommend(state), CoapMediaType.text_plain));	// payload 값은 대기 상태에 따른 추천 영화
		
		clientChannel.sendMessage(request);		// request 메시지 전송
		
		displayRequest(request, state);	// 추천 영화와 대기 상태를 표시
	}

	
	/* 멀티 캐스트 요청에 대한 응답 메시지 처리 */
	@Override
	public void onMCResponse(CoapClientChannel channel, CoapResponse response, InetAddress srcAddress, int srcPort) {
		// TODO Auto-generated method stub
	}
	
	/* Observe로 받은 대기 상태에 따른 추천 영화 출력 */
	private void displayRequest(CoapRequest request, String state) {
		display_text.append(System.lineSeparator());	// 줄바꿈
		display_text.append("Recommended Movie (" + state + ") : " + Encoder.ByteToString(request.getPayload()));	// "Recommended Movie (state) : Movie name" 형태로 출력
		display_text.append(System.lineSeparator());	// 줄바꿈
	}
	
	public static void main(String[] args){
		// 프레임 열기
		GUI_Client gui = new GUI_Client("192.168.0.9", CoapConstants.COAP_DEFAULT_PORT);	// 192.168.0.9 IP 주소를 가진 CoAP 서버와 통신
	}
	
	
}
