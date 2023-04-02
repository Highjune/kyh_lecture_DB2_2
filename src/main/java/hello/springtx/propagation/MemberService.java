package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    @Transactional
    public void joinV1(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("== memberRepository 호출 시작==");
        memberRepository.save(member);
        log.info("== memberRepository 호출 종료==");

        log.info("== logRepository 호출 시작==");
        logRepository.save(logMessage);
        log.info("== logRepository 호출 종료==");
    }

    @Transactional
    public void joinV2(String username) {
        Member member = new Member(username);
        Log logMessage = new Log(username);

        log.info("== memberRepository 호출 시작==");
        memberRepository.save(member);
        log.info("== memberRepository 호출 종료==");

        log.info("== logRepository 호출 시작==");
        try {
            logRepository.save(logMessage);
        } catch (RuntimeException e) {
            // 임의 약속) 로그 저장 에러시 생성되는 RuntimeException 은 굳이 고객에게 보여줘서 힘들게 할 필요 없고 그냥 로그로 남기고 나중에 정상복구하자! 라고 정했음.
            log.info("log 저장에 실패했습니다. logMessage={}", logMessage.getMessage());
            log.info("정상 흐름 반환");
        }

        log.info("== logRepository 호출 종료==");
    }


}
