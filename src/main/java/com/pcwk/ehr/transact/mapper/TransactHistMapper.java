/**
 * 파일명: TransactHistMapper.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설 명: 거래 내역 데이터베이스 매퍼 인터페이스 <br>
 */
package com.pcwk.ehr.transact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param; // @Param 임포트
import com.pcwk.ehr.transact.domain.TransactHistVO;

public interface TransactHistMapper {

    // 1. 거래 내역 등록
    int insertTransact(TransactHistVO vo);

    // 2. 특정 거래내역 조회
    TransactHistVO selectByTxId(Long txId);

    // 3. 거래 상태 업데이트
    int updateStatus(TransactHistVO vo);

    // 4. 특정 거래내역 삭제
    int deleteTransact(Long txId);

    // 5. 전체 거래내역 삭제
    int deleteAll();

    // 6. 특정 회원의 거래내역 조회
    List<TransactHistVO> selectListByUser(Long userNum);

    // 7. 특정 상품으로 거래내역 조회 (정렬 조건 추가로 다중 파라미터 매핑 적용)
    List<TransactHistVO> selectListByProduct(@Param("productNo") Long productNo, @Param("sort") String sort);

    // 8. 전체 데이터 총 개수 조회
    int totalCount();
}


//CRUD 기본 메서드:
//
//insertTransact: SEQ_TRANSACTIN_HIST.NEXTVAL을 사용하여 PK를 생성하는 쿼리(XML)와 연결됩니다.
//
//selectByTxId: 고유 txId로 상세 정보를 조회합니다.
//
//updateStatus: 상태 코드(01, 02, 03)와 COMPLETE_DT 또는 CANCEL_DT를 갱신합니다.
//
//추가 기능 (실무적 고려):
//
//selectListByUser: 마이페이지에서 "내가 구매한 물건", "내가 판매한 물건"을 모아볼 때 사용합니다.
//
//selectListByProduct: 특정 물품에 걸려있는 거래 상태를 체크할 때 유용합니다.
//
//@Param 어노테이션: MyBatis에서 파라미터가 1개일 때도 명시적으로 이름을 지정해주면, xml에서 #{userNum}과 같이 쓰기 편하고 유지보수에 유리합니다.