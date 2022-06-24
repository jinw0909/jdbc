
package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

/**
 * Ʈ����� - Ʈ����� �Ŵ���
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {
    private final PlatformTransactionManager transactionManager;
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // Ʈ����� ����
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // ����Ͻ� ����
            bizLogic(fromId, toId, money);
            transactionManager.commit(status); //������ Ŀ��
        } catch (Exception e) {
            transactionManager.rollback(status); //���н� �ѹ�
            throw new IllegalStateException(e);
        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("��ü�� ���� �߻�");
        }
    }
}
