package mini_project;

import java.util.Random;

public class RecommendMovieList {
	// 대기에 따른 영화 리스트
	private static String[] h_h_list = {"Florida Project", "It"};	// 고온 다습한 대기에 어울리는 영화 리스트
	private static String[] h_m_list = {"Dune", "Oppenheimer"};	// 고온의 대기에 어울리는 영화 리스트
	private static String[] h_l_list = {"Mad Max", "The Hurt Locker"};	// 고온 건조한 대기에 어울리는 영화 리스트
	private static String[] m_h_list = {"Cure", "The Wailing"};	// 온화하고 다습한 대기에 어울리는 영화 리스트
	private static String[] m_m_list = {"Big Fish", "Cinema Paradiso"};	// 온화한 대기에 어울리는 영화 리스트
	private static String[] m_l_list = {"Her", "Late Autumn"};	// 온화하고 건조한 대기에 어울리는 영화 리스트
	private static String[] l_h_list = {"Love Letter", "Eternal Sunshine"};	// 한랭 다습한 대기에 어울리는 영화 리스트
	private static String[] l_m_list = {"Nostalghia", "The Fortress"};	// 한랭한 대기에 어울리는 영화 리스트
	private static String[] l_l_list = {"Doubt", "Bleak Night"};	// 한랭 건조한 대기에 어울리는 영화 리스트
	
	// 감정에 따른 영화 리스트
	private static String[] p_list = {"WALL-E", "Crayon Shinchan"};	// 기쁠 때 보기 좋은 영화 리스트
	private static String[] s_list = {"Aftersun", "A.I."};	// 슬플 때 보기 좋은 영화 리스트
	private static String[] a_list = {"Bourne Ultimatum", "The Avengers"};	// 화날 때 보기 좋은 영화 리스트
	private static String[] n_list = {"Drive my car", "UP"};	// 불안할 때 보기 좋은 영화 리스트
	
	/* 대기 상태에 따른 영화 추천 */
	public static String weatRecommend(String state) {
		Random random = new Random();	// 랜덤 객체 생성
		random.setSeed(System.currentTimeMillis());
		int randValue = random.nextInt(2);	// 0과 1 중 랜덤으로 뽑기
		String result = "";	// 랜덤으로 선택된 추천 영화
		
		if (state.equals("고온다습")) {
			result = h_h_list[randValue];
		}
		else if (state.equals("온화다습")) {
			result = m_h_list[randValue];
		}
		else if (state.equals("한랭다습")) {
			result = l_h_list[randValue];
		}
		else if (state.equals("고온")) {
			result = h_m_list[randValue];
		}
		else if (state.equals("온화")) {
			result = m_m_list[randValue];
		}
		else if (state.equals("한랭")) {
			result = l_m_list[randValue];
		}
		else if (state.equals("고온건조")) {
			result = h_l_list[randValue];
		}
		else if (state.equals("온화건조")) {
			result = m_l_list[randValue];
		}
		else if (state.equals("한랭건조")) {
			result = l_l_list[randValue];
		}

		return result;
	}
	
	/* 감정에 따른 영화 추천 */
	public static String emotionRecommend(String state) {
		Random random = new Random();	// 랜덤 객체 생성
		random.setSeed(System.currentTimeMillis());
		int randValue = random.nextInt(2);	// 0과 1 중 랜덤으로 뽑기
		String result = "";	// 랜덤으로 선택된 추천 영화
		
		if (state.equals("기쁨")) {
			result = p_list[randValue];
		}
		else if (state.equals("슬픔")) {
			result = s_list[randValue];
		}
		else if (state.equals("화남")) {
			result = a_list[randValue];
		}
		else if (state.equals("불안")) {
			result = n_list[randValue];
		}
		else {	// 잘못 입력하였을 때
			result = "Incorrectly Inputted";	
		}
		
		return result;
	}
}
