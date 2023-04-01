package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class TxLevelTest {

    @Autowired LevelService service;

    @Test
    void orderTest() {
        service.write();
        service.read();
    }

    @TestConfiguration
    static class TxLevelTestConfig {

        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }

    @Slf4j
    @Transactional(readOnly = true) // 옵션이 없다면 기본적으로 쓰기, 읽기 둘 다 가능(radOnly = false가 기본값) . readOnly = true 면 쓰기 안됨
    static class LevelService {

        @Transactional(readOnly = false) // readOnly = false 는 읽기, 쓰기 둘 다 가능하다는 것. 쓰기만 되는 것은 없음.
        public void write() {
            log.info("call write");
            printTxInfo();
        }

        // @Transactional이 없지만 클래스 단위에 붙은 @Transactional(readOnly = true) 가 적용된다.
        public void read() {
            log.info("call read");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly = {}", readOnly);
        }
    }
}
