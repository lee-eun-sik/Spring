package front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "front") // 시작점, bin 중에서 첫 시작점임을 나타낸다. 어노테이션을 xml로 만드는 것을 부트가 한다. 
public class BoardFrontEndApplication {

	public static void main(String[] args) {// 얘를 실행시켜서 서버를 올린다.
		SpringApplication.run(BoardFrontEndApplication.class, args);
	}

}
