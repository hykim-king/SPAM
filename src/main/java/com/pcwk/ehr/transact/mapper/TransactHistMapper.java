/**
 * 파일명: TransactHistMapper.java <br>
 * 작성자: Wholesome-Gee  <br>
 * 생성일: 2026-07-07 <br>
 * 설　명: <br>
 */
package com.pcwk.ehr.transact.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.pcwk.ehr.transact.domain.TransactHistVO;
// 필요 시 검색/페이징을 위한 DTO import
// import com.pcwk.ehr.transact.domain.TransactHistSearchDTO;

public interface TransactHistMapper {

    /**
     * 거래내역 등록 (INSERT)
     */
    int insertTransact(TransactHistVO transact);

    /**
     * 거래내역 상세 조회 (SELECT ONE)
     */
    TransactHistVO selectByTxId(@Param("txId") Long txId);

    /**
     * 거래내역 상태 업데이트 (UPDATE)
     * 예: 거래중 -> 거래완료, 거래취소 등
     */
    int updateStatus(TransactHistVO transact) ;

    /**
     * 거래내역 삭제 (DELETE) - 물리적 삭제보다는 상태 변경을 권장하지만 필요시 구현
     */
    int deleteTransact(@Param("txId") Long txId);
    
    /**
     * 거래내역 전체 삭제 - 테스트 환경 초기화나 운영 데이터 정리 시 사용
     */
    int deleteAll();

    /**
     * [추가기능] 특정 사용자의 거래 목록 조회 (판매자 또는 구매자 기준)
     * mybatis에서 동적 SQL을 활용해 sellerNo 또는 receiverNo로 필터링
     */
    List<TransactHistVO> selectListByUser(@Param("userNum") Long userNum);

    /**
     * [추가기능] 특정 상품과 관련된 거래내역 조회
     * 한 상품에 여러 거래가 발생할 수 있는 경우(경매 등)를 대비
     */
    List<TransactHistVO> selectListByProduct(@Param("productNo") Long productNo);

    /**
     * [추가기능] 총 거래 건수 (페이징용)
     */
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