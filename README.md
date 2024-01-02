# CoAP 기반의 감정 및 날씨에 따른 영화 추천 IoT
--------------------------------
## 프로젝트 배경 및 목적
* **감정 날씨 등에 따른 영화 추천 기능이 부족함**
* **영화는 종합예술이기에 효율적이고 경제적임**
* **영화를 통해 윤택한 삶을 만듦**
--------------------------------
## 시스템 구현
* **DHT11** : 온도와 상대 습도 감지, Observe Option을 통한 주기적인 모니터링
* **LCD Display** : Client에게 추천 영화 출력
* **CoAP Server** : CoAP 통신을 사용한 Resource 관리
* **CoAP Client/UI** : 현재 감정 입력 및 DHT11의 모니터링 값에 따른추천 영화를 제공 받음
  ![image](https://github.com/DongminL/Movie_IoT/assets/108854865/5750f609-f7c3-4e12-98bf-fce8a0b77e21)
--------------------------------
## 하드웨어 구현
* **Raspberry Pi 3 - Model B+**
* **DHT11**
* **LCD Display**
  ![image](https://github.com/DongminL/Movie_IoT/assets/108854865/bfac6909-b469-459f-8e38-794dd8ca544c)
--------------------------------
## 소프트웨어 구현
  ![image](https://github.com/DongminL/Movie_IoT/assets/108854865/f862e0f2-5e25-4534-b316-80d2549d3be0)
--------------------------------
## 실행 화면
* #### Raspberry Pi
  ![image](https://github.com/DongminL/Movie_IoT/assets/108854865/42b67ff0-400f-4597-bd59-01daceb29339)
* #### GUI
  ![image](https://github.com/DongminL/Movie_IoT/assets/108854865/100ae296-e602-4985-9c4c-c979b9ae840e)
* #### DHT11으로 감지된 대기 상태 값
  ![image](https://github.com/DongminL/Movie_IoT/assets/108854865/bc422864-66ba-4e0e-b753-c097df89f49e)
--------------------------------
## 결과
　이 프로젝트는 매우 제한적인 환경에서 제작을 하였다. 그래서 LCD Display의 경우 긴 문장을 전부 표현할 수 없었다. 그래서 원하는 영화명을 데이터 셋으로 설정하지 못한 한계가 존재했다. 또한 영화 추천의 경우 빅데이터가 필요하지만 이 프로젝트에서는 각 영화 리스트에 두 개의 영화만 설정하여 최소한의 기능만을 보였다. <br><br>
 　다만 이 아이디어를 가지고 실제 IoT 제품을 만들어낸다면, 영화 추천을 넘어 음악, 드라마 등도 추천하여 문화 생활에 투자하는 기회를 줄 수 있는 IoT가 될 것이라고 생각한다. 특히 이 프로젝트에서는 LCD Display 모듈로 간단히 출력했다면 TV나 모니터와 같은 Display와 연결한다면 실용성과 활용도는 더 높아질 것이라고 생각한다. <br><br>
  　이 프로젝트로 바쁜 현대인에게 문화예술을 향유할 수 있는 기회가 되었으면 한다. 이런 기회들이 많아지고 문화예술에 대한 접근성이 조금 더 좁아진다면 우리의 삶은 조금 더 윤택해질 것이라고 생각한다.
