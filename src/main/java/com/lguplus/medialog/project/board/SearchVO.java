package com.lguplus.medialog.project.board;

import lombok.Data;

@Data
public class SearchVO extends  PageVO  {

    private String bgno;                       // 게시판 그룹
    private String searchKeyword = "";         // 검색 키워드
    private String searchType = "";            // 검색 필드: 제목, 내용  
    private String[] searchTypeArr;            // 검색 필드를 배열로 변환
    

    public String[] getSearchTypeArr() {
        return searchType.split(",");
    }
    
}
 